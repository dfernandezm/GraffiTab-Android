package com.graffitab.ui.adapters.streamables.viewholders;

import android.view.View;

import com.graffitabsdk.model.GTStreamable;

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
    public void setItem(GTStreamable streamable) {
        super.setItem(streamable);
    }

    @Override
    protected void setupViews() {
        super.setupViews();

        streamableView.setLikeImageScale(0.5f);
    }
}
