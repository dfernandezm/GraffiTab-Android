package com.graffitab.ui.adapters.comments.viewholders;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitab.ui.activities.home.users.ProfileActivity;
import com.graffitabsdk.model.GTComment;
import com.luseen.autolinklibrary.AutoLinkMode;
import com.luseen.autolinklibrary.AutoLinkOnClickListener;
import com.luseen.autolinklibrary.AutoLinkTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.graffitab.R.id.textField;

/**
 * Created by georgichristov on 09/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class CommentViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.avatar) public ImageView avatar;
    @BindView(R.id.nameField) public TextView nameField;
    @BindView(R.id.usernameField) public TextView usernameField;
    @BindView(textField) public AutoLinkTextView autoLinkTextView;

    protected GTComment item;

    public CommentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        setupViews();
    }

    public void setItem(GTComment comment) {
        this.item = comment;

        autoLinkTextView.setAutoLinkText(comment.text);
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

        autoLinkTextView.setHashtagModeColor(ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary));
        autoLinkTextView.setMentionModeColor(ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary));
        autoLinkTextView.setUrlModeColor(ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary));
        autoLinkTextView.addAutoLinkMode(AutoLinkMode.MODE_URL, AutoLinkMode.MODE_HASHTAG, AutoLinkMode.MODE_MENTION);
        autoLinkTextView.setAutoLinkOnClickListener(new AutoLinkOnClickListener() {

            @Override
            public void onAutoLinkTextClick(AutoLinkMode autoLinkMode, String matchedText) {
                if (autoLinkMode == AutoLinkMode.MODE_URL) {

                }
                else if (autoLinkMode == AutoLinkMode.MODE_HASHTAG) {

                }
                else if (autoLinkMode == AutoLinkMode.MODE_MENTION) {

                }
            }
        });
    }
}
