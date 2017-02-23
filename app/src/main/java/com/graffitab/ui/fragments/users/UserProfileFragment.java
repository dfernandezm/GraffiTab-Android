package com.graffitab.ui.fragments.users;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitab.application.MyApplication;
import com.graffitab.ui.activities.home.users.ProfileActivity;
import com.graffitab.ui.adapters.profile.UserProfileHeaderAdapter;
import com.graffitab.ui.adapters.streamables.GenericStreamablesRecyclerViewAdapter;
import com.graffitab.ui.adapters.viewpagers.ProfileViewPagerAdapter;
import com.graffitab.ui.fragments.streamables.ListStreamablesFragment;
import com.graffitab.ui.views.recyclerview.AdvancedEndlessRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.AdvancedRecyclerViewItemDecoration;
import com.graffitab.utils.activity.ActivityUtils;
import com.graffitab.utils.image.ImageUtils;
import com.graffitabsdk.constants.GTConstants;
import com.graffitabsdk.model.GTStreamable;
import com.graffitabsdk.model.GTUser;
import com.graffitabsdk.network.common.params.GTQueryParameters;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.network.service.user.response.GTUserResponse;
import com.graffitabsdk.sdk.GTSDK;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by georgichristov on 21/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class UserProfileFragment extends ListStreamablesFragment {

    private Menu menu;
    private View header;
    private ImageView cover;
    private ImageView avatar;
    private TextView usernameField;
    private TextView nameField;
    private TextView postsField;
    private TextView followersField;
    private TextView followingField;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private ImageView followButtonIcon;
    private TextView followButtonText;
    private View followButtonLayout;
    private View followButtonSeparator;

    private GTUser user;
    private ProfileViewPagerAdapter pagerAdapter;
    private int lastScrollYOffset;
    private boolean loadedInitially = false;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (user.isMe()) // Show settings only if user is current logged in user.
            inflater.inflate(R.menu.menu_settings, menu);
        else
            super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setupHeaderView();
    }

    public boolean toggleFollow() {
        user.followedByCurrentUser = !user.followedByCurrentUser;
        if (user.followedByCurrentUser) { // Follow.
            user.addToFollowersCount();
            GTSDK.getUserManager().follow(user.id, new GTResponseHandler<GTUserResponse>() {

                @Override
                public void onSuccess(GTResponse<GTUserResponse> gtResponse) {
                    user = gtResponse.getObject().user;
                    loadUserCountData();
                }

                @Override
                public void onFailure(GTResponse<GTUserResponse> gtResponse) {}
            });
        }
        else { // Unfollow.
            user.removeFromFollowersCount();
            GTSDK.getUserManager().unfollow(user.id, new GTResponseHandler<GTUserResponse>() {

                @Override
                public void onSuccess(GTResponse<GTUserResponse> gtResponse) {
                    user = gtResponse.getObject().user;
                    loadUserCountData();
                }

                @Override
                public void onFailure(GTResponse<GTUserResponse> gtResponse) {}
            });
        }

        loadUserCountData();
        loadFollowButton();

        return user.followedByCurrentUser;
    }

    // Loading

    public void loadFollowButton() {
        if (getActivity() == null) return; // View is destroyed.

        followButtonIcon.setImageDrawable(user.followedByCurrentUser ? ImageUtils.tintIcon(getActivity(), R.drawable.ic_action_unfollow, getResources().getColor(R.color.colorBlack)) : ImageUtils.tintIcon(getActivity(), R.drawable.ic_action_follow, getResources().getColor(R.color.colorPrimary)));
        followButtonText.setText(user.followedByCurrentUser ? getString(R.string.profile_following) : getString(R.string.profile_follow));
        followButtonText.setTextColor(user.followedByCurrentUser ? Color.parseColor("#AAAAAA") : getResources().getColor(R.color.colorPrimary));

        followButtonLayout.setVisibility(user.isMe() ? View.GONE : View.VISIBLE); // Only show follow button if we're viewing some else's profile.
        followButtonSeparator.setVisibility(followButtonLayout.getVisibility());
    }

    public void loadUserCountData() {
        if (getActivity() == null) return; // View is destroyed.

        postsField.setText(user.streamablesCountAsString());
        followersField.setText(user.followersCountAsString());
        followingField.setText(user.followingCountAsString());
    }

    public void loadUserNamesAndHeaderData() {
        nameField.setText(user.fullName());
        usernameField.setText(user.mentionUsername());
        circleIndicator.setVisibility(user.aboutString().length() > 0 ? View.VISIBLE : View.GONE);
        viewPager.setAlpha(circleIndicator.getVisibility() == View.VISIBLE ? 1 : 0);
    }

    public void loadUserAssets() {
        loadAvatar();
        loadCover();
    }

    private void loadAvatar() {
        ImageUtils.setAvatar(avatar, user);
    }

    private void loadCover() {
        ImageUtils.setCover(cover, user);
    }

    @Override
    public void loadItems(boolean isFirstLoad, int offset, GTResponseHandler handler) {
        GTQueryParameters parameters = new GTQueryParameters();
        parameters.addParameter(GTQueryParameters.GTParameterType.OFFSET, offset);
        parameters.addParameter(GTQueryParameters.GTParameterType.LIMIT, GTConstants.MAX_ITEMS);
        GTSDK.getUserManager().getPosts(user.id, isFirstLoad, parameters, handler);

        if (offset == 0 && loadedInitially) // If we pull, refresh user profile. Skip first time.
            ((ProfileActivity) getActivity()).reloadUserProfile();
        loadedInitially = true;
    }

    @Override
    public void onOpenOwnerProfile(GTStreamable streamable, GTUser owner, int adapterPosition) {
        // Profile is already opened.
    }

    // Configuration

    @Override
    public AdvancedEndlessRecyclerViewAdapter getAdapterForViewType() {
        GenericStreamablesRecyclerViewAdapter customAdapter = new UserProfileHeaderAdapter(MyApplication.getInstance(), items, getRecyclerView().getRecyclerView());
        customAdapter.setViewType(getViewType());
        customAdapter.setClickListener(this);
        return customAdapter;
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new RecyclerView.ItemDecoration() {

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int itemCount = state.getItemCount();
                int position = parent.getChildLayoutPosition(view);

                if (position == 0) {
                    outRect.top = 0;
                    outRect.left = 0;
                    outRect.right = 0;
                    outRect.bottom = 2;
                }
                else {
                    AdvancedRecyclerViewItemDecoration decoration = (AdvancedRecyclerViewItemDecoration) UserProfileFragment.super.getItemDecoration();
                    decoration.setPadEdges(false);
                    decoration.getItemOffsets(outRect, view, parent, state);
                }
            }
        };
    }

    @Override
    public void finalizeLoad() {
        super.finalizeLoad();

        advancedRecyclerView.addOnEmptyViewListener(null);
    }

    @Override
    public void setupCustomViews() {
        super.setupCustomViews();

        setupHeaderView();

        adapter.addHeaderView(header, advancedRecyclerView.getRecyclerView());

        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        final ActionBar actionBar = activity.getSupportActionBar();
        final Drawable actionBarDrawable = new ColorDrawable(0xffffffff);
        actionBarDrawable.setAlpha(0);
        actionBar.setBackgroundDrawable(actionBarDrawable);

        getRecyclerView().getRecyclerView().setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                RecyclerView.ViewHolder holder = recyclerView.findViewHolderForLayoutPosition(0);
                if (holder != null) {
                    lastScrollYOffset = -holder.itemView.getTop();

                    final int headerHeight = holder.itemView.getHeight() / 2 - actionBar.getHeight();
                    final float ratio = (float) Math.min(Math.max(lastScrollYOffset, 0), headerHeight) / headerHeight;
                    final int newAlpha = (int) (ratio * 255);
                    actionBarDrawable.setAlpha(newAlpha);

                    if (lastScrollYOffset > 400) {
                        String title = user.fullName();
                        String subtitle = user.mentionUsername();

                        Spannable coloredTitle = new SpannableString(title);
                        coloredTitle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 0, coloredTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        Spannable coloredSubtitle = new SpannableString(subtitle);
                        coloredSubtitle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorMetadata)), 0, coloredSubtitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                        activity.getSupportActionBar().setTitle(coloredTitle);
                        activity.getSupportActionBar().setSubtitle(coloredSubtitle);

                        activity.getSupportActionBar().setHomeAsUpIndicator(ImageUtils.tintIcon(getContext(), R.drawable.ic_arrow_back_black_24dp, getResources().getColor(R.color.colorPrimary)));

                        ActivityUtils.colorMenu(getContext(), menu, R.color.colorPrimary);
                    }
                    else {
                        activity.getSupportActionBar().setTitle("");
                        activity.getSupportActionBar().setSubtitle("");

                        activity.getSupportActionBar().setHomeAsUpIndicator(ImageUtils.tintIcon(getContext(), R.drawable.ic_arrow_back_black_24dp, getResources().getColor(R.color.colorWhite)));

                        ActivityUtils.colorMenu(getContext(), menu, R.color.colorWhite);
                    }
                }
            }
        });
    }

    public void setUser(GTUser user) {
        this.user = user;
        if (pagerAdapter != null) pagerAdapter.setUser(user);

        if (adapter != null) {
            // Refresh the streamables' user.
            for (GTStreamable streamable : items) {
                if (streamable.user.equals(user))
                    streamable.user = user;
            }
            adapter.notifyDataSetChanged();
        }
    }

    // Setup

    private void setupHeaderView() {
        if (header == null)
            header = LayoutInflater.from(getActivity()).inflate(R.layout.decoration_header_profile, advancedRecyclerView, false);

        nameField = (TextView) header.findViewById(R.id.nameField);
        usernameField = (TextView) header.findViewById(R.id.usernameField);
        postsField = (TextView) header.findViewById(R.id.postsField);
        followersField = (TextView) header.findViewById(R.id.followersField);
        followingField = (TextView) header.findViewById(R.id.followingField);
        cover = (ImageView) header.findViewById(R.id.cover);
        avatar = (ImageView) header.findViewById(R.id.avatar);
        viewPager = (ViewPager) header.findViewById(R.id.viewpager);
        circleIndicator = (CircleIndicator) header.findViewById(R.id.indicator);
        followButtonIcon = (ImageView) header.findViewById(R.id.followButtonIcon);
        followButtonText = (TextView) header.findViewById(R.id.followButtonText);
        followButtonLayout = header.findViewById(R.id.followButtonLayout);
        followButtonSeparator = header.findViewById(R.id.followButtonSeparator);

        final GestureDetector tapGestureDetector = new GestureDetector(getActivity(), new TapGestureListener());
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                tapGestureDetector.onTouchEvent(event);
                getRecyclerView().getRefreshLayout().setEnabled(false);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_OUTSIDE:
                        getRecyclerView().getRefreshLayout().setEnabled(true);
                        break;
                }
                return false;
            }
        });

        pagerAdapter = new ProfileViewPagerAdapter(getContext(), viewPager, user);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ProfileViewPagerAdapter.ProfilePagerChangeListener(viewPager));
        circleIndicator.setViewPager(viewPager);

        loadUserNamesAndHeaderData();
        loadUserAssets();
        loadFollowButton();
    }

    class TapGestureListener extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (viewPager.getCurrentItem() == 0)
                ((ProfileActivity) getActivity()).onClickCover(cover);
            else
                ((ProfileActivity) getActivity()).onClickAbout(null);
            return true;
        }
    }
}
