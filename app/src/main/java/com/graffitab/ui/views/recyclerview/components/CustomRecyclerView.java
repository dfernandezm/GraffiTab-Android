package com.graffitab.ui.views.recyclerview.components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by georgichristov on 18/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class CustomRecyclerView extends RecyclerView {

    @Nullable
    private View emptyView;

    private OnEmptyViewRecyclerListener emptyListener;

    final @NonNull AdapterDataObserver observer = new AdapterDataObserver() {

        @Override
        public void onChanged() {
            super.onChanged();
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            checkIfEmpty();
        }
    };

    public CustomRecyclerView(Context context) {
        super(context);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null)
            oldAdapter.unregisterAdapterDataObserver(observer);

        super.setAdapter(adapter);
        if (adapter != null)
            adapter.registerAdapterDataObserver(observer);
    }

    @Override
    public void swapAdapter(Adapter adapter, boolean removeAndRecycleExistingViews) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null)
            oldAdapter.unregisterAdapterDataObserver(observer);

        if (adapter != null)
            adapter.registerAdapterDataObserver(observer);

        super.swapAdapter(adapter, removeAndRecycleExistingViews);
        checkIfEmpty();
    }

    public void setEmptyView(@Nullable View emptyView, OnEmptyViewRecyclerListener listener) {
        this.emptyListener = listener;
        this.emptyView = emptyView;

        checkIfEmpty();
    }

    private void checkIfEmpty() {
        if (emptyView != null) {
            if (emptyListener != null) {
                emptyListener.willShowEmptyView();
                emptyView.setVisibility(getAdapter().getItemCount() > 0 ? GONE : VISIBLE);
            }
        }
    }

    public interface OnEmptyViewRecyclerListener {
        void willShowEmptyView();
    }
}
