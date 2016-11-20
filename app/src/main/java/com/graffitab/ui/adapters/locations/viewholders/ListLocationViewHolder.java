package com.graffitab.ui.adapters.locations.viewholders;

import android.view.View;
import android.widget.ImageView;

import com.graffitab.R;
import com.graffitab.graffitabsdk.model.GTLocation;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 17/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class ListLocationViewHolder extends LocationViewHolder {

    @BindView(R.id.thumbnail) public ImageView thumbnail;

    public ListLocationViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setItem(GTLocation location) {
        super.setItem(location);
    }
}
