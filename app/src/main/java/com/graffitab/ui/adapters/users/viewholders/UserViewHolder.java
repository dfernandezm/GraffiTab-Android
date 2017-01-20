package com.graffitab.ui.adapters.users.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.graffitab.R;
import com.graffitab.ui.adapters.users.OnUserClickListener;
import com.graffitabsdk.model.GTUser;
import com.squareup.picasso.Picasso;

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
    protected OnUserClickListener clickListener;

    public UserViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        setupViews();
    }

    public void setItem(GTUser user) {
        this.item = user;

        loadAvatar();
    }

    public void loadAvatar() {
        if (item.hasAvatar())
            Picasso.with(avatar.getContext()).load(item.avatar.thumbnail).error(R.drawable.default_avatar).into(avatar);
        else
            Picasso.with(avatar.getContext()).load(R.drawable.default_avatar).into(avatar);
    }

    public GTUser getItem() {
        return item;
    }

    public void setClickListener(OnUserClickListener listener) {
        this.clickListener = listener;
    }

    public void onClickToggleFollow() {
        if (clickListener != null)
            clickListener.onToggleFollow(item, this, getAdapterPosition());
    }

    // Setup

    protected void setupViews() {
        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (clickListener != null)
                    clickListener.onRowSelected(item, getAdapterPosition());
            }
        });
    }
}
