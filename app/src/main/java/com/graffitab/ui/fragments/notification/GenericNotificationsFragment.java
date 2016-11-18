package com.graffitab.ui.fragments.notification;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.graffitabsdk.model.GTNotification;
import com.graffitab.ui.adapters.notifications.ListNotificationsRecyclerViewAdapter;
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
public abstract class GenericNotificationsFragment extends Fragment implements AdvancedRecyclerView.OnEmptyViewListener {

    public enum ViewType {LIST_FULL}

    @BindView(R.id.advancedRecyclerView) AdvancedRecyclerView advancedRecyclerView;

    public List<GTNotification> items = new ArrayList();
    public boolean isDownloading = false;
    public boolean canLoadMore = true;
    public int offset = 0;

    private ViewType viewType;
    private CustomRecyclerViewAdapter adapter;
    private RecyclerView.ItemDecoration itemDecoration;

    public GenericNotificationsFragment() {
        // No-op.
    }

    public void basicInit() {
        setViewType(ViewType.LIST_FULL);
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
            case LIST_FULL:
                return new ListNotificationsRecyclerViewAdapter(getActivity(), items);
        }

        return null;
    }

    private RecyclerView.LayoutManager getLayoutManagerForViewType() {
        switch (viewType) {
            case LIST_FULL:
                return new LinearLayoutManager(getContext());
        }
        return null;
    }

    private RecyclerView.ItemDecoration getItemDecorationForViewType() {
        switch (viewType) {
            case LIST_FULL:
                return new ListNotificationsRecyclerViewAdapter.RecyclerViewMargin(1);
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
                List<GTNotification> loaded = new ArrayList();
                for (int i = 0; i < 25; i++) {
                    GTNotification notification = new GTNotification();
                    notification.type = GTNotification.GTNotificationType.values()[Utils.randInt(0, GTNotification.GTNotificationType.values().length - 1)];
                    notification.isRead = i > 2;
                    loaded.add(notification);
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

        // We must set the adapter after we set the footer view, otherwise the footer will not show.
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
