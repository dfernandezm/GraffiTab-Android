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
public class MentionNotificationViewHolder extends NotificationViewHolder {

    public MentionNotificationViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setItem(GTNotification notification) {
        super.setItem(notification);

        actionLbl.setText(itemView.getContext().getString(R.string.notifications_mention, item.mentioner.fullName()));
        descriptionLbl.setText(item.mentionedComment.quotedText());

        loadAvatar(item.mentioner);
        loadStreamable(item.mentionedStreamable);
    }
}
