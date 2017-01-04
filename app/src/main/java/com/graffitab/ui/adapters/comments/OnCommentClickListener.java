package com.graffitab.ui.adapters.comments;

import com.graffitabsdk.model.GTComment;
import com.graffitabsdk.model.GTUser;

/**
 * Created by georgichristov on 26/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public interface OnCommentClickListener {

    void onRowSelected(GTComment comment, int adapterPosition);
    void onRowLongSelected(GTComment comment, int adapterPosition);
    void onOpenCommenterProfile(GTComment comment, GTUser commenter, int adapterPosition);
    void onOpenHashtag(GTComment comment, String hashtag, int adapterPosition);
    void onOpenMention(GTComment comment, String mention, int adapterPosition);
    void onOpenLink(GTComment comment, String url, int adapterPosition);
}
