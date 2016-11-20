package com.graffitab.ui.adapters.locations.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitab.graffitabsdk.model.GTLocation;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 17/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class LocationViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.address) public TextView address;

    protected GTLocation item;

    public LocationViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setItem(GTLocation location) {
        this.item = location;
    }
}
