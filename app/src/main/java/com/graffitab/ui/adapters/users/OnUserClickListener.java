package com.graffitab.ui.adapters.users;

import com.graffitab.ui.adapters.users.viewholders.UserViewHolder;
import com.graffitabsdk.model.GTUser;

/**
 * Created by georgichristov on 26/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public interface OnUserClickListener {

    void onRowSelected(GTUser user);
    void onToggleFollow(GTUser user, UserViewHolder holder);
}
