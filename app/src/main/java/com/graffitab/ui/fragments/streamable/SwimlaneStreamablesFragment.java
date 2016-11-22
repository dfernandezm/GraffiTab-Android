package com.graffitab.ui.fragments.streamable;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.graffitab.ui.adapters.streamables.SwimlaneStreamablesRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.components.AdvancedEndlessRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecyclerViewItemDecoration;
import com.graffitab.utils.display.DisplayUtils;

/**
 * Created by georgichristov on 16/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class SwimlaneStreamablesFragment extends GenericStreamablesFragment {

    @Override
    public void basicInit() {
        setViewType(ViewType.SWIMLANE);
    }

    // Configuration

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new AdvancedRecyclerViewItemDecoration(DisplayUtils.isLandscape(getContext()) ? 4 : 3, 5);
    }

    @Override
    public AdvancedEndlessRecyclerViewAdapter getAdapterForViewType() {
        if (getActivity() == null)
            return null;

        return new SwimlaneStreamablesRecyclerViewAdapter(getActivity(), items);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManagerForViewType() {
        return new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    public void configureLayoutManagers() {}
}
