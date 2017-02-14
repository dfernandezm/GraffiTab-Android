package com.graffitab.ui.fragments.users;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.graffitab.R;
import com.graffitab.application.MyApplication;
import com.graffitab.ui.activities.home.users.ProfileActivity;
import com.graffitab.ui.adapters.users.GenericUsersRecyclerViewAdapter;
import com.graffitab.ui.adapters.users.OnUserClickListener;
import com.graffitab.ui.adapters.users.viewholders.UserViewHolder;
import com.graffitab.ui.dialog.DialogBuilder;
import com.graffitab.ui.dialog.OnYesNoHandler;
import com.graffitab.ui.fragments.GenericItemListFragment;
import com.graffitab.ui.views.recyclerview.AdvancedEndlessRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.AdvancedRecyclerViewItemDecoration;
import com.graffitab.ui.views.recyclerview.AdvancedRecyclerViewLayoutConfiguration;
import com.graffitabsdk.model.GTUser;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.network.service.user.response.GTUserResponse;
import com.graffitabsdk.sdk.GTSDK;
import com.graffitabsdk.sdk.events.users.GTUserFollowedEvent;
import com.graffitabsdk.sdk.events.users.GTUserProfileUpdatedEvent;
import com.graffitabsdk.sdk.events.users.GTUserUnfollowedEvent;
import com.squareup.otto.Subscribe;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public abstract class GenericUsersFragment extends GenericItemListFragment<GTUser> implements OnUserClickListener {

    public enum ViewType {LIST_FULL}

    private ViewType viewType;
    private Object eventListener;

    @Override
    public void basicInit() {
        super.basicInit();

        eventListener = new Object() {

            @Subscribe
            public void userFollowedEvent(GTUserFollowedEvent event) {
                if (getRecyclerView() == null || getRecyclerView().getRecyclerView() == null) return;
                refreshUserAfterFollowToggle(event.getUser());
            }

            @Subscribe
            public void userUnfollowedEvent(GTUserUnfollowedEvent event) {
                if (getRecyclerView() == null || getRecyclerView().getRecyclerView() == null) return;
                refreshUserAfterFollowToggle(event.getUser());
            }

            @Subscribe
            public void userProfileUpdatedEvent(GTUserProfileUpdatedEvent event) {
                if (getRecyclerView() == null || getRecyclerView().getRecyclerView() == null) return;
                refreshUserAfterFollowToggle(event.getUser());
            }

            private void refreshUserAfterFollowToggle(GTUser toggledUser) {
                int index = items.indexOf(toggledUser);
                if (index >= 0) {
                    items.set(index, toggledUser);
                    adapter.setItems(items, getRecyclerView().getRecyclerView());
                }
            }
        };
        GTSDK.registerEventListener(eventListener);
    }

    @Override
    public void onDestroyView() {
        if (eventListener != null)
            GTSDK.unregisterEventListener(eventListener);
        super.onDestroyView();
    }

    @Override
    public int emptyViewImageResource() {
        return -1;
    }

    @Override
    public String emptyViewTitle() {
        return getString(R.string.other_empty_no_users);
    }

    @Override
    public String emptyViewSubtitle() {
        return getString(R.string.other_empty_no_users_description);
    }

    public void setViewType(ViewType type) {
        this.viewType = type;
    }

    public void switchViewType(ViewType type) {
        setViewType(type);

        ((GenericUsersRecyclerViewAdapter) adapter).setViewType(viewType);
        configureLayout();
    }

    @Override
    public void onRowSelected(GTUser user, int adapterPosition) {
        ProfileActivity.show(user, getActivity());
    }

    @Override
    public void onToggleFollow(final GTUser user, UserViewHolder holder, int adapterPosition) {
        final Runnable followRunnable = new Runnable() {

            @Override
            public void run() {
                user.followedByCurrentUser = !user.followedByCurrentUser;
                adapter.notifyDataSetChanged();
                if (user.followedByCurrentUser)
                    GTSDK.getUserManager().follow(user.id, new GTResponseHandler<GTUserResponse>() {

                        @Override
                        public void onSuccess(GTResponse<GTUserResponse> gtResponse) {}

                        @Override
                        public void onFailure(GTResponse<GTUserResponse> gtResponse) {}
                    });
                else
                    GTSDK.getUserManager().unfollow(user.id, new GTResponseHandler<GTUserResponse>() {

                        @Override
                        public void onSuccess(GTResponse<GTUserResponse> gtResponse) {}

                        @Override
                        public void onFailure(GTResponse<GTUserResponse> gtResponse) {}
                    });
            }
        };

        if (user.followedByCurrentUser) {
            DialogBuilder.buildUnfollowDialog(getActivity(), user, new OnYesNoHandler() {

                @Override
                public void onClickYes() {
                    followRunnable.run();
                }

                @Override
                public void onClickNo() {}
            });
        }
        else
            followRunnable.run();
    }

    // Configuration

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        if (viewType == ViewType.LIST_FULL)
            return new AdvancedRecyclerViewItemDecoration(1, 0);
        return null;
    }

    @Override
    public AdvancedEndlessRecyclerViewAdapter getAdapterForViewType() {
        GenericUsersRecyclerViewAdapter customAdapter = new GenericUsersRecyclerViewAdapter(MyApplication.getInstance(), items, getRecyclerView().getRecyclerView());
        customAdapter.setViewType(viewType);
        customAdapter.setClickListener(this);
        return customAdapter;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManagerForViewType() {
        if (viewType == ViewType.LIST_FULL)
            return new LinearLayoutManager(getContext());
        return null;
    }

    @Override
    public AdvancedRecyclerViewLayoutConfiguration getLayoutConfiguration() {
        return null;
    }
}
