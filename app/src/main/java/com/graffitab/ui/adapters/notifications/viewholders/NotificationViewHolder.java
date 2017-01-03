package com.graffitab.ui.adapters.notifications.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.graffitab.R;
import com.graffitab.ui.adapters.notifications.OnNotificationClickListener;
import com.graffitabsdk.model.GTNotification;

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
    protected OnNotificationClickListener clickListener;

    public NotificationViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        setupViews();
    }

    public void setItem(GTNotification notification) {
        this.item = notification;

        unreadIndicator.setVisibility(item.isRead ? View.INVISIBLE : View.VISIBLE);
    }

    public GTNotification getItem() {
        return item;
    }

    public void setClickListener(OnNotificationClickListener listener) {
        this.clickListener = listener;
    }

    // Setup

    protected void setupViews() {
        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (clickListener != null)
                    clickListener.onRowSelected(item);
            }
        });
    }
}
