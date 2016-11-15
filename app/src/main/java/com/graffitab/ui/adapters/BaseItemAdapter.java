package com.graffitab.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class BaseItemAdapter<T> extends BaseAdapter {

    public Context context;

    public List<T> itemsList;
    public LayoutInflater mInflater;

    public BaseItemAdapter(Context context, List<T> items) {
        this.context = context;
        this.mInflater = LayoutInflater.from( context );

        if (items == null)
            itemsList = new ArrayList<T>();
        else
            itemsList = new ArrayList<T>(items);
    }

    public void setItems(List<T> list) {
        if (list == null)
            this.itemsList = new ArrayList<T>();
        else
            this.itemsList = new ArrayList<T>(list);

        notifyDataSetChanged();
    }

    public void remove(int index) {
        itemsList.remove(index);

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return itemsList.size();
    }

    @Override
    public T getItem(int pos) {
        return itemsList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
