package com.graffitab.ui.views.recyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.graffitab.R;

import java.util.List;

/**
 * Created by georgichristov on 21/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public abstract class AdvancedEndlessRecyclerViewAdapter<T> extends AdvancedRecyclerViewAdapter<T> {

    private AdvancedRecyclerView.OnLoadMoreListener onLoadMoreListener;
    private RecyclerView.OnScrollListener onScrollListener;
    private boolean isMoreLoading = false;
    private boolean canLoadMore = true;
    private int visibleThreshold = 1;
    private int firstVisibleItem, visibleItemCount, totalItemCount;

    public AdvancedEndlessRecyclerViewAdapter(Context context, List<T> items, RecyclerView recyclerView) {
        super(context, items, recyclerView);
    }

    @Override
    public void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindFooterViewHolder(holder, position);

        View progressView = holder.itemView.findViewById(R.id.loadMoreIndicator);
        View loadedAllView = holder.itemView.findViewById(R.id.loadedAllIndicator);

        progressView.setVisibility(canLoadMore ? View.VISIBLE : View.GONE);
        loadedAllView.setVisibility(canLoadMore ? View.GONE : View.VISIBLE);
    }

    public void addOnLoadMoreListener(AdvancedRecyclerView.OnLoadMoreListener listener, RecyclerView recyclerView) {
        removeEndlessListener(recyclerView);

        View view = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.decoration_footer_endless, recyclerView, false);
        addFooter(view, recyclerView);

        this.onLoadMoreListener = listener;
        this.onScrollListener = new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = layoutManager.getItemCount();

                // Find first visible item.
                if (layoutManager != null && layoutManager instanceof LinearLayoutManager)
                    firstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                if (layoutManager != null && layoutManager instanceof GridLayoutManager)
                    firstVisibleItem = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
                if (layoutManager != null && layoutManager instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) layoutManager;

                    int[] firstVisibleItems = null;
                    firstVisibleItems = manager.findFirstVisibleItemPositions(firstVisibleItems);
                    if (firstVisibleItems != null && firstVisibleItems.length > 0)
                        firstVisibleItem = firstVisibleItems[0];
                }

                // Check if we should load more.
                if (canLoadMore && !isMoreLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isMoreLoading = true;
                }
            }
        };
        recyclerView.addOnScrollListener(onScrollListener);
    }

    public void removeEndlessListener(RecyclerView recyclerView) {
        if (onScrollListener != null) {
            recyclerView.removeOnScrollListener(onScrollListener);
            removeFooterView(recyclerView);
        }
    }

    public void finishLoadingMore() {
        this.isMoreLoading = false;
    }

    public void setCanLoadMore(boolean canLoadMore, RecyclerView recyclerView) {
        this.canLoadMore = canLoadMore;
        notifyDataSetChanged();
    }
}
