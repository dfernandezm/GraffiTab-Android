package com.graffitab.ui.adapters.streamables;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.graffitab.R;
import com.graffitab.ui.adapters.streamables.viewholders.GridStreamableViewHolder;
import com.graffitab.ui.adapters.streamables.viewholders.ListStreamableViewHolder;
import com.graffitab.ui.adapters.streamables.viewholders.SwimlaneStreamableViewHolder;
import com.graffitab.ui.adapters.streamables.viewholders.TrendingStreamableViewHolder;
import com.graffitab.ui.fragments.streamables.GenericStreamablesFragment.ViewType;
import com.graffitab.ui.views.recyclerview.components.AdvancedEndlessRecyclerViewAdapter;
import com.graffitabsdk.model.GTStreamable;

import java.util.List;

/**
 * Created by georgichristov on 30/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class GenericStreamablesRecyclerViewAdapter extends AdvancedEndlessRecyclerViewAdapter<GTStreamable> {

    private final int VIEW_TYPE_TRENDING = 0;
    private final int VIEW_TYPE_LIST_FULL = 1;
    private final int VIEW_TYPE_SWIMLANE = 2;
    private final int VIEW_TYPE_GRID = 3;

    private ViewType itemViewType;
    private OnStreamableClickListener clickListener;

    public GenericStreamablesRecyclerViewAdapter(Context context, List<GTStreamable> items, RecyclerView recyclerView) {
        super(context, items, recyclerView);
    }

    public void setViewType(ViewType type) {
        this.itemViewType = type;
    }

    public void setClickListener(OnStreamableClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public int getViewType(int position) {
        switch (itemViewType) {
            case TRENDING: return VIEW_TYPE_TRENDING;
            case LIST_FULL: return VIEW_TYPE_LIST_FULL;
            case SWIMLANE: return VIEW_TYPE_SWIMLANE;
            case GRID: return VIEW_TYPE_GRID;
        }

        return VIEW_TYPE_GRID;
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_TRENDING) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_streamable_trending, parent, false);
            TrendingStreamableViewHolder rcv = new TrendingStreamableViewHolder(layoutView);
            rcv.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (clickListener != null)
                        clickListener.onClick(view);
                }
            });
            return rcv;
        }
        else if (viewType == VIEW_TYPE_LIST_FULL) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_streamable_list, parent, false);
            ListStreamableViewHolder rcv = new ListStreamableViewHolder(layoutView);
            rcv.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (clickListener != null)
                        clickListener.onClick(view);
                }
            });
            return rcv;
        }
        else if (viewType == VIEW_TYPE_SWIMLANE) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_streamable_swimlane, parent, false);
            SwimlaneStreamableViewHolder rcv = new SwimlaneStreamableViewHolder(layoutView);
            rcv.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (clickListener != null)
                        clickListener.onClick(view);
                }
            });
            return rcv;
        }
        else { // Grid is default case.
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_streamable_grid, parent, false);
            GridStreamableViewHolder rcv = new GridStreamableViewHolder(layoutView);
            rcv.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (clickListener != null)
                        clickListener.onClick(view);
                }
            });
            return rcv;
        }
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final GTStreamable item = getItem(position);

        if (holder instanceof TrendingStreamableViewHolder) {
            TrendingStreamableViewHolder customHolder = (TrendingStreamableViewHolder) holder;
            customHolder.setItem(item, position);
        }
        else if (holder instanceof ListStreamableViewHolder) {
            ListStreamableViewHolder customHolder = (ListStreamableViewHolder) holder;
            customHolder.setItem(item, position);
        }
        else if (holder instanceof SwimlaneStreamableViewHolder) {
            SwimlaneStreamableViewHolder customHolder = (SwimlaneStreamableViewHolder) holder;
            customHolder.setItem(item, position);
        }
        else if (holder instanceof GridStreamableViewHolder) {
            GridStreamableViewHolder customHolder = (GridStreamableViewHolder) holder;
            customHolder.setItem(item, position);
        }
    }
}
