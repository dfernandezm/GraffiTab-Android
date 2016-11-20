package com.graffitab.ui.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.ui.views.recyclerview.AdvancedRecyclerView;
import com.graffitab.ui.views.recyclerview.components.CustomRecyclerViewAdapter;
import com.graffitab.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public abstract class GenericItemListFragment<T> extends Fragment implements AdvancedRecyclerView.OnEmptyViewListener {

    @BindView(R.id.advancedRecyclerView) public AdvancedRecyclerView advancedRecyclerView;

    public List<T> items = new ArrayList();
    public boolean isDownloading = false;
    public boolean canLoadMore = true;
    public int offset = 0;

    private CustomRecyclerViewAdapter adapter;
    private RecyclerView.ItemDecoration itemDecoration;

    public GenericItemListFragment() {
        // No-op.
    }

    public void basicInit() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_refreshable_recyclerview, container, false);
        ButterKnife.bind(this, view);

        basicInit();

        setupRecyclerView();
        setupCustomViews();

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

    public AdvancedRecyclerView getRecyclerView() {
        return advancedRecyclerView;
    }

    // Configuration

    public abstract CustomRecyclerViewAdapter getAdapterForViewType();

    public abstract RecyclerView.LayoutManager getLayoutManagerForViewType();

    public abstract RecyclerView.ItemDecoration getItemDecorationForViewType();

    public abstract void configureLayoutManagers();

    private void configureLayout() {
        // Replace layout manager.
        RecyclerView.LayoutManager manager = getLayoutManagerForViewType();
        if (advancedRecyclerView.getRecyclerView().getLayoutManager() == null || advancedRecyclerView.getRecyclerView().getLayoutManager().getClass() != manager.getClass()) {
            advancedRecyclerView.getRecyclerView().setLayoutManager(manager);
        }

        // Configure individual layouts.
        configureLayoutManagers();

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

    public abstract List<T> generateDummyData();

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
                List<T> loaded = generateDummyData();

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

    public void setupCustomViews() {

    }

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
