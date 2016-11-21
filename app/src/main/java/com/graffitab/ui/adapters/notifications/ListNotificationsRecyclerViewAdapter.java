package com.graffitab.ui.adapters.notifications;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.IntRange;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.graffitab.R;
import com.graffitab.graffitabsdk.model.GTNotification;
import com.graffitab.ui.adapters.notifications.viewholders.CommentNotificationViewHolder;
import com.graffitab.ui.adapters.notifications.viewholders.FollowNotificationViewHolder;
import com.graffitab.ui.adapters.notifications.viewholders.LikeNotificationViewHolder;
import com.graffitab.ui.adapters.notifications.viewholders.MentionNotificationViewHolder;
import com.graffitab.ui.adapters.notifications.viewholders.NotificationViewHolder;
import com.graffitab.ui.adapters.notifications.viewholders.WelcomeNotificationViewHolder;
import com.graffitab.ui.views.recyclerview.components.AdvancedEndlessRecyclerViewAdapter;

import java.util.List;

/**
 * Created by georgichristov on 15/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class ListNotificationsRecyclerViewAdapter extends AdvancedEndlessRecyclerViewAdapter<GTNotification> {

    private final int VIEW_TYPE_WELCOME = 0;
    private final int VIEW_TYPE_LIKE = 1;
    private final int VIEW_TYPE_FOLLOW = 2;
    private final int VIEW_TYPE_MENTION = 3;
    private final int VIEW_TYPE_COMMENT = 4;

    public ListNotificationsRecyclerViewAdapter(Context context, List<GTNotification> items) {
        super(context, items);
    }

    @Override
    public int getViewType(int position) {
        GTNotification.GTNotificationType type = getItem(position).type;
        switch (type) {
            case WELCOME: return VIEW_TYPE_WELCOME;
            case LIKE: return VIEW_TYPE_LIKE;
            case FOLLOW: return VIEW_TYPE_FOLLOW;
            case MENTION: return VIEW_TYPE_MENTION;
            case COMMENT: return VIEW_TYPE_COMMENT;
        }

        return VIEW_TYPE_WELCOME;
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_WELCOME: {
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notification_welcome, parent, false);
                WelcomeNotificationViewHolder rcv = new WelcomeNotificationViewHolder(layoutView);
                return rcv;
            }
            case VIEW_TYPE_LIKE: {
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notification_like, parent, false);
                LikeNotificationViewHolder rcv = new LikeNotificationViewHolder(layoutView);
                return rcv;
            }
            case VIEW_TYPE_FOLLOW: {
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notification_follow, parent, false);
                FollowNotificationViewHolder rcv = new FollowNotificationViewHolder(layoutView);
                return rcv;
            }
            case VIEW_TYPE_MENTION: {
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notification_mention, parent, false);
                MentionNotificationViewHolder rcv = new MentionNotificationViewHolder(layoutView);
                return rcv;
            }
            case VIEW_TYPE_COMMENT: {
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notification_comment, parent, false);
                CommentNotificationViewHolder rcv = new CommentNotificationViewHolder(layoutView);
                return rcv;
            }
        }
        return null;
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        NotificationViewHolder viewHolder = (NotificationViewHolder) holder;

        final GTNotification item = getItem(position);
        viewHolder.setItem(item);

        viewHolder.topTimelineSeparator.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
        viewHolder.bottomTimelineSeparator.setVisibility(position == getItemCount() - 2 /* Account for the infinite scroll view here. */ ? View.INVISIBLE : View.VISIBLE);
    }

    public static class RecyclerViewMargin extends RecyclerView.ItemDecoration {

        private final int columns;

        public RecyclerViewMargin(@IntRange(from = 0) int columns) {
            this.columns = columns;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {}
    }
}
