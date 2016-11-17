package com.graffitab.ui.adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.graffitab.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public abstract class BaseItemRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<T> itemList;
    private Context context;

    private OnLoadMoreListener onLoadMoreListener;
    private RecyclerView.LayoutManager layoutManager;
    private boolean isMoreLoading = false;
    private int visibleThreshold = 1;
    private int firstVisibleItem, visibleItemCount, totalItemCount;
    private int lastProgressBarPosition = -1;

    public BaseItemRecyclerAdapter(Context context, List<T> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    public abstract RecyclerView.ViewHolder onCreateCustomViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindCustomViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM)
            return onCreateCustomViewHolder(parent, viewType);
        else {
            RecyclerView.ViewHolder holder = new ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_endless_footer, parent, false));

            if (layoutManager instanceof StaggeredGridLayoutManager) { // Workaround for staggered layouts.
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                layoutParams.setFullSpan(true);
            }

            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProgressViewHolder) {
            // No-op.
        }
        else
            onBindCustomViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public T getItem(int position) {
        return itemList.get(position);
    }

    public void addOnLoadMoreListener(OnLoadMoreListener listener, RecyclerView recyclerView) {
        this.onLoadMoreListener = listener;
        this.layoutManager = recyclerView.getLayoutManager();

        if (layoutManager instanceof GridLayoutManager) { // Workaround for grid layouts.
            final GridLayoutManager gridLayout = (GridLayoutManager) layoutManager;
            gridLayout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

                @Override
                public int getSpanSize(int position) {
                    switch (getItemViewType(position)) {
                        case VIEW_PROG:
                            return gridLayout.getSpanCount();
                        default:
                            return 1;
                    }
                }
            });
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

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

                if (!isMoreLoading && (totalItemCount - visibleItemCount)<= (firstVisibleItem + visibleThreshold)) {
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

    public void setProgressMore(final boolean isProgress) {
        // Remove any previous progress bar.
        if (lastProgressBarPosition >= 0 && lastProgressBarPosition < getItemCount())
            itemList.remove(lastProgressBarPosition);

        // Add progress row, if necessary.
        if (isProgress) {
            itemList.add(null);
            lastProgressBarPosition = getItemCount() - 1;

        }

        notifyDataSetChanged();
    }

    static class ProgressViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.load_more_progressBar) ProgressBar progressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnLoadMoreListener {
        public void onLoadMore();
    }
}
