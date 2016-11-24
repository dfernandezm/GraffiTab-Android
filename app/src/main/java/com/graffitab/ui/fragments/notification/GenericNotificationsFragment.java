package com.graffitab.ui.fragments.notification;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.graffitab.R;
import com.graffitab.application.MyApplication;
import com.graffitab.graffitabsdk.model.GTNotification;
import com.graffitab.ui.adapters.notifications.ListNotificationsRecyclerViewAdapter;
import com.graffitab.ui.fragments.GenericItemListFragment;
import com.graffitab.ui.views.recyclerview.components.AdvancedEndlessRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecyclerViewItemDecoration;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecyclerViewLayoutConfiguration;
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
    private ViewType previousViewType;
    private boolean initialViewTypeSet = false;

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
        if (previousViewType == null || previousViewType != type) {
            this.viewType = type;
            this.previousViewType = this.viewType;

            if (initialViewTypeSet) // Only reset views once initial viewType has been set.
                didChangeViewType();
        }

        initialViewTypeSet = true; // After the first layout pass, we allow changing the view type.
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
        if (viewType == ViewType.LIST_FULL)
            return new ListNotificationsRecyclerViewAdapter(MyApplication.getInstance(), items);
        return null;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManagerForViewType() {
        if (viewType == ViewType.LIST_FULL)
            return new LinearLayoutManager(MyApplication.getInstance());
        return null;
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
