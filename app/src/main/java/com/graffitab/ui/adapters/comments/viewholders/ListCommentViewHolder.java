package com.graffitab.ui.adapters.comments.viewholders;

import android.view.View;

import com.graffitabsdk.model.GTComment;

import butterknife.ButterKnife;

/**
 * Created by georgichristov on 09/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class ListCommentViewHolder extends CommentViewHolder {

    public ListCommentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setItem(GTComment comment) {
        super.setItem(comment);
    }
}
