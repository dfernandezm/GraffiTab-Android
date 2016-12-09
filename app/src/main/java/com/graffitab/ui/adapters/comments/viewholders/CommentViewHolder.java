package com.graffitab.ui.adapters.comments.viewholders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitab.ui.activities.home.users.ProfileActivity;
import com.graffitabsdk.model.GTComment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 09/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class CommentViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.avatar) public ImageView avatar;
    @BindView(R.id.nameField) public TextView nameField;
    @BindView(R.id.usernameField) public TextView usernameField;
    @BindView(R.id.textField) public TextView textField;

    protected GTComment item;

    public CommentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        setupViews();
    }

    public void setItem(GTComment comment) {
        this.item = comment;

        textField.setText(comment.text);
    }

    public void openUserProfile() {
        Context context = itemView.getContext();
        context.startActivity(new Intent(context, ProfileActivity.class));
    }

    // Setup

    protected void setupViews() {
        View.OnClickListener profileListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                openUserProfile();
            }
        };
        avatar.setClickable(true);
        avatar.setOnClickListener(profileListener);
        nameField.setClickable(true);
        nameField.setOnClickListener(profileListener);
        usernameField.setClickable(true);
        usernameField.setOnClickListener(profileListener);
    }
}
