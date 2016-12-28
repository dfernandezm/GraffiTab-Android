package com.graffitab.ui.views.recyclerview.components;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.graffitab.utils.Utils;

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

    private View headerView;
    private View footerView;

    private List<T> itemList;
    protected List<Integer> itemTypes = new ArrayList<>();

    public AdvancedRecyclerViewAdapter(Context context, List<T> itemList, RecyclerView recyclerView) {
        this.context = context;

        setItems(itemList, recyclerView);
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

    public boolean isEmpty() {
        return itemList.isEmpty();
    }

    @Override
    public int getItemCount() {
        if (isEmpty()) {
            if (!hasHeader())
                return 0; // Do not show header or footer.
            else
                return 1; // Only show header.
        }
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
            return new DecorationViewHolder(headerView, ((RecyclerView) parent).getLayoutManager());
        else if (viewType == ViewTypes.FOOTER)
            return new DecorationViewHolder(footerView, ((RecyclerView) parent).getLayoutManager());
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
        return itemTypes.contains(ViewTypes.HEADER);
    }

    public void addHeaderView(View view, RecyclerView recyclerView) {
        headerView = view;
        rebuildItemTypes();
        notifyDataSetChanged();
        configureDecorationLayout(recyclerView);
    }

    public void removeHeaderView(RecyclerView recyclerView) {
        headerView = null;
        rebuildItemTypes();
        notifyDataSetChanged();
        configureDecorationLayout(recyclerView);
    }

    public void addFooter(View view, RecyclerView recyclerView) {
        footerView = view;
        rebuildItemTypes();
        notifyDataSetChanged();
        configureDecorationLayout(recyclerView);
    }

    public void removeFooterView(RecyclerView recyclerView) {
        footerView = null;
        rebuildItemTypes();
        notifyDataSetChanged();
        configureDecorationLayout(recyclerView);
    }

    public void setItems(List<T> items, RecyclerView recyclerView) {
        if (items == null) this.itemList = new ArrayList();
        else this.itemList = new ArrayList(items);
        rebuildItemTypes();
        notifyDataSetChanged();
        configureDecorationLayout(recyclerView);
    }

    public void removeItem(int position, final RecyclerView recyclerView) {
        itemList.remove(position);
        notifyItemRemoved(position);

        Utils.runWithDelay(new Runnable() {

            @Override
            public void run() {
                rebuildItemTypes();
                notifyDataSetChanged();
                configureDecorationLayout(recyclerView);
            }
        }, 400);
    }

    public T getItem(int position) {
        return itemList.get(position);
    }

    public void configureDecorationLayout(RecyclerView recyclerView) {
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

        if (headerView != null) itemTypes.add(ViewTypes.HEADER); // Add header, if any.
        for (T item : itemList) // Add items.
            itemTypes.add(ViewTypes.NORMAL);
        if (footerView != null) itemTypes.add(ViewTypes.FOOTER); // Add footer, if any.
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
