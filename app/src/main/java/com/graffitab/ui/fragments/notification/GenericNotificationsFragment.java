package com.graffitab.ui.fragments.notification;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.graffitab.R;
import com.graffitab.application.MyApplication;
import com.graffitab.ui.adapters.notifications.GenericNotificationsRecyclerViewAdapter;
import com.graffitab.ui.fragments.GenericItemListFragment;
import com.graffitab.ui.views.recyclerview.components.AdvancedEndlessRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecyclerViewItemDecoration;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecyclerViewLayoutConfiguration;
import com.graffitab.utils.Utils;
import com.graffitabsdk.model.GTNotification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public abstract class GenericNotificationsFragment extends GenericItemListFragment<GTNotification> {

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

    // Configuration

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new AdvancedRecyclerViewItemDecoration(1, 0);
    }

    @Override
    public AdvancedEndlessRecyclerViewAdapter getAdapterForViewType() {
        return new GenericNotificationsRecyclerViewAdapter(MyApplication.getInstance(), items, getRecyclerView().getRecyclerView());
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManagerForViewType() {
        return new LinearLayoutManager(MyApplication.getInstance());
    }

    @Override
    public AdvancedRecyclerViewLayoutConfiguration getLayoutConfiguration() {
        return null;
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
