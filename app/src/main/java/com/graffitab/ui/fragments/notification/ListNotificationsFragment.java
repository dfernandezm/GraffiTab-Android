package com.graffitab.ui.fragments.notification;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.graffitab.application.MyApplication;
import com.graffitab.ui.adapters.notifications.ListNotificationsRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.components.AdvancedEndlessRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecyclerViewItemDecoration;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecyclerViewLayoutConfiguration;

/**
 * Created by georgichristov on 17/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class ListNotificationsFragment extends GenericNotificationsFragment {

    @Override
    public void basicInit() {
        setViewType(ViewType.LIST_FULL);
    }
}
