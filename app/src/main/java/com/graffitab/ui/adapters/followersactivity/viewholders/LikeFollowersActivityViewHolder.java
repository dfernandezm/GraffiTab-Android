package com.graffitab.ui.adapters.followersactivity.viewholders;

import android.view.View;
import android.widget.ImageView;
import butterknife.ButterKnife;
import com.graffitab.R;
import com.graffitab.ui.adapters.followersactivity.FollowersActivityItemsAdapter;
import com.graffitabsdk.model.GTStreamable;
import com.graffitabsdk.model.activity.GTActivity;
import com.graffitabsdk.model.activity.GTActivityContainer;
import com.squareup.picasso.Picasso;

/**
 * Created by georgichristov on 17/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class LikeFollowersActivityViewHolder extends ActivityContainerViewHolder<GTStreamable> {

    public LikeFollowersActivityViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setItem(final GTActivityContainer activityContainer) {
        super.setItem(activityContainer);

        if (activityContainer.isSingle()) {
            actionLbl.setText(itemView.getContext().getString(R.string.followers_activity_like_one, item.user.fullName()));
            loadStreamable(item.activities.get(0).likedStreamable);
        } else {
            actionLbl.setText(itemView.getContext().getString(R.string.followers_activity_like_multiple, item.user.fullName(),
                    activityContainer.activities.size()));
            followersActivityItemsAdapter.setItems(getStreamablesFromActivityContainer(item));
            followersActivityItemsAdapter.setImageLoader(new FollowersActivityItemsAdapter.ImageLoader<GTStreamable>() {
                @Override
                public void loadImage(ImageView imageView, GTStreamable activityDetailItem) {
                    Picasso.with(imageView.getContext()).load(activityDetailItem.asset.thumbnail).into(imageView);
                }
            });
            followersActivityItemsAdapter.notifyDataSetChanged();
        }

        loadAvatar(item.user);
    }

    @Override
    protected GTStreamable extractStreamable(GTActivity gtActivity) {
        return gtActivity.likedStreamable;
    }
}
