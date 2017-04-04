package com.graffitab.ui.fragments.followersactivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.graffitab.R;
import com.graffitab.application.MyApplication;
import com.graffitab.ui.activities.home.streamables.StreamableDetailsActivity;
import com.graffitab.ui.activities.home.streamables.explorer.staticcontainers.StaticStreamablesActivity;
import com.graffitab.ui.activities.home.users.ProfileActivity;
import com.graffitab.ui.activities.home.users.staticcontainers.StaticUsersActivity;
import com.graffitab.ui.adapters.followersactivity.GenericFollowersActivityRecyclerViewAdapter;
import com.graffitab.ui.adapters.followersactivity.OnFollowerActivityClickListener;
import com.graffitab.ui.fragments.GenericItemListFragment;
import com.graffitab.ui.views.recyclerview.AdvancedEndlessRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.AdvancedRecyclerViewItemDecoration;
import com.graffitab.ui.views.recyclerview.AdvancedRecyclerViewLayoutConfiguration;
import com.graffitabsdk.model.GTStreamable;
import com.graffitabsdk.model.GTUser;
import com.graffitabsdk.model.activity.GTActivity;
import com.graffitabsdk.model.activity.GTActivityContainer;

import java.util.ArrayList;

/**
 * Created by david on 16/02/2017.
 */

public abstract class GenericFollowersActivityFragment extends GenericItemListFragment<GTActivityContainer> implements OnFollowerActivityClickListener {

    @Override
    public int emptyViewImageResource() {
        return -1;
    }

    @Override
    public String emptyViewTitle() {
        return getString(R.string.followers_activity_empty_title);
    }

    @Override
    public String emptyViewSubtitle() {
        return getString(R.string.followers_activity_empty_subtitle);
    }

    @Override
    public void onRowSelected(GTActivityContainer activityContainer, int adapterPosition) {
        switch (activityContainer.type) {
            case LIKE:
                if (activityContainer.isSingle()) {
                    openSingleStreamableActivity(getSingleActivityItem(activityContainer).likedStreamable, adapterPosition);
                } else {
                    StaticStreamablesActivity.open(getActivity(), getStaticElementsFromActivityContainer(activityContainer, new GTStaticElementExtractor<GTStreamable>() {
                        @Override
                        public GTStreamable extract(GTActivity innerActivity) {
                            return innerActivity.likedStreamable;
                        }
                    }));
                }
                break;
            case COMMENT:
                if (activityContainer.isSingle()) {
                    openSingleStreamableActivity(getSingleActivityItem(activityContainer).commentedStreamable, adapterPosition);
                } else {
                    // open list of streamables in new activity with static streamables
                    StaticStreamablesActivity.open(getActivity(), getStaticElementsFromActivityContainer(activityContainer, new GTStaticElementExtractor<GTStreamable>() {
                        @Override
                        public GTStreamable extract(GTActivity innerActivity) {
                            return innerActivity.commentedStreamable;
                        }
                    }));
                }
                break;
            case CREATE_STREAMABLE:
                if (activityContainer.isSingle()) {
                    openSingleStreamableActivity(getSingleActivityItem(activityContainer).createdStreamable, adapterPosition);
                } else {
                    // open list of streamables in new activity with static streamables
                    StaticStreamablesActivity.open(getActivity(), getStaticElementsFromActivityContainer(activityContainer, new GTStaticElementExtractor<GTStreamable>() {
                        @Override
                        public GTStreamable extract(GTActivity innerActivity) {
                            return innerActivity.createdStreamable;
                        }
                    }));
                }
                break;
            case FOLLOW:
                if (activityContainer.isSingle()) {
                    openSingleUserActivity(getSingleActivityItem(activityContainer).followed);
                } else {
                    // open list of users in new activity
                    StaticUsersActivity.open(getActivity(), getStaticElementsFromActivityContainer(activityContainer, new GTStaticElementExtractor<GTUser>() {
                        @Override
                        public GTUser extract(GTActivity innerActivity) {
                            return innerActivity.followed;
                        }
                    }));
                }
                break;
            default:
                throw new RuntimeException("Type not recognized -- " + activityContainer.type);
        }
    }

    // Find streamable view to make it fullScreen
    private View findStreamableViewSourceForAdapterPosition(int adapterPosition) {
        View source = null;
        View v = getRecyclerView().getRecyclerView().getLayoutManager().findViewByPosition(adapterPosition);
        if (v != null)
            source = v.findViewById(R.id.itemImage);
        return source;
    }

    private <T> ArrayList<T> getStaticElementsFromActivityContainer(GTActivityContainer item, GTStaticElementExtractor<T> extractor) {
        ArrayList<T> streamables = new ArrayList<>();
        for (GTActivity activity: item.activities) {
            streamables.add(extractor.extract(activity));
        }
        return streamables;
    }

    private GTActivity getSingleActivityItem(GTActivityContainer activityContainer) {
        return activityContainer.activities.get(0);
    }

    private void openSingleStreamableActivity(GTStreamable streamable, int adapterPosition) {
        StreamableDetailsActivity.openStreamableDetails(getActivity(), streamable, findStreamableViewSourceForAdapterPosition(adapterPosition));
    }

    private void openSingleUserActivity(GTUser user) {
        ProfileActivity.show(getActivity(), user);
    }

    // Configuration

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new AdvancedRecyclerViewItemDecoration(1, 0);
    }

    @Override
    public AdvancedEndlessRecyclerViewAdapter getAdapterForViewType() {
        GenericFollowersActivityRecyclerViewAdapter customAdapter = new GenericFollowersActivityRecyclerViewAdapter(MyApplication.getInstance(), items, getRecyclerView().getRecyclerView());
        customAdapter.setClickListener(this);
        return customAdapter;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManagerForViewType() {
        return new LinearLayoutManager(MyApplication.getInstance());
    }

    @Override
    public AdvancedRecyclerViewLayoutConfiguration getLayoutConfiguration() {
        return null;
    }

    private interface GTStaticElementExtractor<T> {
        T extract(GTActivity innerActivity);
    }
}
