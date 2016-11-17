package com.graffitab.ui.adapters.notifications.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.graffitab.R;
import com.graffitab.graffitabsdk.model.GTNotification;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 17/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class NotificationViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.topTimelineSeparator) public View topTimelineSeparator;
    @BindView(R.id.bottomTimelineSeparator) public View bottomTimelineSeparator;
    @BindView(R.id.unreadIndicator) public View unreadIndicator;

    protected GTNotification item;

    public NotificationViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setItem(GTNotification notification) {
        this.item = notification;
    }
}
