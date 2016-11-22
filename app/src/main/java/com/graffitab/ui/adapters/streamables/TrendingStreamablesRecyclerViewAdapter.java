package com.graffitab.ui.adapters.streamables;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.graffitabsdk.model.GTStreamable;
import com.graffitab.ui.adapters.streamables.viewholders.TrendingStreamableViewHolder;
import com.graffitab.ui.views.recyclerview.components.AdvancedEndlessRecyclerViewAdapter;

import java.util.List;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class TrendingStreamablesRecyclerViewAdapter extends AdvancedEndlessRecyclerViewAdapter<GTStreamable> {

    public TrendingStreamablesRecyclerViewAdapter(Context context, List<GTStreamable> items) {
        super(context, items);
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_streamable_trending, parent, false);
        TrendingStreamableViewHolder rcv = new TrendingStreamableViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        TrendingStreamableViewHolder customHolder = (TrendingStreamableViewHolder) holder;

        final GTStreamable item = getItem(position);
        customHolder.setItem(item);

        customHolder.streamableView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, item.asset.thumbnailHeight));
        customHolder.streamableView.setBackgroundColor(Color.parseColor(Constants.PALLETE[position % Constants.PALLETE.length]));
    }
}
