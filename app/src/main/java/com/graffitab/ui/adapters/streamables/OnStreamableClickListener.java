package com.graffitab.ui.adapters.streamables;

import com.graffitab.ui.adapters.streamables.viewholders.StreamableViewHolder;
import com.graffitabsdk.model.GTStreamable;
import com.graffitabsdk.model.GTUser;

/**
 * Created by georgichristov on 26/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public interface OnStreamableClickListener {

    void onRowSelected(GTStreamable streamable);
    void onOpenOwnerProfile(GTStreamable streamable, GTUser owner);
    void onOpenLikes(GTStreamable streamable);
    void onOpenComments(GTStreamable streamable);
    void onShare(GTStreamable streamable);
    void onToggleLike(GTStreamable streamable, StreamableViewHolder holder);
}
