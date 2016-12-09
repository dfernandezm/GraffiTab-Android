package com.graffitab.ui.adapters.streamables.viewholders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.ui.activities.home.streamables.LikesActivity;
import com.graffitab.ui.activities.home.users.ProfileActivity;
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
        setupViews();
    }

    public void setItem(GTStreamable notification, int position) {
        this.item = notification;

        streamableView.setBackgroundColor(Color.parseColor(Constants.PALLETE[position % Constants.PALLETE.length]));
    }

    public void openUserProfile() {
        Context context = itemView.getContext();
        context.startActivity(new Intent(context, ProfileActivity.class));
    }

    public void openPost() {
        System.out.println("POST");
    }

    public void openLikes() {
        Context context = itemView.getContext();
        context.startActivity(new Intent(context, LikesActivity.class));
    }

    public void openComments() {
        System.out.println("COMMENTS");
    }

    public void share() {
        System.out.println("SHARE");
    }

    public void toggleLike() {
        System.out.println("LIKE");
    }

    // Setup

    protected void setupViews() {
        streamableView.setClickable(true);
        streamableView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                openPost();
            }
        });
    }
}
