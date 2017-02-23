package com.graffitab.ui.fragments.streamables;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.graffitab.R;
import com.graffitab.application.MyApplication;
import com.graffitab.constants.Constants;
import com.graffitab.ui.activities.home.streamables.CommentsActivity;
import com.graffitab.ui.activities.home.streamables.LikesActivity;
import com.graffitab.ui.activities.home.streamables.StreamableDetailsActivity;
import com.graffitab.ui.activities.home.users.ProfileActivity;
import com.graffitab.ui.adapters.streamables.GenericStreamablesRecyclerViewAdapter;
import com.graffitab.ui.adapters.streamables.OnStreamableClickListener;
import com.graffitab.ui.adapters.streamables.viewholders.StreamableViewHolder;
import com.graffitab.ui.fragments.GenericItemListFragment;
import com.graffitab.ui.views.recyclerview.AdvancedEndlessRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.AdvancedRecyclerViewItemDecoration;
import com.graffitab.ui.views.recyclerview.AdvancedRecyclerViewLayoutConfiguration;
import com.graffitab.utils.Utils;
import com.graffitab.utils.device.DeviceUtils;
import com.graffitabsdk.model.GTStreamable;
import com.graffitabsdk.model.GTUser;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.network.service.streamable.response.GTStreamableResponse;
import com.graffitabsdk.sdk.GTSDK;
import com.graffitabsdk.sdk.events.comments.GTCommentPostedEvent;
import com.graffitabsdk.sdk.events.streamables.GTStreamableLikedEvent;
import com.graffitabsdk.sdk.events.streamables.GTStreamableUnlikedEvent;
import com.graffitabsdk.sdk.events.users.GTUserAvatarUpdatedEvent;
import com.graffitabsdk.sdk.events.users.GTUserProfileUpdatedEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public abstract class GenericStreamablesFragment extends GenericItemListFragment<GTStreamable> implements OnStreamableClickListener {

    public enum ViewType {GRID, TRENDING, SWIMLANE, LIST_FULL}

    private ViewType viewType;
    private Object eventListener;

    @Override
    public void basicInit() {
        super.basicInit();

        eventListener = new Object() {

            @Subscribe
            public void userProfileUpdatedEvent(GTUserProfileUpdatedEvent event) {
                if (getRecyclerView() == null || getRecyclerView().getRecyclerView() == null) return;
                refreshStreamablesAfterUserProfileChange(event.getUser());
            }

            @Subscribe
            public void userAvatarUpdatedEvent(GTUserAvatarUpdatedEvent event) {
                if (getRecyclerView() == null || getRecyclerView().getRecyclerView() == null) return;
                refreshStreamablesAfterUserProfileChange(GTSDK.getAccountManager().getLoggedInUser());
            }

            @Subscribe
            public void streamableLikedEvent(GTStreamableLikedEvent event) {
                if (getRecyclerView() == null || getRecyclerView().getRecyclerView() == null) return;
                refreshStreamableAfterLikeToggle(event.getStreamable());
            }

            @Subscribe
            public void streamableUnlikedEvent(GTStreamableUnlikedEvent event) {
                if (getRecyclerView() == null || getRecyclerView().getRecyclerView() == null) return;
                refreshStreamableAfterLikeToggle(event.getStreamable());
            }

            @Subscribe
            public void commentPostedEvent(GTCommentPostedEvent event) {
                if (getRecyclerView() == null || getRecyclerView().getRecyclerView() == null) return;
                for (int i = 0; i < items.size(); i++) {
                    GTStreamable streamable = items.get(i);
                    if (streamable.equals(event.getComment().streamable)) {
                        items.set(i, event.getComment().streamable);
                        adapter.setItem(event.getComment().streamable, i, getRecyclerView().getRecyclerView());
                        break;
                    }
                }
            }

            private void refreshStreamablesAfterUserProfileChange(GTUser user) {
                List<Integer> indexes = indexesOfStreamablesForOwner(user);
                if (indexes.size() >= 0) {
                    // Refresh streamable users.
                    for (Integer index : indexes)
                        items.get(index).user = user;
                    adapter.setItems(items, indexes, getRecyclerView().getRecyclerView());
                }
            }

            private void refreshStreamableAfterLikeToggle(GTStreamable toggledStreamable) {
                int index = items.indexOf(toggledStreamable);
                if (index >= 0) {
                    items.set(index, toggledStreamable);
                    adapter.setItem(toggledStreamable, index, getRecyclerView().getRecyclerView());
                }
            }

            private List<Integer> indexesOfStreamablesForOwner(GTUser owner) {
                List<Integer> indexes = new ArrayList<>();
                for (int i = 0; i < items.size(); i++) {
                    GTStreamable streamable = items.get(i);
                    if (streamable.user.equals(owner))
                        indexes.add(i);
                }
                return indexes;
            }
        };
    }

    @Override
    public void onPause() {
        super.onPause();
        GTSDK.registerEventListener(eventListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        GTSDK.unregisterEventListener(eventListener);
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
        return getString(R.string.other_empty_no_posts);
    }

    @Override
    public String emptyViewSubtitle() {
        return getString(R.string.other_empty_no_posts_description);
    }

    public ViewType getViewType() {
        return viewType;
    }

    public void setViewType(ViewType type) {
        this.viewType = type;
    }

    public void switchViewType(ViewType type) {
        setViewType(type);

        ((GenericStreamablesRecyclerViewAdapter) adapter).setViewType(viewType);
        configureLayout();
    }

    @Override
    public void onRowSelected(GTStreamable streamable, int adapterPosition) {
        View source = null;
        View v = getRecyclerView().getRecyclerView().getLayoutManager().findViewByPosition(adapterPosition);
        if (v != null)
            source = v.findViewById(R.id.streamableView);
        StreamableDetailsActivity.openStreamableDetails(getActivity(), streamable, source);
    }

    @Override
    public void onOpenComments(GTStreamable streamable, int adapterPosition) {
        Intent i = new Intent(getActivity(), CommentsActivity.class);
        i.putExtra(Constants.EXTRA_STREAMABLE, streamable);
        startActivity(i);
    }

    @Override
    public void onOpenLikes(GTStreamable streamable, int adapterPosition) {
        Intent intent = new Intent(getActivity(), LikesActivity.class);
        intent.putExtra(Constants.EXTRA_STREAMABLE, streamable);
        startActivity(intent);
    }

    @Override
    public void onOpenOwnerProfile(GTStreamable streamable, GTUser owner, int adapterPosition) {
        ProfileActivity.show(getActivity(), owner);
    }

    @Override
    public void onShare(GTStreamable streamable, Bitmap image, int adapterPosition) {
        Utils.shareImage(getActivity(), image);
    }

    @Override
    public void onToggleLike(GTStreamable streamable, StreamableViewHolder holder, int adapterPosition) {
        streamable.likedByCurrentUser = !streamable.likedByCurrentUser;
        if (streamable.likedByCurrentUser) {
            streamable.addToLikersCount();
            GTSDK.getStreamableManager().like(streamable.id, new GTResponseHandler<GTStreamableResponse>() {

                @Override
                public void onSuccess(GTResponse<GTStreamableResponse> gtResponse) {}

                @Override
                public void onFailure(GTResponse<GTStreamableResponse> gtResponse) {}
            });
        }
        else {
            streamable.removeFromLikersCount();
            GTSDK.getStreamableManager().unlike(streamable.id, new GTResponseHandler<GTStreamableResponse>() {

                @Override
                public void onSuccess(GTResponse<GTStreamableResponse> gtResponse) {}

                @Override
                public void onFailure(GTResponse<GTStreamableResponse> gtResponse) {}
            });
        }
        adapter.notifyDataSetChanged();
    }

    // Configuration

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        if (viewType == ViewType.GRID) {
            if (DeviceUtils.isLandscape(MyApplication.getInstance()))
                return new AdvancedRecyclerViewItemDecoration(getResources().getInteger(R.integer.streamables_grid_columns_landscape), (int) getResources().getDimension(R.dimen.row_streamable_grid_spacing));
            else
                return new AdvancedRecyclerViewItemDecoration(getResources().getInteger(R.integer.streamables_grid_columns_portrait), (int) getResources().getDimension(R.dimen.row_streamable_grid_spacing));
        }
        else if (viewType == ViewType.LIST_FULL) {
            final int spacing = (int) getResources().getDimension(R.dimen.row_streamable_list_spacing);

            return new AdvancedRecyclerViewItemDecoration(getResources().getInteger(R.integer.streamables_list_columns), spacing) {

                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    int itemCount = state.getItemCount();
                    int position = parent.getChildLayoutPosition(view);

                    outRect.right = 0;
                    outRect.left = 0;
                    outRect.top = DeviceUtils.pxToDip(MyApplication.getInstance(), spacing);

                    if (position == itemCount - 1)
                        outRect.bottom = DeviceUtils.pxToDip(MyApplication.getInstance(), spacing);
                }
            };
        }
        else if (viewType == ViewType.SWIMLANE) {
            if (DeviceUtils.isLandscape(MyApplication.getInstance()))
                return new AdvancedRecyclerViewItemDecoration(getResources().getInteger(R.integer.streamables_swimlane_columns_landscape), (int) getResources().getDimension(R.dimen.row_streamable_swimlane_spacing));
            else
                return new AdvancedRecyclerViewItemDecoration(getResources().getInteger(R.integer.streamables_swimlane_columns_portrait), (int) getResources().getDimension(R.dimen.row_streamable_swimlane_spacing));
        }
        else if (viewType == ViewType.TRENDING) {
            if (DeviceUtils.isLandscape(MyApplication.getInstance()))
                return new AdvancedRecyclerViewItemDecoration(getResources().getInteger(R.integer.streamables_trending_columns_landscape), (int) getResources().getDimension(R.dimen.row_streamable_trending_spacing));
            else
                return new AdvancedRecyclerViewItemDecoration(getResources().getInteger(R.integer.streamables_trending_columns_portrait), (int) getResources().getDimension(R.dimen.row_streamable_trending_spacing));
        }
        return null;
    }

    @Override
    public AdvancedEndlessRecyclerViewAdapter getAdapterForViewType() {
        GenericStreamablesRecyclerViewAdapter customAdapter = new GenericStreamablesRecyclerViewAdapter(MyApplication.getInstance(), items, getRecyclerView().getRecyclerView());
        customAdapter.setViewType(viewType);
        customAdapter.setClickListener(this);
        return customAdapter;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManagerForViewType() {
        if (viewType == ViewType.GRID)
            return new GridLayoutManager(MyApplication.getInstance(), getResources().getInteger(R.integer.streamables_grid_columns_portrait));
        else if (viewType == ViewType.LIST_FULL)
            return new LinearLayoutManager(MyApplication.getInstance());
        else if (viewType == ViewType.SWIMLANE)
            return new StaggeredGridLayoutManager(getResources().getInteger(R.integer.streamables_swimlane_columns_portrait), StaggeredGridLayoutManager.VERTICAL);
        else if (viewType == ViewType.TRENDING)
            return new StaggeredGridLayoutManager(getResources().getInteger(R.integer.streamables_trending_columns_portrait), StaggeredGridLayoutManager.VERTICAL);
        return null;
    }

    @Override
    public AdvancedRecyclerViewLayoutConfiguration getLayoutConfiguration() {
        if (viewType == ViewType.GRID) {
            if (DeviceUtils.isLandscape(MyApplication.getInstance()))
                return new AdvancedRecyclerViewLayoutConfiguration(getResources().getInteger(R.integer.streamables_grid_columns_landscape));
            else
                return new AdvancedRecyclerViewLayoutConfiguration(getResources().getInteger(R.integer.streamables_grid_columns_portrait));
        }
        else if (viewType == ViewType.LIST_FULL)
            return null;
        else if (viewType == ViewType.SWIMLANE) {
            if (DeviceUtils.isLandscape(MyApplication.getInstance()))
                return new AdvancedRecyclerViewLayoutConfiguration(getResources().getInteger(R.integer.streamables_swimlane_columns_landscape));
            else
                return new AdvancedRecyclerViewLayoutConfiguration(getResources().getInteger(R.integer.streamables_swimlane_columns_portrait));
        }
        else if (viewType == ViewType.TRENDING) {
            if (DeviceUtils.isLandscape(MyApplication.getInstance()))
                return new AdvancedRecyclerViewLayoutConfiguration(getResources().getInteger(R.integer.streamables_trending_columns_landscape));
            else
                return new AdvancedRecyclerViewLayoutConfiguration(getResources().getInteger(R.integer.streamables_trending_columns_portrait));
        }
        return null;
    }
}
