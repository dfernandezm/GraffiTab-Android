package com.graffitab.gcm;

import com.google.android.gms.iid.InstanceIDListenerService;
import com.graffitab.managers.GTGcmManager;

/**
 * Created by georgichristov on 05/01/2017
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class GcmIDListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        GTGcmManager.sharedInstance.refreshGcmToken(this);
    }
}
