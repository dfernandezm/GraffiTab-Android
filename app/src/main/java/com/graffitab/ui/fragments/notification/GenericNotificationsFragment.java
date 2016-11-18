package com.graffitab.ui.fragments.notification;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.graffitab.R;
import com.graffitab.graffitabsdk.model.GTNotification;
import com.graffitab.ui.adapters.notifications.ListNotificationsRecyclerViewAdapter;
import com.graffitab.ui.fragments.GenericItemListFragment;
import com.graffitab.ui.views.recyclerview.components.CustomRecyclerViewAdapter;
import com.graffitab.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public abstract class GenericNotificationsFragment extends GenericItemListFragment<GTNotification> {

    public enum ViewType {LIST_FULL}

    private ViewType viewType;

    public void basicInit() {
        setViewType(ViewType.LIST_FULL);
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

    public void setViewType(ViewType type) {
        this.viewType = type;
    }

    // Configuration

    @Override
    public CustomRecyclerViewAdapter getAdapterForViewType() {
        if (getActivity() == null)
            return null;

        switch (viewType) {
            case LIST_FULL:
                return new ListNotificationsRecyclerViewAdapter(getActivity(), items);
        }

        return null;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManagerForViewType() {
        switch (viewType) {
            case LIST_FULL:
                return new LinearLayoutManager(getContext());
        }
        return null;
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecorationForViewType() {
        switch (viewType) {
            case LIST_FULL:
                return new ListNotificationsRecyclerViewAdapter.RecyclerViewMargin(1);
        }
        return null;
    }

    @Override
    public void configureLayoutManagers() {
        switch (viewType) {
            case LIST_FULL: {
                break;
            }
        }
    }

    // Loading

    @Override
    public List<GTNotification> generateDummyData() {
        List<GTNotification> loaded = new ArrayList();
        for (int i = 0; i < 25; i++) {
            GTNotification notification = new GTNotification();
            notification.type = GTNotification.GTNotificationType.values()[Utils.randInt(0, GTNotification.GTNotificationType.values().length - 1)];
            notification.isRead = i > 2;
            loaded.add(notification);
        }
        return loaded;
    }
}
