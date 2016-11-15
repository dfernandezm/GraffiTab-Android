package com.graffitab.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class BaseItemRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<T> itemList;
    private Context context;

    public BaseItemRecyclerAdapter(Context context, List<T> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    public T getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        throw new RuntimeException("Must be overridden");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        throw new RuntimeException("Must be overridden");
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
