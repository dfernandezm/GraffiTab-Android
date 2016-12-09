package com.graffitab.ui.adapters.streamables.viewholders;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitabsdk.model.GTStreamable;

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

    public void setItem(GTStreamable notification, int position) {
        this.item = notification;

        streamableView.setBackgroundColor(Color.parseColor(Constants.PALLETE[position % Constants.PALLETE.length]));
    }

    public void openUserProfile() {

    }

    public void openPost() {

    }

    public void openLikes() {

    }

    public void openComments() {

    }

    public void share() {

    }

    public void toggleLike() {

    }
}
