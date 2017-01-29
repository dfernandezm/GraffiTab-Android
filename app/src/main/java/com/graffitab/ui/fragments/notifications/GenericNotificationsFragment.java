package com.graffitab.ui.fragments.notifications;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.graffitab.R;
import com.graffitab.application.MyApplication;
import com.graffitab.constants.Constants;
import com.graffitab.ui.activities.home.streamables.StreamableDetailsActivity;
import com.graffitab.ui.activities.home.users.ProfileActivity;
import com.graffitab.ui.adapters.notifications.GenericNotificationsRecyclerViewAdapter;
import com.graffitab.ui.adapters.notifications.OnNotificationClickListener;
import com.graffitab.ui.fragments.GenericItemListFragment;
import com.graffitab.ui.views.recyclerview.components.AdvancedEndlessRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecyclerViewItemDecoration;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecyclerViewLayoutConfiguration;
import com.graffitabsdk.model.GTNotification;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public abstract class GenericNotificationsFragment extends GenericItemListFragment<GTNotification> implements OnNotificationClickListener {

    @Override
    public int emptyViewImageResource() {
        return -1;
    }

    @Override
    public String emptyViewTitle() {
        return getString(R.string.notifications_empty_title);
    }

    @Override
    public String emptyViewSubtitle() {
        return getString(R.string.notifications_empty_description);
    }

    @Override
    public void onRowSelected(GTNotification notification, int adapterPosition) {
        if (notification.type == GTNotification.GTNotificationType.FOLLOW)
            ProfileActivity.show(notification.follower, getActivity());
        else if (notification.type == GTNotification.GTNotificationType.COMMENT) {
            Intent intent = new Intent(getActivity(), StreamableDetailsActivity.class);
            intent.putExtra(Constants.EXTRA_STREAMABLE, notification.commentedStreamable);
            startActivity(intent);
        }
        else if (notification.type == GTNotification.GTNotificationType.LIKE) {
            Intent intent = new Intent(getActivity(), StreamableDetailsActivity.class);
            intent.putExtra(Constants.EXTRA_STREAMABLE, notification.likedStreamable);
            startActivity(intent);
        }
        else if (notification.type == GTNotification.GTNotificationType.MENTION) {
            Intent intent = new Intent(getActivity(), StreamableDetailsActivity.class);
            intent.putExtra(Constants.EXTRA_STREAMABLE, notification.mentionedStreamable);
            startActivity(intent);
        }
        else {
            // No-op - Welcome notification type is not handled.
        }
    }

    // Configuration

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new AdvancedRecyclerViewItemDecoration(1, 0);
    }

    @Override
    public AdvancedEndlessRecyclerViewAdapter getAdapterForViewType() {
        GenericNotificationsRecyclerViewAdapter customAdapter = new GenericNotificationsRecyclerViewAdapter(MyApplication.getInstance(), items, getRecyclerView().getRecyclerView());
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
