package com.graffitab.ui.fragments.user;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.graffitab.ui.adapters.users.ListUsersRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.components.AdvancedEndlessRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecyclerViewItemDecoration;

/**
 * Created by georgichristov on 15/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class ListUsersFragment extends GenericUsersFragment {

    @Override
    public void basicInit() {
        setViewType(ViewType.LIST_FULL);
    }

    // Configuration

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new AdvancedRecyclerViewItemDecoration(1, 0);
    }

    @Override
    public AdvancedEndlessRecyclerViewAdapter getAdapterForViewType() {
        if (getActivity() == null)
            return null;

        return new ListUsersRecyclerViewAdapter(getActivity(), items);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManagerForViewType() {
        return new LinearLayoutManager(getContext());
    }

    @Override
    public void configureLayoutManagers() {}
}
