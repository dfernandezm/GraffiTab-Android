package com.graffitab.ui.adapters.notifications;

import com.graffitabsdk.model.GTNotification;

/**
 * Created by georgichristov on 26/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public interface OnNotificationClickListener {

    void onRowSelected(GTNotification notification);
}
