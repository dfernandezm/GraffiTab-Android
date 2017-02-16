package com.graffitab.ui.adapters.streamables.viewholders;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.ui.adapters.streamables.OnStreamableClickListener;
import com.graffitab.ui.views.likeimageview.LikeImageView;
import com.graffitabsdk.model.GTStreamable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 17/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class StreamableViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.streamableView) public LikeImageView streamableView;

    protected GTStreamable item;
    protected OnStreamableClickListener clickListener;

    public StreamableViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        setupViews();
    }

    public void setItem(GTStreamable streamable) {
        this.item = streamable;

        streamableView.setBackgroundColor(Color.parseColor(Constants.PALLETE[getAdapterPosition() % Constants.PALLETE.length]));

        loadStreamableImage();
    }

    public void loadStreamableImage() {
        streamableView.loadImage(item.asset.thumbnail);
    }

    public GTStreamable getItem() {
        return item;
    }

    public void setClickListener(OnStreamableClickListener listener) {
        this.clickListener = listener;
    }

    public void onClickUserProfile() {
        if (clickListener != null)
            clickListener.onOpenOwnerProfile(item, item.user, getAdapterPosition());
    }

    public void onClickLikes() {
        if (clickListener != null)
            clickListener.onOpenLikes(item, getAdapterPosition());
    }

    public void onClickComments() {
        if (clickListener != null)
            clickListener.onOpenComments(item, getAdapterPosition());
    }

    public void onClickShare() {
        if (clickListener != null)
            clickListener.onShare(item, ((BitmapDrawable) streamableView.getDrawable()).getBitmap(), getAdapterPosition());
    }

    public void onClickToggleLike() {
        if (clickListener != null)
            clickListener.onToggleLike(item, this, getAdapterPosition());
    }

    public void onClickDetails() {
        if (clickListener != null)
            clickListener.onRowSelected(item, getAdapterPosition());
    }

    // Setup

    protected void setupViews() {
        streamableView.setLikeListener(new LikeImageView.OnLikeListener() {

            @Override
            public void onLiked() {
                if (!item.likedByCurrentUser)
                    onClickToggleLike();
            }

            @Override
            public void onTapped() {
                onClickDetails();
            }
        });
    }
}
