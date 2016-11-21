package com.graffitab.ui.views.recyclerview.components;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.graffitab.R;
import com.graffitab.ui.views.recyclerview.AdvancedRecyclerView;

import java.util.List;

/**
 * Created by georgichristov on 21/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public abstract class AdvancedEndlessRecyclerViewAdapter<T> extends AdvancedRecyclerViewAdapter<T> {

    private AdvancedRecyclerView.OnLoadMoreListener onLoadMoreListener;
    private boolean isMoreLoading = false;
    private boolean canLoadMore = true;
    private int visibleThreshold = 1;
    private int firstVisibleItem, visibleItemCount, totalItemCount;
    private int lastProgressBarPosition = -1;

    public AdvancedEndlessRecyclerViewAdapter(Context context, List<T> items) {
        super(context, items);
    }

    public void addOnLoadMoreListener(AdvancedRecyclerView.OnLoadMoreListener listener, RecyclerView recyclerView) {
        addFooter(R.layout.decoration_footer_endless, recyclerView);

        this.onLoadMoreListener = listener;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = layoutManager.getItemCount();

                if (layoutManager != null && layoutManager instanceof LinearLayoutManager)
                    firstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                if (layoutManager != null && layoutManager instanceof GridLayoutManager)
                    firstVisibleItem = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
                if (layoutManager != null && layoutManager instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) layoutManager;

                    int[] firstVisibleItems = null;
                    firstVisibleItems = manager.findFirstVisibleItemPositions(firstVisibleItems);
                    if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                        firstVisibleItem = firstVisibleItems[0];
                    }
                }

                if (canLoadMore && !isMoreLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isMoreLoading = true;
                }
            }
        });
    }

    public void finishLoadingMore() {
        this.isMoreLoading = false;
    }

    public void setCanLoadMore(boolean canLoadMore, RecyclerView recyclerView) {
        this.canLoadMore = canLoadMore;

        if (!canLoadMore)
            removeFooterView();
        else
            addFooter(R.layout.decoration_footer_endless, recyclerView);
    }
}
