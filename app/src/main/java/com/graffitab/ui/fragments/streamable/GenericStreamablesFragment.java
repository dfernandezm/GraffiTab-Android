package com.graffitab.ui.fragments.streamable;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.ui.adapters.BaseItemAdapter;
import com.graffitab.ui.adapters.streamables.GridStreamablesAdapter;
import com.graffitab.ui.adapters.streamables.ListStreamablesAdapter;
import com.graffitab.ui.listeners.EndlessGridScrollListener;
import com.graffitab.utils.Utils;
import com.graffitab.utils.display.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.recruit_mp.android.headerfootergridview.HeaderFooterGridView;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public abstract class GenericStreamablesFragment extends Fragment implements EndlessGridScrollListener.GridScrollListener {

    public enum ViewType {GRID/*, TRENDING, SWIMLANE*/, LIST_FULL}

    @BindView(R.id.gridView) HeaderFooterGridView gridView;
    @BindView(R.id.refreshLayout) SwipeRefreshLayout refreshLayout;

    public List<Integer> items = new ArrayList();
    public boolean isDownloading = false;
    public boolean canLoadMore = true;
    public int offset = 0;

    private EndlessGridScrollListener gridListener;
    private ViewType viewType;
    private BaseItemAdapter adapter;

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

        configureLayout();

        setupRefreshView();
        setupGridView();

        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        configureLayout();
    }

    @Override
    public void onGridRefresh(int pageNumber) {
        if (canLoadMore && !isDownloading) {
            offset += Constants.MAX_ITEMS;

            loadItems(false, offset);
        }
        else {
            isDownloading = false;

            gridListener.noMorePages();
        }
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // No-op.
    }

    public void setViewType(ViewType type) {
        this.viewType = type;
    }

    private BaseItemAdapter getAdapterForViewType() {
        if (getActivity() == null)
            return null;

        switch (viewType) {
            case GRID:
                return new GridStreamablesAdapter(getActivity(), gridView, items);
            case LIST_FULL:
                return new ListStreamablesAdapter(getActivity(), gridView, items);
        }

        return null;
    }

    // Configuration

    private void configureLayout() {
        switch (viewType) {
            case GRID:
                gridView.setNumColumns(DisplayUtils.isLandscape(getContext()) ? 4 : 3);
                break;
            case LIST_FULL:
                gridView.setNumColumns(1);
                break;
        }

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
                if (o == 0) {
                    items.clear();
                    gridListener.startUpdates();
                }

                List<Integer> loaded = new ArrayList<Integer>();
                for (int i = 0; i < 25; i++)
                    loaded.add(i);
                items.addAll(loaded);

                if (loaded.size() <= 0 || loaded.size() < Constants.MAX_ITEMS) {
                    canLoadMore = false;
                    gridListener.noMorePages();
                }
                else
                    gridListener.notifyMorePages();

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
        adapter.setItems(items);
        adapter.notifyDataSetChanged();
    }

    private void finalizeLoad() {
        refreshLayout.setRefreshing(false);

        isDownloading = false;

        adapter.setItems(items);
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
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > adapter.getCount() - 1)
                    return;


            }
        });

        gridListener = new EndlessGridScrollListener(gridView, this, false);
        gridView.setOnScrollListener(gridListener);

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
}
