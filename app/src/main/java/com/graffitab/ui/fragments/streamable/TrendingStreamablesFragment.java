package com.graffitab.ui.fragments.streamable;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.graffitab.ui.adapters.streamables.TrendingStreamablesRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.components.AdvancedEndlessRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecyclerViewItemDecoration;
import com.graffitab.utils.display.DisplayUtils;

/**
 * Created by georgichristov on 15/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class TrendingStreamablesFragment extends GenericStreamablesFragment {

    @Override
    public void basicInit() {
        setViewType(ViewType.TRENDING);
    }

    // Configuration

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new AdvancedRecyclerViewItemDecoration(DisplayUtils.isLandscape(getContext()) ? 3 : 2, 15);
    }

    @Override
    public AdvancedEndlessRecyclerViewAdapter getAdapterForViewType() {
        if (getActivity() == null)
            return null;

        return new TrendingStreamablesRecyclerViewAdapter(getActivity(), items);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManagerForViewType() {
        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    public void configureLayoutManagers() {}
}
