package com.graffitab.ui.activities.home;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.github.clans.fab.FloatingActionButton;
import com.graffitab.R;
import com.graffitab.graffitabsdk.log.GTLog;
import com.graffitab.graffitabsdk.managers.api.GTApiManager;
import com.graffitab.graffitabsdk.managers.api.GTUserManager;
import com.graffitab.graffitabsdk.model.GTUser;
import com.graffitab.graffitabsdk.network.common.GTResponseObject;
import com.graffitab.graffitabsdk.network.common.ResponseHandler;
import com.graffitab.ui.activities.home.me.locations.LocationsActivity;
import com.graffitab.ui.activities.home.users.ProfileActivity;
import com.graffitab.ui.adapters.viewpagers.ViewPagerTabAdapter;
import com.graffitab.ui.fragments.home.FeedFragment;
import com.graffitab.ui.fragments.home.NotificationsFragment;
import com.graffitab.ui.fragments.home.RecentFragment;
import com.graffitab.ui.fragments.home.TrendingFragment;
import com.graffitab.ui.views.sidemenu.CustomResideMenu;
import com.graffitab.utils.ImageUtils;
import com.graffitab.utils.Utils;
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

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.fab) FloatingActionButton fab;

    private CustomResideMenu resideMenu;

    private int[] tabIcons = {
            R.drawable.ic_home_black_24dp,
            R.drawable.ic_whatshot_black_24dp,
            R.drawable.ic_notifications_none_black_24dp,
            R.drawable.ic_access_time_black_24dp
    };

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
                    startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                else if ((int) view.getTag() == 1) // Locations.
                    startActivity(new Intent(HomeActivity.this, LocationsActivity.class));
                else if ((int) view.getTag() == 2) // Search.
                    startActivity(new Intent(HomeActivity.this, SearchActivity.class));
                else if ((int) view.getTag() == 3) { // Settings.

                }
            }
        }, 300);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);

        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            Drawable drawable = menuItem.getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            }
        }

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
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        resideMenu.setBackground(R.drawable.login);
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
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void setupTabBar() {
        tabLayout.setupWithViewPager(viewPager);
        setupTabBarColors(0);
    }

    private void setupTabBarColors(int selectedPosition) {
        for (int i = 0; i < tabIcons.length; i++)
            tabLayout.getTabAt(i).setIcon(ImageUtils.tintIcon(this, tabIcons[i], getResources().getColor(i == selectedPosition ? R.color.colorPrimary : R.color.colorHomeUnselected)));
    }
}
