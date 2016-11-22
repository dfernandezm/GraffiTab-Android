package com.graffitab.ui.adapters.locations;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.graffitab.R;
import com.graffitab.graffitabsdk.model.GTLocation;
import com.graffitab.ui.adapters.locations.viewholders.ListLocationViewHolder;
import com.graffitab.ui.views.recyclerview.components.AdvancedEndlessRecyclerViewAdapter;

import java.util.List;

/**
 * Created by georgichristov on 15/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class ListLocationsRecyclerViewAdapter extends AdvancedEndlessRecyclerViewAdapter<GTLocation> {

    public ListLocationsRecyclerViewAdapter(Context context, List<GTLocation> items) {
        super(context, items);
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_location_list, parent, false);
        ListLocationViewHolder rcv = new ListLocationViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListLocationViewHolder customHolder = (ListLocationViewHolder) holder;

        final GTLocation item = getItem(position);
        customHolder.setItem(item);
    }
}
