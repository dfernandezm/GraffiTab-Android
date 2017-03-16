package com.graffitab.ui.adapters.followersactivity.viewholders;

import android.view.View;
import butterknife.ButterKnife;
import com.graffitabsdk.model.activity.GTActivityContainer;

/**
 * Created by georgichristov on 17/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class LikeFollowersActivityViewHolder extends ActivityContainerViewHolder {

    public LikeFollowersActivityViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setItem(GTActivityContainer activityContainer) {
        super.setItem(activityContainer);

        //actionLbl.setText(itemView.getContext().getString(R.string.notifications_like, item.liker.fullName()));

        //loadAvatar(item.liker);
        //loadStreamable(item.likedStreamable);
    }
}
