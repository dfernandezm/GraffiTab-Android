package com.graffitab.ui.adapters.notifications.viewholders;

import android.view.View;

import com.graffitab.R;
import com.graffitabsdk.model.GTNotification;

import butterknife.ButterKnife;

/**
 * Created by georgichristov on 17/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class CommentNotificationViewHolder extends NotificationViewHolder {

    public CommentNotificationViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setItem(GTNotification notification) {
        super.setItem(notification);

        actionLbl.setText(itemView.getContext().getString(R.string.notifications_comment, item.commenter.fullName()));
        descriptionLbl.setText(item.comment.text);

        loadAvatar(item.commenter);
        loadStreamable(item.commentedStreamable);
    }
}
