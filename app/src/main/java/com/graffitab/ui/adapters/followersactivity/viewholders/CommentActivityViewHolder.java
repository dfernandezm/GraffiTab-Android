package com.graffitab.ui.adapters.followersactivity.viewholders;

import android.view.View;
import butterknife.ButterKnife;
import com.graffitabsdk.model.activity.GTActivityContainer;

/**
 * Created by georgichristov on 17/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class CommentActivityViewHolder extends ActivityContainerViewHolder {

    public CommentActivityViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setItem(GTActivityContainer gtActivityContainer) {
        super.setItem(gtActivityContainer);

//        actionLbl.setText(itemView.getContext().getString(R.string.notifications_comment, item.commenter.fullName()));
//        descriptionLbl.setText(item.comment.text);
//
//        loadAvatar(item.commenter);
//        loadStreamable(item.commentedStreamable);
    }
}
