package com.graffitab.ui.fragments.streamable;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.graffitab.ui.adapters.streamables.ListStreamablesRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.components.AdvancedEndlessRecyclerViewAdapter;
import com.graffitab.utils.display.DisplayUtils;

/**
 * Created by georgichristov on 15/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class ListStreamablesFragment extends GenericStreamablesFragment {

    @Override
    public void basicInit() {
        setViewType(ViewType.LIST_FULL);
    }

    // Configuration

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new RecyclerView.ItemDecoration() {

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int itemCount = state.getItemCount();
                int position = parent.getChildLayoutPosition(view);

                outRect.right = 0;
                outRect.left = 0;
                outRect.top = DisplayUtils.pxToDip(parent.getContext(), 10);

                if (position == itemCount - 1)
                    outRect.bottom = DisplayUtils.pxToDip(parent.getContext(), 10);
            }
        };
    }

    @Override
    public AdvancedEndlessRecyclerViewAdapter getAdapterForViewType() {
        if (getActivity() == null)
            return null;

        return new ListStreamablesRecyclerViewAdapter(getActivity(), items);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManagerForViewType() {
        return new LinearLayoutManager(getContext());
    }

    @Override
    public void configureLayoutManagers() {}
}
