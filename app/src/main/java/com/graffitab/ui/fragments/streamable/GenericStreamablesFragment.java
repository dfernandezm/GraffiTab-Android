package com.graffitab.ui.fragments.streamable;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.graffitabsdk.model.GTAsset;
import com.graffitab.graffitabsdk.model.GTStreamable;
import com.graffitab.ui.adapters.BaseItemRecyclerAdapter;
import com.graffitab.ui.adapters.streamables.GridStreamablesRecyclerAdapter;
import com.graffitab.ui.adapters.streamables.ListStreamablesRecyclerAdapter;
import com.graffitab.ui.adapters.streamables.SwimlaneStreamablesRecyclerAdapter;
import com.graffitab.ui.adapters.streamables.TrendingStreamablesRecyclerAdapter;
import com.graffitab.utils.Utils;
import com.graffitab.utils.display.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public abstract class GenericStreamablesFragment extends Fragment {

    public enum ViewType {GRID, TRENDING, SWIMLANE, LIST_FULL}

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.refreshLayout) SwipeRefreshLayout refreshLayout;

    public List<GTStreamable> items = new ArrayList();
    public boolean isDownloading = false;
    public boolean canLoadMore = true;
    public int offset = 0;

    private ViewType viewType;
    private BaseItemRecyclerAdapter adapter;
    private RecyclerView.ItemDecoration itemDecoration;

    public GenericStreamablesFragment() {
        // No-op.
    }

    public void basicInit() {
        setViewType(ViewType.GRID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_refreshable_recyclerview, container, false);
        ButterKnife.bind(this, view);

        basicInit();

        setupRefreshView();
        setupRecyclerView();

        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        configureLayout();
    }

    public void setViewType(ViewType type) {
        this.viewType = type;
    }

    // Configuration

    private BaseItemRecyclerAdapter getAdapterForViewType() {
        if (getActivity() == null)
            return null;

        switch (viewType) {
            case GRID:
                return new GridStreamablesRecyclerAdapter(getActivity(), items);
            case TRENDING:
                return new TrendingStreamablesRecyclerAdapter(getActivity(), items);
            case SWIMLANE:
                return new SwimlaneStreamablesRecyclerAdapter(getActivity(), items);
            case LIST_FULL:
                return new ListStreamablesRecyclerAdapter(getActivity(), items);
        }

        return null;
    }

    private RecyclerView.LayoutManager getLayoutManagerForViewType() {
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

    private RecyclerView.ItemDecoration getItemDecorationForViewType() {
        switch (viewType) {
            case GRID: {
                int spanCount = ((GridLayoutManager) recyclerView.getLayoutManager()).getSpanCount();
                return new GridStreamablesRecyclerAdapter.RecyclerViewMargin(spanCount);
            }
            case TRENDING: {
                int spanCount = ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).getSpanCount();
                return new TrendingStreamablesRecyclerAdapter.RecyclerViewMargin(spanCount);
            }
            case SWIMLANE: {
                int spanCount = ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).getSpanCount();
                return new SwimlaneStreamablesRecyclerAdapter.RecyclerViewMargin(spanCount);
            }
            case LIST_FULL:
                return new ListStreamablesRecyclerAdapter.RecyclerViewMargin(1);
        }
        return null;
    }

    private void configureLayout() {
        // Replace layout manager.
        RecyclerView.LayoutManager manager = getLayoutManagerForViewType();
        if (recyclerView.getLayoutManager() == null || recyclerView.getLayoutManager().getClass() != manager.getClass()) {
            recyclerView.setLayoutManager(manager);
        }

        // Configure individual layouts.
        switch (viewType) {
            case GRID: {
                ((GridLayoutManager) recyclerView.getLayoutManager()).setSpanCount(DisplayUtils.isLandscape(getContext()) ? 4 : 3);
                break;
            }
            case TRENDING: {
                ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).setSpanCount(DisplayUtils.isLandscape(getContext()) ? 3 : 2);
                break;
            }
            case SWIMLANE: {
                ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).setSpanCount(DisplayUtils.isLandscape(getContext()) ? 4 : 3);
                break;
            }
            case LIST_FULL: {
                break;
            }
        }

        // Replace item decoration.
        if (itemDecoration != null)
            recyclerView.removeItemDecoration(itemDecoration);
        itemDecoration = getItemDecorationForViewType();
        recyclerView.addItemDecoration(itemDecoration);

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    // Loading

//    public abstract void loadItems(boolean isStart, int offset, GTNetworkResponseHandler handler);

    public void reload() {
        refresh();
    }

    private void refresh() {
        offset = 0;
        canLoadMore = true;

        loadItems(false, offset);
    }

    private void loadItems(boolean isStart, final int o) {
        if (getActivity() == null)
            return;

        if (items.size() <= 0 && !isDownloading) {
            if (isStart)
                refreshLayout.setRefreshing(true);
        }

        isDownloading = true;

        // TODO: Remove
        Utils.runWithDelay(new Runnable() {
            @Override
            public void run() {
                // TODO: This is just a dummy procedure to generate some random content.
                List<GTStreamable> loaded = new ArrayList();
                for (int i = 0; i < 25; i++) {
                    GTStreamable streamable = new GTStreamable();
                    GTAsset asset = new GTAsset();
                    asset.thumbnailWidth = Utils.randInt(300, 400);
                    asset.thumbnailHeight = Utils.randInt(500, 1024);
                    streamable.asset = asset;
                    loaded.add(streamable);
                }

                // Clear items if we are pulling to refresh.
                if (o == 0)
                    items.clear();

                // Merge newly loaded items.
                items.addAll(loaded);

                // Configure load more layout.
                if (loaded.size() <= 0 || loaded.size() < Constants.MAX_ITEMS) {
                    canLoadMore = false;
                    adapter.setProgressMore(false);
                }
                else
                    adapter.setProgressMore(true);

                // Finalize and refresh UI.
                finalizeLoad();
            }
        }, 3000);
    }

    private void finalizeCacheLoad() {
        adapter.finishLoadingMore();
        adapter.notifyDataSetChanged();
    }

    private void finalizeLoad() {
        refreshLayout.setRefreshing(false);
        isDownloading = false;

        adapter.finishLoadingMore();
        adapter.notifyDataSetChanged();
    }

    // Setup

    private void setupRefreshView() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                refresh();
            }
        });

        refreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorSecondary);
    }

    private void setupRecyclerView() {
        configureLayout();

        // We must set the adapter after we set the footer view, otherwise the footer will not show.
        recyclerView.post(new Runnable() {

            @Override
            public void run() {
                // Setup adapter.
                adapter = getAdapterForViewType();
                recyclerView.setAdapter(adapter);

                // Setup endless scroller.
                adapter.addOnLoadMoreListener(new BaseItemRecyclerAdapter.OnLoadMoreListener() {

                    @Override
                    public void onLoadMore() {
                        if (canLoadMore && !isDownloading) {
                            offset += Constants.MAX_ITEMS;
                            loadItems(false, offset);
                        }
                        else {
                            isDownloading = false;
                            adapter.setProgressMore(false);
                        }
                    }
                }, recyclerView);

                loadItems(true, offset);
            }
        });
    }
}
