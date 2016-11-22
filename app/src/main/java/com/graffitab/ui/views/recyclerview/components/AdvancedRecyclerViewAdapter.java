package com.graffitab.ui.views.recyclerview.components;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public abstract class AdvancedRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context context;

    private class ViewTypes {
        public static final int HEADER = 123456789;
        public static final int NORMAL = 1;
        public static final int FOOTER = 987654321;
    }

    private int headerResId = -1;
    private int footerResId = -1;

    private List<T> itemList;
    private List<Integer> itemTypes = new ArrayList<>();

    public AdvancedRecyclerViewAdapter(Context context, List<T> itemList) {
        this.context = context;

        setItems(itemList);
    }

    // Methods to override

    public abstract RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType);
    public abstract void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position);

    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        // No-op.
    }

    public void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {
        // No-op.
    }

    public int getViewType(int position) {
        return itemTypes.get(position);
    }

    // Methods from superclass

    @Override
    public int getItemCount() {
        return itemTypes.size();
    }

    @Override
    public int getItemViewType(int position) {
        int type = itemTypes.get(position);
        if (type == ViewTypes.HEADER || type == ViewTypes.FOOTER) // Return header or footer view type.
            return type;
        return getViewType(position); // Allow subclasses to return custom view types.
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ViewTypes.HEADER)
            return new DecorationViewHolder(LayoutInflater.from(parent.getContext()).inflate(headerResId, parent, false), ((RecyclerView) parent).getLayoutManager());
        else if (viewType == ViewTypes.FOOTER)
            return new DecorationViewHolder(LayoutInflater.from(parent.getContext()).inflate(footerResId, parent, false), ((RecyclerView) parent).getLayoutManager());
        else
            return onCreateItemViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (itemTypes.get(position) == ViewTypes.HEADER)
            onBindHeaderViewHolder(holder, position);
        else if (itemTypes.get(position) == ViewTypes.FOOTER)
            onBindFooterViewHolder(holder, position);
        else
            onBindItemViewHolder(holder, hasHeader() ? position - 1 : position);
    }

    // Public methods.

    public boolean hasHeader() {
        return headerResId >= 0;
    }

    public boolean hasFooter() {
        return footerResId >= 0;
    }

    public int getHeaderResId() {
        return headerResId;
    }

    public int getFooterResId() {
        return footerResId;
    }

    public void addHeaderView(int headerResId, RecyclerView recyclerView) {
        if (!hasHeader()) {
            this.headerResId = headerResId;
            itemTypes.add(0, ViewTypes.HEADER);
            setDecorationLayout(recyclerView);
            notifyDataSetChanged();
        }
    }

    public void removeHeaderView() {
        if (hasHeader()) {
            headerResId = -1;
            itemTypes.remove(0);
            notifyDataSetChanged();
        }
    }

    public void addFooter(int footerResId, RecyclerView recyclerView) {
        if (!hasFooter()) {
            this.footerResId = footerResId;
            itemTypes.add(ViewTypes.FOOTER);
            setDecorationLayout(recyclerView);
            notifyDataSetChanged();
        }
    }

    public void removeFooterView() {
        if (hasFooter()) {
            footerResId = -1;
            itemTypes.remove(itemTypes.size() - 1);
            notifyDataSetChanged();
        }
    }

    public void setItems(List<T> items) {
        if (items == null) this.itemList = new ArrayList();
        else this.itemList = new ArrayList(items);
        rebuildItemTypes();
        notifyDataSetChanged();
    }

    public void addItems(List<T> items) {
        itemList.addAll(items);
        rebuildItemTypes();
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        return itemList.get(position);
    }

    private void setDecorationLayout(RecyclerView recyclerView) {
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager gridLayout = (GridLayoutManager) recyclerView.getLayoutManager();
            gridLayout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    if (type == ViewTypes.HEADER || type == ViewTypes.FOOTER)
                        return gridLayout.getSpanCount();
                    else
                        return 1;
                }
            });
        }
    }

    private void rebuildItemTypes() {
        itemTypes.clear();

        if (headerResId >= 0) itemTypes.add(ViewTypes.HEADER); // Add header, if any.
        for (T item : itemList) // Add items.
            itemTypes.add(ViewTypes.NORMAL);
        if (footerResId >= 0) itemTypes.add(ViewTypes.FOOTER); // Add footer, if any.
    }

    // Custom classes

    static class DecorationViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView.LayoutManager layoutManager;

        public DecorationViewHolder(View itemView, RecyclerView.LayoutManager layoutManager) {
            super(itemView);

            this.layoutManager = layoutManager;

            setupSpan();
        }

        // Setup

        private void setupSpan() {
            if (layoutManager == null) return;

            if (layoutManager instanceof StaggeredGridLayoutManager) { // Set full span for this item.
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) itemView.getLayoutParams();
                layoutParams.setFullSpan(true);
            }
        }
    }
}
