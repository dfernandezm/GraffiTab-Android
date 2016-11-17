package com.graffitab.ui.adapters.streamables.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.graffitab.R;
import com.graffitab.graffitabsdk.model.GTStreamable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 17/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class StreamableViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.streamableView) public ImageView streamableView;

    protected GTStreamable item;

    public StreamableViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setItem(GTStreamable notification) {
        this.item = notification;
    }
}
