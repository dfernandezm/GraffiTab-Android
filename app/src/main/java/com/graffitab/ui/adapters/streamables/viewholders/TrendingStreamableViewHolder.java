package com.graffitab.ui.adapters.streamables.viewholders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitab.application.MyApplication;
import com.graffitab.utils.image.ImageUtils;
import com.graffitabsdk.model.GTStreamable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 17/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class TrendingStreamableViewHolder extends StreamableViewHolder {

    @BindView(R.id.avatar) public ImageView avatar;
    @BindView(R.id.nameField) public TextView nameField;
    @BindView(R.id.usernameField) public TextView usernameField;
    @BindView(R.id.likesField) public TextView likesField;
    @BindView(R.id.commentsField) public TextView commentsField;
    @BindView(R.id.likeStatusImage) public ImageView likeStatusImage;
    @BindView(R.id.commentsImage) public ImageView commentImage;

    public TrendingStreamableViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setItem(GTStreamable streamable) {
        super.setItem(streamable);

        int color = !streamable.likedByCurrentUser ? MyApplication.getInstance().getResources().getColor(R.color.colorMetadata) : MyApplication.getInstance().getResources().getColor(R.color.colorPrimary);
        likeStatusImage.setImageDrawable(ImageUtils.tintIcon(MyApplication.getInstance(), R.drawable.ic_thumb_up_black_24dp, color));
        likesField.setTextColor(color);

        likesField.setText(streamable.likersCount + "");
        commentsField.setText(streamable.commentsCount + "");

        streamableView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, item.asset.thumbnailHeight));
    }

    @Override
    protected void setupViews() {
        super.setupViews();

        View.OnClickListener profileListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onClickUserProfile();
            }
        };

        avatar.setClickable(true);
        avatar.setOnClickListener(profileListener);
        usernameField.setClickable(true);
        usernameField.setOnClickListener(profileListener);
        nameField.setClickable(true);
        nameField.setOnClickListener(profileListener);

        likesField.setClickable(true);
        likesField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onClickLikes();
            }
        });
        likeStatusImage.setClickable(true);
        likeStatusImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onClickLikes();
            }
        });

        View.OnClickListener commentListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onClickComments();
            }
        };
        commentsField.setClickable(true);
        commentsField.setOnClickListener(commentListener);
        commentImage.setClickable(true);
        commentImage.setOnClickListener(commentListener);
    }
}
