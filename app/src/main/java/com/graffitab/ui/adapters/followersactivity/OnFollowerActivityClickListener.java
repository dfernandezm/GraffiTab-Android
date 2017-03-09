package com.graffitab.ui.adapters.followersactivity;

import com.graffitabsdk.model.activity.GTActivityContainer;

/**
 * Created by georgichristov on 26/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public interface OnFollowerActivityClickListener {

    void onRowSelected(GTActivityContainer activityContainer, int adapterPosition);
}
