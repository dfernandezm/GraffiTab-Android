package com.graffitab.ui.adapters.followersactivity.viewholders;

import android.view.View;
import butterknife.ButterKnife;
import com.graffitab.R;
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

        if (activityContainer.isSingle()) {
            actionLbl.setText(itemView.getContext().getString(R.string.followers_activity_like_one, item.user.fullName()));
            loadStreamable(item.activities.get(0).likedStreamable);
        } else {
            actionLbl.setText(itemView.getContext().getString(R.string.followers_activity_like_multiple, item.user.fullName(),
                    activityContainer.activities.size()));
        }

        loadAvatar(item.user);

    }
}
