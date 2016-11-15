package com.graffitab.ui.adapters.streamables;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.ui.adapters.BaseItemRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class GridStreamablesRecyclerAdapter extends BaseItemRecyclerAdapter<Integer> {

    public GridStreamablesRecyclerAdapter(Context context, List<Integer> items) {
        super(context, items);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_streamable_grid, null);
        GridViewHolder rcv = new GridViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Integer item = getItem(position);

        ((GridViewHolder) holder).streamableView.setBackgroundColor(Color.parseColor(Constants.PALLETE[position % Constants.PALLETE.length]));
    }

    static class GridViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.streamableView) ImageView streamableView;

        public GridViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
