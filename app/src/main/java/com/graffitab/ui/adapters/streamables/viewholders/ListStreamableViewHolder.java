package com.graffitab.ui.adapters.streamables.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitabsdk.model.GTStreamable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 17/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class ListStreamableViewHolder extends StreamableViewHolder {

    @BindView(R.id.avatar) public ImageView avatar;
    @BindView(R.id.nameField) public TextView nameField;
    @BindView(R.id.usernameField) public TextView usernameField;
    @BindView(R.id.dateField) public TextView dateField;
    @BindView(R.id.likesField) public TextView likesField;
    @BindView(R.id.commentsField) public TextView commentsField;
    @BindView(R.id.likeStatusImage) public ImageView likeStatusImage;
    @BindView(R.id.likeButton) public View likeButton;
    @BindView(R.id.commentButton) public View commentButton;
    @BindView(R.id.shareButton) public View shareButton;

    public ListStreamableViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setItem(GTStreamable notification, int position) {
        super.setItem(notification, position);
    }

    @Override
    protected void setupViews() {
        super.setupViews();

        View.OnClickListener profileListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                openUserProfile();
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
                openLikes();
            }
        });

        View.OnClickListener commentListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                openComments();
            }
        };
        commentsField.setClickable(true);
        commentsField.setOnClickListener(commentListener);
        commentButton.setClickable(true);
        commentButton.setOnClickListener(commentListener);

        shareButton.setClickable(true);
        shareButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                share();
            }
        });
    }
}
