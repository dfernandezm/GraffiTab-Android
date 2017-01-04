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

    void onRowSelected(GTStreamable streamable, int adapterPosition);
    void onOpenOwnerProfile(GTStreamable streamable, GTUser owner, int adapterPosition);
    void onOpenLikes(GTStreamable streamable, int adapterPosition);
    void onOpenComments(GTStreamable streamable, int adapterPosition);
    void onShare(GTStreamable streamable, int adapterPosition);
    void onToggleLike(GTStreamable streamable, StreamableViewHolder holder, int adapterPosition);
}
