package com.graffitab.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.graffitab.R;
import com.graffitab.ui.activities.home.HomeActivity;
import com.graffitabsdk.model.GTNotification;

/**
 * Created by georgichristov on 05/01/2017
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class GcmIntentService extends GcmListenerService {

    @Override
    public void onMessageReceived(String s, Bundle bundle) {
        // Parse notification type.
        GTNotification.GTNotificationType type = parseNotificationType(bundle);
        if (type == null) return;

        // Parse notification message.
        String message = parseNotificationMessage(bundle, type);
        Log.i(getClass().getSimpleName(), "GCM message received:\nMessage: " + message + "\nType: " + type);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        // Show notification.
        Intent resultIntent = new Intent(this, HomeActivity.class);
        resultIntent.setAction(Long.toString(System.currentTimeMillis()));
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notifications_none_black_24dp)
                .setLargeIcon(bm)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(resultPendingIntent);

        notificationManager.notify(1, mBuilder.build());
    }

    private GTNotification.GTNotificationType parseNotificationType(Bundle bundle) {
        try {
            return GTNotification.GTNotificationType.valueOf(bundle.getString("type"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String parseNotificationMessage(Bundle bundle, GTNotification.GTNotificationType type) {
        String message = null;
        switch (type) {
            case FOLLOW: {
                String user = bundle.getString("followerName");
                message = getString(R.string.notifications_follow, user);
                break;
            }
            case WELCOME: {
                message = getString(R.string.notifications_welcome_description);
                break;
            }
            case MENTION: {
                String user = bundle.getString("mentionerName");
                message = getString(R.string.notifications_mention, user);
                break;
            }
            case LIKE: {
                String user = bundle.getString("likerName");
                message = getString(R.string.notifications_like, user);
                break;
            }
            case COMMENT: {
                String user = bundle.getString("commenterName");
                message = getString(R.string.notifications_comment, user);
                break;
            }
        }
        return message;
    }
}
