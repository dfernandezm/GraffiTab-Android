package com.graffitab.ui.fragments.streamable;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.graffitab.ui.adapters.streamables.GridStreamablesRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.components.AdvancedEndlessRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecyclerViewItemDecoration;
import com.graffitab.utils.display.DisplayUtils;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class GridStreamablesFragment extends GenericStreamablesFragment {

    @Override
    public void basicInit() {
        setViewType(ViewType.GRID);
    }

    // Configuration

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new AdvancedRecyclerViewItemDecoration(DisplayUtils.isLandscape(getContext()) ? 4 : 3, 2);
    }

    @Override
    public AdvancedEndlessRecyclerViewAdapter getAdapterForViewType() {
        if (getActivity() == null)
            return null;

        return new GridStreamablesRecyclerViewAdapter(getActivity(), items);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManagerForViewType() {
        return new GridLayoutManager(getContext(), 3);
    }

    @Override
    public void configureLayoutManagers() {
        ((GridLayoutManager) advancedRecyclerView.getRecyclerView().getLayoutManager()).setSpanCount(DisplayUtils.isLandscape(getContext()) ? 4 : 3);
    }
}
