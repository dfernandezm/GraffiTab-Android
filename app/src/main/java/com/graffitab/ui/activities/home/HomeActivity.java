package com.graffitab.ui.activities.home;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.github.clans.fab.FloatingActionButton;
import com.graffitab.R;
import com.graffitab.managers.GTGcmManager;
import com.graffitab.permissions.GTPermissions;
import com.graffitab.ui.activities.home.me.locations.LocationsActivity;
import com.graffitab.ui.activities.home.settings.SettingsActivity;
import com.graffitab.ui.activities.home.streamables.explorer.ExplorerActivity;
import com.graffitab.ui.activities.home.users.ProfileActivity;
import com.graffitab.ui.adapters.viewpagers.ViewPagerTabAdapter;
import com.graffitab.ui.dialog.DialogBuilder;
import com.graffitab.ui.fragments.home.FeedFragment;
import com.graffitab.ui.fragments.home.NotificationsFragment;
import com.graffitab.ui.fragments.home.RecentFragment;
import com.graffitab.ui.fragments.home.TrendingFragment;
import com.graffitab.ui.views.sidemenu.CustomResideMenu;
import com.graffitab.utils.Utils;
import com.graffitab.utils.activity.ActivityUtils;
import com.graffitab.utils.image.ImageUtils;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.network.service.user.response.GTUnseenNotificationsResponse;
import com.graffitabsdk.sdk.GTSDK;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 13/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    static final int NOTIFICATIONS_POSITION = 2;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.fab) FloatingActionButton fab;

    private CustomResideMenu resideMenu;
    private View notificationsIndicator;
    private final int TIME_INTERVAL = 3000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setupTopBar();
        setupViewPager();
        setupTabBar();
        setupMenu();
        setupButtons();

        registerForPushNotifications();

        Utils.runWithDelay(new Runnable() {

            @Override
            public void run() {
                performStartUpAnimations();
            }
        }, 1500);
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        }
        else
            DialogBuilder.buildOKToast(this, getString(R.string.home_exit_prompt));

        mBackPressed = System.currentTimeMillis();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshUnseenNotifications();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (resideMenu != null && resideMenu.isOpened())
            resideMenu.closeMenu();
    }

    @Override
    public void onClick(final View view) {
        resideMenu.closeMenu();

        // Wait for menu to close.
        Utils.runWithDelay(new Runnable() {

            @Override
            public void run() {
                if ((int) view.getTag() == 0) // Profile.
                    ProfileActivity.show(HomeActivity.this, GTSDK.getAccountManager().getLoggedInUser());
                else if ((int) view.getTag() == 1) // Locations.
                    startActivity(new Intent(HomeActivity.this, LocationsActivity.class));
                else if ((int) view.getTag() == 2) // Search.
                    SearchActivity.openSearch(HomeActivity.this, null);
                else if ((int) view.getTag() == 3) // Settings.
                    startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
            }
        }, 300);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        ActivityUtils.colorMenu(this, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (!resideMenu.isOpened())
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);

            return true;
        }
        else if (item.getItemId() == R.id.action_map) {
            openExplorer();
            return true;
        }
        else if (item.getItemId() == R.id.action_search) {
            SearchActivity.openSearch(HomeActivity.this, null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isNotificationsTabSelected() {
        return viewPager.getCurrentItem() == NOTIFICATIONS_POSITION;
    }

    private void openExplorer() {
        final Runnable explorer = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(HomeActivity.this, ExplorerActivity.class));
            }
        };

        GTPermissions.manager.checkPermission(this, GTPermissions.PermissionType.LOCATION, new GTPermissions.OnPermissionResultListener() {

            @Override
            public void onPermissionGranted() {
                explorer.run();
            }

            @Override
            public void onPermissionDenied() {
                explorer.run();
            }

            @Override
            public void onDecideLater() {
                explorer.run();
            }
        });
    }

    // Loading

    private void refreshNotificationsTab() {
        ViewPagerTabAdapter adapter = (ViewPagerTabAdapter) viewPager.getAdapter();
        NotificationsFragment fragment = (NotificationsFragment) adapter.getItem(NOTIFICATIONS_POSITION);
        fragment.advancedRecyclerView.beginRefreshing(); // Force notifications refresh.
        fragment.reload();
    }

    private void refreshUnseenNotifications() {
        GTSDK.getMeManager().getUnseenNotifications(new GTResponseHandler<GTUnseenNotificationsResponse>() {

            @Override
            public void onSuccess(GTResponse<GTUnseenNotificationsResponse> gtResponse) {
                if (viewPager == null) return; // View is destroyed.

                if (gtResponse.getObject().count <= 0) return; // No new notifications.
                if (isNotificationsTabSelected())
                    refreshNotificationsTab();
                else
                    notificationsIndicator.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(GTResponse<GTUnseenNotificationsResponse> gtResponse) {}
        });
    }

    private void performStartUpAnimations() {
        fab.setVisibility(View.VISIBLE);
        fab.animate().scaleX(1);
        fab.animate().scaleY(1);
    }

    // GCM

    private void registerForPushNotifications() {
        GTGcmManager.sharedInstance.refreshGcmToken(this);
    }

    // Setup

    private void setupTopBar() {
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getString(R.string.home));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ImageUtils.tintIcon(this, R.drawable.ic_menu_black_24dp, getResources().getColor(R.color.colorPrimary)));
    }

    private void setupMenu() {
        // Attach to current activity.
        resideMenu = new CustomResideMenu(this);
        resideMenu.setBackground(R.drawable.login_full);
        resideMenu.attachToActivity(this);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        resideMenu.addIgnoredView(viewPager);
        resideMenu.setFitsSystemWindows(true);

        // Create menu items.
        String titles[] = {getString(R.string.home_profile), getString(R.string.home_locations), getString(R.string.home_search), getString(R.string.home_settings)};
        int icon[] = { R.drawable.ic_account_circle_white_24dp, R.drawable.ic_location_on_white_24dp, R.drawable.ic_search_white_24dp, R.drawable.ic_settings_white_24dp};

        for (int i = 0; i < titles.length; i++){
            ResideMenuItem item = new ResideMenuItem(this, icon[i], titles[i]);
            item.setTag(i);
            item.setOnClickListener(this);
            resideMenu.addMenuItem(item,  ResideMenu.DIRECTION_LEFT);
        }
    }

    private void setupViewPager() {
        final ViewPagerTabAdapter adapter = new ViewPagerTabAdapter(getSupportFragmentManager());
        adapter.addFragment(new FeedFragment(), getString(R.string.home));
        adapter.addFragment(new TrendingFragment(), getString(R.string.home_trending));
        adapter.addFragment(new NotificationsFragment(), getString(R.string.home_notifications));
        adapter.addFragment(new RecentFragment(), getString(R.string.home_recent));
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setTitle(adapter.getCurrentPageTitle(position));
                setupTabBarColors(position);

                if (isNotificationsTabSelected()) {
                    if (notificationsIndicator.getVisibility() == View.VISIBLE) // We have new notifications, so when switching to the tab refresh the content.
                        refreshNotificationsTab();
                    notificationsIndicator.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void setupTabBar() {
        tabLayout.setupWithViewPager(viewPager);

        // Set custom views for all tabs.
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            View v = LayoutInflater.from(this).inflate(R.layout.view_tab_home, null);
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(v);

            if (i == NOTIFICATIONS_POSITION)
                notificationsIndicator = v.findViewById(R.id.tabNotificationsIndicator);
        }
        setupTabBarColors(0);
    }

    private void setupTabBarColors(int selectedPosition) {
        int[] tabIcons = {
                R.drawable.ic_home_black_24dp,
                R.drawable.ic_whatshot_black_24dp,
                R.drawable.ic_notifications_none_black_24dp,
                R.drawable.ic_access_time_black_24dp
        };

        for (int i = 0; i < tabIcons.length; i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            ImageView tabImage = (ImageView) tab.getCustomView().findViewById(R.id.tabIcon);
            tabImage.setImageDrawable(ImageUtils.tintIcon(this, tabIcons[i], getResources().getColor(i == selectedPosition ? R.color.colorPrimary : R.color.colorHomeUnselected)));
        }
    }

    private void setupButtons() {
        fab.setVisibility(View.GONE);
        fab.animate().scaleX(0);
        fab.animate().scaleY(0);
    }
}
