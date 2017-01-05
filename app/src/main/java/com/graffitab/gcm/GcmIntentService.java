package com.graffitab.gcm;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.graffitab.R;

/**
 * Created by georgichristov on 05/01/2017
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class GcmIntentService extends GcmListenerService {

    @Override
    public void onMessageReceived(String s, Bundle bundle) {
        Log.i(getClass().getSimpleName(), "GCM message received");
        String message = bundle.getString("message");
        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message);
        notificationManager.notify(1, mBuilder.build());
    }
}
