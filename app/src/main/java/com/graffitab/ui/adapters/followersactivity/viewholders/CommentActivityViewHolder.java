package com.graffitab.ui.adapters.followersactivity.viewholders;

import android.view.View;
import android.widget.ImageView;

import com.graffitab.R;
import com.graffitab.ui.adapters.followersactivity.FollowersActivityItemsAdapter;
import com.graffitabsdk.model.GTStreamable;
import com.graffitabsdk.model.activity.GTActivity;
import com.graffitabsdk.model.activity.GTActivityContainer;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;

/**
 * Created by georgichristov on 17/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class CommentActivityViewHolder extends ActivityContainerViewHolder<GTStreamable> {

    public CommentActivityViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setItem(final GTActivityContainer activityContainer) {
        super.setItem(activityContainer);

        if (activityContainer.isSingle()) {
            actionLbl.setText(itemView.getContext().getString(R.string.followers_activity_comment_single, item.user.fullName()));
            descriptionLbl.setText(item.activities.get(0).comment.quotedText());
            loadStreamable(item.activities.get(0).commentedStreamable);
        } else {
            actionLbl.setText(itemView.getContext().getString(R.string.followers_activity_comment_multiple, item.user.fullName(),
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
        return gtActivity.commentedStreamable;
    }
}
