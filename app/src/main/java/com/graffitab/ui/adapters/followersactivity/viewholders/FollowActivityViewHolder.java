package com.graffitab.ui.adapters.followersactivity.viewholders;

import android.view.View;
import butterknife.ButterKnife;
import com.graffitab.R;
import com.graffitab.ui.adapters.followersactivity.adapters.FollowersActivityItemsAdapter;
import com.graffitabsdk.model.GTUser;
import com.graffitabsdk.model.activity.GTActivity;
import com.graffitabsdk.model.activity.GTActivityContainer;

/**
 * Created by georgichristov on 17/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class FollowActivityViewHolder extends ActivityContainerViewHolder<GTUser> {

    public FollowActivityViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setItem(final GTActivityContainer activityContainer) {
        super.setItem(activityContainer);

        if (activityContainer.isSingle()) {
            GTUser followed = item.activities.get(0).followed;
            actionLbl.setText(itemView.getContext().getString(R.string.followers_activity_follow_one, item.user.fullName(), followed.fullName()));
            loadFollowedAvatar(followed);
        } else {
            actionLbl.setText(itemView.getContext().getString(R.string.followers_activity_follow_multiple, item.user.fullName(),
                    activityContainer.activities.size()));
            followersActivityItemsAdapter.setItems(getUsersFromActivityContainer(item));
            followersActivityItemsAdapter.setImageExtractor(new FollowersActivityItemsAdapter.ImageExtractor<GTUser>() {
                @Override
                public String getImageUrl(GTUser activityDetailItem) {
                    return activityDetailItem.avatar.thumbnail;
                }
            });
            followersActivityItemsAdapter.notifyDataSetChanged();
        }

        loadAvatar(item.user);

    }

    @Override
    public GTUser extractUser(GTActivity gtActivity) {
        return gtActivity.followed;
    }
}
