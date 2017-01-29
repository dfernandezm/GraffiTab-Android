package com.graffitab.ui.fragments;

import android.content.res.Configuration;
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
import com.graffitab.ui.dialog.DialogBuilder;
import com.graffitab.ui.views.recyclerview.AdvancedRecyclerView;
import com.graffitab.ui.views.recyclerview.components.AdvancedEndlessRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecyclerViewLayoutConfiguration;
import com.graffitab.utils.Utils;
import com.graffitab.utils.activity.ActivityUtils;
import com.graffitabsdk.constants.GTConstants;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.network.common.result.GTListItemsResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.graffitabsdk.constants.GTConstants.MAX_ITEMS;

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
        basicInit();
    }

    /** This method is called from the constructor so avoid any view-related operations. */
    public void basicInit() {
        // No-op
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
        }

        ActivityUtils.colorMenu(getContext(), menu);
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
        return getString(R.string.other_empty_no_items);
    }

    @Override
    public String emptyViewSubtitle() {
        return getString(R.string.other_empty_no_items_description);
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
            adapter.configureDecorationLayout(getRecyclerView().getRecyclerView());
        }
    }

    // Loading

    public abstract void loadItems(boolean isFirstLoad, int offset, GTResponseHandler<T> handler);

    public void reload() {
        refresh();
    }

    private void refresh() {
        if (isDownloading) return;

        offset = 0;
        canLoadMore = true;

        loadItems(false, offset);
    }

    private void loadItems(boolean isFirstLoad, final int o) {
        if (getActivity() == null)
            return;

        if (items.size() <= 0 && !isDownloading) {
            if (isFirstLoad)
                advancedRecyclerView.beginRefreshing();
        }

        isDownloading = true;

        loadItems(isFirstLoad, o, new GTResponseHandler<T>() {

            @Override
            public void onSuccess(GTResponse<T> gtResponse) {
                if (getActivity() == null)
                    return;

                // Clear items if we are pulling to refresh.
                if (o == 0)
                    items.clear();

                // Merge newly loaded items.
                GTListItemsResult<T> loaded = (GTListItemsResult<T>) gtResponse.getObject();
                items.addAll(loaded.items);

                // Configure load more layout.
                if (loaded.items.size() <= 0 || loaded.items.size() < MAX_ITEMS)
                    canLoadMore = false;

                adapter.setCanLoadMore(canLoadMore, advancedRecyclerView.getRecyclerView());

                // Finalize and refresh UI.
                finalizeLoad();
            }

            @Override
            public void onFailure(GTResponse<T> gtResponse) {
                if (getActivity() == null)
                    return;

                canLoadMore = false;
                adapter.setCanLoadMore(canLoadMore, advancedRecyclerView.getRecyclerView());
                finalizeLoad();
                DialogBuilder.buildAPIErrorDialog(getActivity(), getString(R.string.app_name), gtResponse.getResultDetail(), false);
            }

            @Override
            public void onCache(GTResponse<T> gtResponse) {
                super.onCache(gtResponse);

                if (getActivity() == null)
                    return;

                items.clear();

                GTListItemsResult<T> loaded = (GTListItemsResult<T>) gtResponse.getObject();
                items.addAll(loaded.items);
                finalizeCacheLoad();
            }
        });
    }

    private void finalizeCacheLoad() {
        if (getActivity() == null)
            return;

        adapter.setItems(items, getRecyclerView().getRecyclerView());
    }

    public void finalizeLoad() {
        if (getActivity() == null)
            return;

        isDownloading = false;

        adapter.setItems(items, getRecyclerView().getRecyclerView());
        adapter.finishLoadingMore();

        advancedRecyclerView.endRefreshing();
        advancedRecyclerView.addOnEmptyViewListener(this);

        if (!setupEndlessScrolling) {
            setupEndlessScrolling = true;
            setupEndlessScroll();
        }
    }

    // Setup

    public void setupCustomViews() {
        // No-op.
    }

    protected void setupRecyclerView() {
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

                configureLayout();

                Utils.runWithDelay(new Runnable() {

                    @Override
                    public void run() {
                        loadItems(true, offset);
                    }
                }, 100);
            }
        });
    }

    private void setupEndlessScroll() {
        advancedRecyclerView.addOnLoadMoreListener(new AdvancedRecyclerView.OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
            if (canLoadMore && !isDownloading) {
                offset += GTConstants.MAX_ITEMS;
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
