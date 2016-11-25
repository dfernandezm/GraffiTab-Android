package com.graffitab.ui.fragments.streamable;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.graffitab.R;
import com.graffitab.application.MyApplication;
import com.graffitab.graffitabsdk.model.GTAsset;
import com.graffitab.graffitabsdk.model.GTStreamable;
import com.graffitab.ui.adapters.streamables.GridStreamablesRecyclerViewAdapter;
import com.graffitab.ui.adapters.streamables.ListStreamablesRecyclerViewAdapter;
import com.graffitab.ui.adapters.streamables.SwimlaneStreamablesRecyclerViewAdapter;
import com.graffitab.ui.adapters.streamables.TrendingStreamablesRecyclerViewAdapter;
import com.graffitab.ui.fragments.GenericItemListFragment;
import com.graffitab.ui.views.recyclerview.components.AdvancedEndlessRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecyclerViewItemDecoration;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecyclerViewLayoutConfiguration;
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
    private ViewType previousViewType;
    private boolean initialViewTypeSet = false;

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

    public ViewType getViewType() {
        return viewType;
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
        if (viewType == ViewType.GRID)
            return new AdvancedRecyclerViewItemDecoration(DisplayUtils.isLandscape(MyApplication.getInstance()) ? 4 : 3, 2);
        else if (viewType == ViewType.LIST_FULL) {
            final int spacing = 10;

            return new AdvancedRecyclerViewItemDecoration(1, spacing) {

                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    int itemCount = state.getItemCount();
                    int position = parent.getChildLayoutPosition(view);

                    outRect.right = 0;
                    outRect.left = 0;
                    outRect.top = DisplayUtils.pxToDip(MyApplication.getInstance(), spacing);

                    if (position == itemCount - 1)
                        outRect.bottom = DisplayUtils.pxToDip(MyApplication.getInstance(), spacing);
                }
            };
        }
        else if (viewType == ViewType.SWIMLANE)
            return new AdvancedRecyclerViewItemDecoration(DisplayUtils.isLandscape(MyApplication.getInstance()) ? 4 : 3, 5);
        else if (viewType == ViewType.TRENDING)
            return new AdvancedRecyclerViewItemDecoration(DisplayUtils.isLandscape(MyApplication.getInstance()) ? 3 : 2, 15);
        return null;
    }

    @Override
    public AdvancedEndlessRecyclerViewAdapter getAdapterForViewType() {
        if (viewType == ViewType.GRID)
            return new GridStreamablesRecyclerViewAdapter(MyApplication.getInstance(), items);
        else if (viewType == ViewType.LIST_FULL)
            return new ListStreamablesRecyclerViewAdapter(MyApplication.getInstance(), items);
        else if (viewType == ViewType.SWIMLANE)
            return new SwimlaneStreamablesRecyclerViewAdapter(MyApplication.getInstance(), items);
        else if (viewType == ViewType.TRENDING)
            return new TrendingStreamablesRecyclerViewAdapter(MyApplication.getInstance(), items);
        return null;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManagerForViewType() {
        if (viewType == ViewType.GRID)
            return new GridLayoutManager(MyApplication.getInstance(), 3);
        else if (viewType == ViewType.LIST_FULL)
            return new LinearLayoutManager(MyApplication.getInstance());
        else if (viewType == ViewType.SWIMLANE)
            return new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        else if (viewType == ViewType.TRENDING)
            return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        return null;
    }

    @Override
    public AdvancedRecyclerViewLayoutConfiguration getLayoutConfiguration() {
        if (viewType == ViewType.GRID)
            return new AdvancedRecyclerViewLayoutConfiguration(DisplayUtils.isLandscape(MyApplication.getInstance()) ? 4 : 3);
        else if (viewType == ViewType.LIST_FULL)
            return null;
        else if (viewType == ViewType.SWIMLANE)
            return new AdvancedRecyclerViewLayoutConfiguration(DisplayUtils.isLandscape(MyApplication.getInstance()) ? 4 : 3);
        else if (viewType == ViewType.TRENDING)
            return new AdvancedRecyclerViewLayoutConfiguration(DisplayUtils.isLandscape(MyApplication.getInstance()) ? 3 : 2);
        return null;
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
