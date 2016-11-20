package com.graffitab.ui.adapters.streamables.viewholders;

import android.view.View;

import com.graffitab.graffitabsdk.model.GTStreamable;

import butterknife.ButterKnife;

/**
 * Created by georgichristov on 17/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class SwimlaneStreamableViewHolder extends StreamableViewHolder {

    public SwimlaneStreamableViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setItem(GTStreamable notification) {
        super.setItem(notification);
    }
}
