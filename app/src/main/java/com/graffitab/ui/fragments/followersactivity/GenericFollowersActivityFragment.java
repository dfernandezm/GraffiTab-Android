package com.graffitab.ui.fragments.followersactivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.graffitab.R;
import com.graffitab.application.MyApplication;
import com.graffitab.ui.adapters.followersactivity.GenericFollowersActivityRecyclerViewAdapter;
import com.graffitab.ui.adapters.followersactivity.OnFollowerActivityClickListener;
import com.graffitab.ui.fragments.GenericItemListFragment;
import com.graffitab.ui.views.recyclerview.AdvancedEndlessRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.AdvancedRecyclerViewItemDecoration;
import com.graffitab.ui.views.recyclerview.AdvancedRecyclerViewLayoutConfiguration;
import com.graffitabsdk.model.activity.GTActivityContainer;

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

    //TODO: fill which each different kind of event
    @Override
    public void onRowSelected(GTActivityContainer activityContainer, int adapterPosition) {
        //TODO
//        if (activityContainer.type == GTActivity.GTActivityType.FOLLOW) {
//            //ProfileActivity.show(getActivity(), activityContainer.follower);
//        } else if (activityContainer.type == GTActivity.GTActivityType.COMMENT) {
//            //StreamableDetailsActivity.openStreamableDetails(getActivity(), activityContainer.commentedStreamable, findStreamableViewSourceForAdapterPosition(adapterPosition));
//        }
//        else if (activityContainer.type == GTActivity.GTActivityType.LIKE) {
//            //StreamableDetailsActivity.openStreamableDetails(getActivity(), activityContainer.likedStreamable, findStreamableViewSourceForAdapterPosition(adapterPosition));
//        }
//        else if (activityContainer.type == GTActivity.GTActivityType.CREATE_STREAMABLE) {
//            //StreamableDetailsActivity.openStreamableDetails(getActivity(), activityContainer.mentionedStreamable, findStreamableViewSourceForAdapterPosition(adapterPosition));
//        }
//        else {
//            // No-op - Welcome notification type is not handled.
//        }
    }

    // Find streamable view to make it fullScreen
    private View findStreamableViewSourceForAdapterPosition(int adapterPosition) {
        View source = null;
        View v = getRecyclerView().getRecyclerView().getLayoutManager().findViewByPosition(adapterPosition);
        if (v != null)
            source = v.findViewById(R.id.itemImage);
        return source;
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

}
