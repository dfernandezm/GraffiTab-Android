package com.graffitab.ui.fragments;

import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.ui.views.recyclerview.AdvancedRecyclerView;
import com.graffitab.ui.views.recyclerview.components.AdvancedEndlessRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecyclerViewLayoutConfiguration;
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

    public AdvancedEndlessRecyclerViewAdapter adapter;
    public List<T> items = new ArrayList();
    public boolean isDownloading = false;
    public boolean canLoadMore = true;
    public int offset = 0;
    public boolean hasOptionsMenu = false;

    private RecyclerView.ItemDecoration itemDecoration;
    private boolean setupEndlessScrolling = false;

    public GenericItemListFragment() {
        // No-op.
    }

    public void basicInit() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (hasOptionsMenu) {
            inflater.inflate(R.menu.menu_refresh, menu);

            for (int i = 0; i < menu.size(); i++) {
                MenuItem menuItem = menu.getItem(i);
                Drawable drawable = menuItem.getIcon();
                if (drawable != null) {
                    drawable.mutate();
                    drawable.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            advancedRecyclerView.beginRefreshing();
            refresh();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    public abstract RecyclerView.ItemDecoration getItemDecoration();

    public abstract AdvancedEndlessRecyclerViewAdapter getAdapterForViewType();

    public abstract RecyclerView.LayoutManager getLayoutManagerForViewType();

    public abstract AdvancedRecyclerViewLayoutConfiguration getLayoutConfiguration();

    public void configureLayout() {
        // Replace layout manager.
        RecyclerView.LayoutManager manager = getLayoutManagerForViewType();
        if (advancedRecyclerView.getRecyclerView().getLayoutManager() == null || advancedRecyclerView.getRecyclerView().getLayoutManager().getClass() != manager.getClass()) {
            advancedRecyclerView.getRecyclerView().setLayoutManager(manager);
        }

        // Configure individual layouts.
        AdvancedRecyclerViewLayoutConfiguration layoutConfiguration = getLayoutConfiguration();
        if (layoutConfiguration != null) {
            if (advancedRecyclerView.getRecyclerView().getLayoutManager() instanceof GridLayoutManager)
                ((GridLayoutManager) advancedRecyclerView.getRecyclerView().getLayoutManager()).setSpanCount(layoutConfiguration.getSpanCount());
            else if (advancedRecyclerView.getRecyclerView().getLayoutManager() instanceof StaggeredGridLayoutManager)
                ((StaggeredGridLayoutManager) advancedRecyclerView.getRecyclerView().getLayoutManager()).setSpanCount(layoutConfiguration.getSpanCount());
        }

        // Replace item decoration.
        if (itemDecoration != null)
            advancedRecyclerView.getRecyclerView().removeItemDecoration(itemDecoration);
        itemDecoration = getItemDecoration();
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
        if (isDownloading) return;

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
                if (getActivity() == null)
                    return;

                // TODO: This is just a dummy procedure to generate some random content.
                List<T> loaded = generateDummyData();
                if (offset > 70)
                    loaded.clear();

                // Clear items if we are pulling to refresh.
                if (o == 0)
                    items.clear();

                // Merge newly loaded items.
                items.addAll(loaded);

                // Configure load more layout.
                if (loaded.size() <= 0 || loaded.size() < Constants.MAX_ITEMS)
                    canLoadMore = false;

                adapter.setCanLoadMore(canLoadMore, advancedRecyclerView.getRecyclerView());

                // Finalize and refresh UI.
                finalizeLoad();
            }
        }, 3000);
    }

    private void finalizeCacheLoad() {
        if (getActivity() == null)
            return;

        adapter.setItems(items);
        adapter.finishLoadingMore();
        adapter.notifyDataSetChanged();
    }

    private void finalizeLoad() {
        if (getActivity() == null)
            return;

        isDownloading = false;

        adapter.setItems(items);
        adapter.finishLoadingMore();
        adapter.notifyDataSetChanged();

        advancedRecyclerView.endRefreshing();
        advancedRecyclerView.addOnEmptyViewListsner(this);

        if (!setupEndlessScrolling) {
            setupEndlessScrolling = true;
            setupEndlessScroll();
        }
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

                setupCustomViews();

                loadItems(true, offset);
            }
        });
    }

    private void setupEndlessScroll() {
        advancedRecyclerView.addOnLoadMoreListener(new AdvancedRecyclerView.OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
            if (canLoadMore && !isDownloading) {
                offset += Constants.MAX_ITEMS;
                loadItems(false, offset);
            }
            else {
                isDownloading = false;
                adapter.setCanLoadMore(false, advancedRecyclerView.getRecyclerView());
            }
            }
        });
    }
}
