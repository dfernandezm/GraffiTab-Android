package com.graffitab.ui.adapters.streamables.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitab.graffitabsdk.model.GTStreamable;

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
    public void setItem(GTStreamable notification) {
        super.setItem(notification);
    }
}
