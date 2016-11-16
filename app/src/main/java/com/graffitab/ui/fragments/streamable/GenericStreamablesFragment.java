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
import com.graffitab.ui.adapters.BaseItemRecyclerAdapter;
import com.graffitab.ui.adapters.streamables.GridStreamablesRecyclerAdapter;
import com.graffitab.ui.adapters.streamables.ListStreamablesRecyclerAdapter;
import com.graffitab.ui.adapters.streamables.SwimlaneStreamablesRecyclerAdapter;
import com.graffitab.ui.adapters.streamables.TrendingStreamablesRecyclerAdapter;
import com.graffitab.utils.Utils;
import com.graffitab.utils.display.DisplayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.graffitab.R.id.recyclerView;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public abstract class GenericStreamablesFragment extends Fragment {

    public enum ViewType {GRID, TRENDING, SWIMLANE, LIST_FULL}

    @BindView(recyclerView) RecyclerView gridView;
    @BindView(R.id.refreshLayout) SwipeRefreshLayout refreshLayout;

    public List<GTStreamble> items = new ArrayList();
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
        View view = inflater.inflate(R.layout.fragment_list_streamable, container, false);
        ButterKnife.bind(this, view);

        basicInit();

        setupRefreshView();
        setupGridView();

        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        configureLayout();
    }

//    @Override
//    public void onGridRefresh(int pageNumber) {
//        if (canLoadMore && !isDownloading) {
//            offset += Constants.MAX_ITEMS;
//
//            loadItems(false, offset);
//        }
//        else {
//            isDownloading = false;
//
////            gridListener.noMorePages();
//        }
//    }

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
                int spanCount = ((GridLayoutManager) gridView.getLayoutManager()).getSpanCount();
                return new GridStreamablesRecyclerAdapter.RecyclerViewMargin(spanCount);
            }
            case TRENDING: {
                int spanCount = ((StaggeredGridLayoutManager) gridView.getLayoutManager()).getSpanCount();
                return new TrendingStreamablesRecyclerAdapter.RecyclerViewMargin(spanCount);
            }
            case SWIMLANE: {
                int spanCount = ((StaggeredGridLayoutManager) gridView.getLayoutManager()).getSpanCount();
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
        if (gridView.getLayoutManager() == null || gridView.getLayoutManager().getClass() != manager.getClass()) {
            gridView.setLayoutManager(manager);
        }

        // Configure individual layouts.
        switch (viewType) {
            case GRID: {
                ((GridLayoutManager) gridView.getLayoutManager()).setSpanCount(DisplayUtils.isLandscape(getContext()) ? 4 : 3);
                break;
            }
            case TRENDING: {
                ((StaggeredGridLayoutManager) gridView.getLayoutManager()).setSpanCount(DisplayUtils.isLandscape(getContext()) ? 3 : 2);
                break;
            }
            case SWIMLANE: {
                ((StaggeredGridLayoutManager) gridView.getLayoutManager()).setSpanCount(DisplayUtils.isLandscape(getContext()) ? 4 : 3);
                break;
            }
            case LIST_FULL: {
                break;
            }
        }

        // Replace item decoration.
        if (itemDecoration != null)
            gridView.removeItemDecoration(itemDecoration);
        itemDecoration = getItemDecorationForViewType();
        gridView.addItemDecoration(itemDecoration);

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
                if (o == 0)
                    items.clear();

                List<GTStreamble> loaded = new ArrayList();
                for (int i = 0; i < 25; i++)
                    loaded.add(new GTStreamble(GTStreamble.randInt(300, 400), GTStreamble.randInt(500, 1024)));
                items.addAll(loaded);

                if (loaded.size() <= 0 || loaded.size() < Constants.MAX_ITEMS) {
                    canLoadMore = false;
//                    gridListener.noMorePages();
                }
//                else
//                    gridListener.notifyMorePages();

                finalizeLoad();
            }
        }, 3000);

//        loadItems(isStart, o, new GTNetworkResponseHandler() {
//
//            @Override
//            public void onSuccess(GTResponseObject response) {
//                if (o == 0) {
//                    items.clear();
//                    gridListener.startUpdates();
//                }
//
//                ArrayList<GTStreamable> loaded = (ArrayList<GTStreamable>) response.object;
//
//                items.addAll(loaded);
//
//                if (loaded.size() <= 0 || loaded.size() < Constants.MAX_ITEMS) {
//                    canLoadMore = false;
//                    gridListener.noMorePages();
//                }
//                else
//                    gridListener.notifyMorePages();
//
//                finalizeLoad();
//            }
//
//            @Override
//            public void onError(GTResponseObject response) {
//                if (getActivity() == null)
//                    return;
//
//                canLoadMore = false;
//                gridListener.noMorePages();
//
//                finalizeLoad();
//
//                if (response.reason == Reason.AUTHORIZATION_NEEDED)
//                    Utils.logoutUser(getActivity());
//                else
//                    DialogBuilder.buildOKDialog(getActivity(), getString(R.string.app_name), response.message);
//            }
//
//            @Override
//            public void onCache(GTResponseObject response) {
//                if (getActivity() == null)
//                    return;
//
//                items.clear();
//                items.addAll((Collection<? extends GTStreamable>) response.object);
//
//                getActivity().runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        finalizeCacheLoad();
//                    }
//                });
//            }
//        });
    }

    private void finalizeCacheLoad() {
        adapter.notifyDataSetChanged();
    }

    private void finalizeLoad() {
        refreshLayout.setRefreshing(false);

        isDownloading = false;

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

    private void setupGridView() {
        configureLayout();

        // We must set the adapter after we set the footer view, otherwise the footer will not show.
        gridView.post(new Runnable() {

            @Override
            public void run() {
                adapter = getAdapterForViewType();
                gridView.setAdapter(adapter);

                loadItems(true, offset);
            }
        });
    }

    public static class GTStreamble {
        public int width;
        public int height;

        public GTStreamble(int width, int height) {
            this.width = width;
            this.height = height;
        }

        /**
         * Returns a pseudo-random number between min and max, inclusive.
         * The difference between min and max can be at most
         * <code>Integer.MAX_VALUE - 1</code>.
         *
         * @param min Minimum value
         * @param max Maximum value.  Must be greater than min.
         * @return Integer between min and max, inclusive.
         * @see java.util.Random#nextInt(int)
         */
        public static int randInt(int min, int max) {

            // NOTE: This will (intentionally) not run as written so that folks
            // copy-pasting have to think about how to initialize their
            // Random instance.  Initialization of the Random instance is outside
            // the main scope of the question, but some decent options are to have
            // a field that is initialized once and then re-used as needed or to
            // use ThreadLocalRandom (if using at least Java 1.7).
            Random rand = new Random();

            // nextInt is normally exclusive of the top value,
            // so add 1 to make it inclusive
            int randomNum = rand.nextInt((max - min) + 1) + min;

            return randomNum;
        }
    }
}
