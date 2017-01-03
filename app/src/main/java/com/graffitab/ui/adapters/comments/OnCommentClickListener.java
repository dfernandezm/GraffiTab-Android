package com.graffitab.ui.adapters.comments;

import com.graffitabsdk.model.GTComment;
import com.graffitabsdk.model.GTUser;

/**
 * Created by georgichristov on 26/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public interface OnCommentClickListener {

    void onRowSelected(GTComment comment);
    void onOpenCommenterProfile(GTComment comment, GTUser commenter);
    void onOpenHashtag(GTComment comment, String hashtag);
    void onOpenMention(GTComment comment, String mention);
    void onOpenLink(GTComment comment, String url);
}
