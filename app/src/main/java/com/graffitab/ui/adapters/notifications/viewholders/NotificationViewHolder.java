package com.graffitab.ui.adapters.notifications.viewholders;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitab.ui.adapters.notifications.OnNotificationClickListener;
import com.graffitabsdk.model.GTNotification;
import com.graffitabsdk.model.GTStreamable;
import com.graffitabsdk.model.GTUser;
import com.squareup.picasso.Picasso;

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
    @BindView(R.id.dateField) public TextView dateField;
    @BindView(R.id.actionLbl) public TextView actionLbl;
    @BindView(R.id.avatar) public ImageView avatar;
    public TextView descriptionLbl;
    public ImageView itemImage;

    protected GTNotification item;
    protected OnNotificationClickListener clickListener;

    public NotificationViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        setupViews();
        safelyBindViews();
    }

    public void setItem(GTNotification notification) {
        this.item = notification;

        unreadIndicator.setVisibility(item.isRead ? View.INVISIBLE : View.VISIBLE);
        dateField.setText(DateUtils.getRelativeTimeSpanString(item.date.getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));
    }

    public GTNotification getItem() {
        return item;
    }

    public void setClickListener(OnNotificationClickListener listener) {
        this.clickListener = listener;
    }

    public void loadAvatar(GTUser user) {
        int p = R.drawable.default_avatar;
        if (user.hasAvatar())
            Picasso.with(avatar.getContext()).load(user.avatar.thumbnail).error(p).into(avatar);
        else
            Picasso.with(avatar.getContext()).load(p).placeholder(p).into(avatar);
    }

    public void loadStreamable(GTStreamable streamable) {
        Picasso.with(itemImage.getContext()).load(streamable.asset.thumbnail).into(itemImage);
    }

    // Setup

    protected void setupViews() {
        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (clickListener != null)
                    clickListener.onRowSelected(item, getAdapterPosition());
            }
        });
    }

    private void safelyBindViews() {
        if (itemView.findViewById(R.id.descriptionLbl) != null) descriptionLbl = (TextView) itemView.findViewById(R.id.descriptionLbl);
        if (itemView.findViewById(R.id.itemImage) != null) itemImage = (ImageView) itemView.findViewById(R.id.itemImage);
    }
}
