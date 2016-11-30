package com.graffitab.ui.adapters.streamables.viewholders;

import android.view.View;

import com.graffitab.graffitabsdk.model.GTStreamable;

import butterknife.ButterKnife;

/**
 * Created by georgichristov on 17/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class GridStreamableViewHolder extends StreamableViewHolder {

    public GridStreamableViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setItem(GTStreamable notification, int position) {
        super.setItem(notification, position);
    }
}
