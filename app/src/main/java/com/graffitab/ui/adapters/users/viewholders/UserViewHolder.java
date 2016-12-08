package com.graffitab.ui.adapters.users.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.graffitab.R;
import com.graffitabsdk.model.GTUser;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 17/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class UserViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.avatar) public ImageView avatar;

    protected GTUser item;

    public UserViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setItem(GTUser user) {
        this.item = user;
    }
}
