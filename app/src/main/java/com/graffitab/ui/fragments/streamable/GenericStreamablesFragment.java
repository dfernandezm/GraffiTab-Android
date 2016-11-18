package com.graffitab.ui.fragments.streamable;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.graffitab.R;
import com.graffitab.graffitabsdk.model.GTAsset;
import com.graffitab.graffitabsdk.model.GTStreamable;
import com.graffitab.ui.adapters.streamables.GridStreamablesRecyclerViewAdapter;
import com.graffitab.ui.adapters.streamables.ListStreamablesRecyclerViewAdapter;
import com.graffitab.ui.adapters.streamables.SwimlaneStreamablesRecyclerViewAdapter;
import com.graffitab.ui.adapters.streamables.TrendingStreamablesRecyclerViewAdapter;
import com.graffitab.ui.fragments.GenericItemListFragment;
import com.graffitab.ui.views.recyclerview.components.CustomRecyclerViewAdapter;
import com.graffitab.utils.Utils;
import com.graffitab.utils.display.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public abstract class GenericStreamablesFragment extends GenericItemListFragment<GTStreamable> {

    public enum ViewType {GRID, TRENDING, SWIMLANE, LIST_FULL}

    private ViewType viewType;

    public void basicInit() {
        setViewType(ViewType.GRID);
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
            case GRID:
                return new GridStreamablesRecyclerViewAdapter(getActivity(), items);
            case TRENDING:
                return new TrendingStreamablesRecyclerViewAdapter(getActivity(), items);
            case SWIMLANE:
                return new SwimlaneStreamablesRecyclerViewAdapter(getActivity(), items);
            case LIST_FULL:
                return new ListStreamablesRecyclerViewAdapter(getActivity(), items);
        }

        return null;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManagerForViewType() {
        switch (viewType) {
            case GRID:
                return new GridLayoutManager(getContext(), 3);
            case TRENDING:
                return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            case SWIMLANE:
                return new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
            case LIST_FULL:
                return new LinearLayoutManager(getContext());
        }
        return null;
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecorationForViewType() {
        switch (viewType) {
            case GRID: {
                int spanCount = ((GridLayoutManager) advancedRecyclerView.getRecyclerView().getLayoutManager()).getSpanCount();
                return new GridStreamablesRecyclerViewAdapter.RecyclerViewMargin(spanCount);
            }
            case TRENDING: {
                int spanCount = ((StaggeredGridLayoutManager) advancedRecyclerView.getRecyclerView().getLayoutManager()).getSpanCount();
                return new TrendingStreamablesRecyclerViewAdapter.RecyclerViewMargin(spanCount);
            }
            case SWIMLANE: {
                int spanCount = ((StaggeredGridLayoutManager) advancedRecyclerView.getRecyclerView().getLayoutManager()).getSpanCount();
                return new SwimlaneStreamablesRecyclerViewAdapter.RecyclerViewMargin(spanCount);
            }
            case LIST_FULL:
                return new ListStreamablesRecyclerViewAdapter.RecyclerViewMargin(1);
        }
        return null;
    }

    @Override
    public void configureLayoutManagers() {
        switch (viewType) {
            case GRID: {
                ((GridLayoutManager) advancedRecyclerView.getRecyclerView().getLayoutManager()).setSpanCount(DisplayUtils.isLandscape(getContext()) ? 4 : 3);
                break;
            }
            case TRENDING: {
                ((StaggeredGridLayoutManager) advancedRecyclerView.getRecyclerView().getLayoutManager()).setSpanCount(DisplayUtils.isLandscape(getContext()) ? 3 : 2);
                break;
            }
            case SWIMLANE: {
                ((StaggeredGridLayoutManager) advancedRecyclerView.getRecyclerView().getLayoutManager()).setSpanCount(DisplayUtils.isLandscape(getContext()) ? 4 : 3);
                break;
            }
            case LIST_FULL: {
                break;
            }
        }
    }

    // Loading

    @Override
    public List<GTStreamable> generateDummyData() {
        List<GTStreamable> loaded = new ArrayList();
        for (int i = 0; i < 25; i++) {
            GTStreamable streamable = new GTStreamable();
            GTAsset asset = new GTAsset();
            asset.thumbnailWidth = Utils.randInt(300, 400);
            asset.thumbnailHeight = Utils.randInt(500, 1024);
            streamable.asset = asset;
            loaded.add(streamable);
        }
        return loaded;
    }
}
