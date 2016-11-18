package com.graffitab.ui.fragments.streamable;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.graffitab.ui.adapters.streamables.GridStreamablesRecyclerViewAdapter;
import com.graffitab.ui.adapters.streamables.ListStreamablesRecyclerViewAdapter;
import com.graffitab.ui.adapters.streamables.SwimlaneStreamablesRecyclerViewAdapter;
import com.graffitab.ui.adapters.streamables.TrendingStreamablesRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.AdvancedRecyclerView;
import com.graffitab.ui.views.recyclerview.components.CustomRecyclerViewAdapter;
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
public abstract class GenericStreamablesFragment extends Fragment implements AdvancedRecyclerView.OnEmptyViewListener {

    public enum ViewType {GRID, TRENDING, SWIMLANE, LIST_FULL}

    @BindView(R.id.advancedRecyclerView) AdvancedRecyclerView advancedRecyclerView;

    public List<GTStreamable> items = new ArrayList();
    public boolean isDownloading = false;
    public boolean canLoadMore = true;
    public int offset = 0;

    private ViewType viewType;
    private CustomRecyclerViewAdapter adapter;
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

        setupRecyclerView();

        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        configureLayout();
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

    private CustomRecyclerViewAdapter getAdapterForViewType() {
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

    private void configureLayout() {
        // Replace layout manager.
        RecyclerView.LayoutManager manager = getLayoutManagerForViewType();
        if (advancedRecyclerView.getRecyclerView().getLayoutManager() == null || advancedRecyclerView.getRecyclerView().getLayoutManager().getClass() != manager.getClass()) {
            advancedRecyclerView.getRecyclerView().setLayoutManager(manager);
        }

        // Configure individual layouts.
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

        // Replace item decoration.
        if (itemDecoration != null)
            advancedRecyclerView.getRecyclerView().removeItemDecoration(itemDecoration);
        itemDecoration = getItemDecorationForViewType();
        advancedRecyclerView.getRecyclerView().addItemDecoration(itemDecoration);

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
                advancedRecyclerView.beginRefreshing();
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
        isDownloading = false;

        adapter.finishLoadingMore();
        adapter.notifyDataSetChanged();

        advancedRecyclerView.endRefreshing();
        advancedRecyclerView.addOnEmptyViewListsner(this);
    }

    // Setup

    private void setupRecyclerView() {
        configureLayout();

        advancedRecyclerView.setRefreshColorScheme(R.color.colorPrimary, R.color.colorSecondary);
        advancedRecyclerView.addOnRefreshListener(new AdvancedRecyclerView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                refresh();
            }
        });

        advancedRecyclerView.post(new Runnable() {

            @Override
            public void run() {
                // Setup adapter.
                adapter = getAdapterForViewType();
                advancedRecyclerView.setAdapter(adapter);

                // Setup endless scroller.
                advancedRecyclerView.addOnLoadMoreListener(new AdvancedRecyclerView.OnLoadMoreListener() {

                    @Override
                    public void onLoadMore() {
                        System.out.println("LOADING MORE");
                        if (canLoadMore && !isDownloading) {
                            offset += Constants.MAX_ITEMS;
                            loadItems(false, offset);
                        }
                        else {
                            isDownloading = false;
                            adapter.setProgressMore(false);
                        }
                    }
                });

                loadItems(true, offset);
            }
        });
    }
}
