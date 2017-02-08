package com.graffitab.managers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.graffitab.R;
import com.graffitabsdk.network.common.result.GTActionCompleteResult;
import com.graffitabsdk.sdk.GTSDK;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;

/**
 * Created by georgichristov on 05/01/2017
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class GTGcmManager {

    public static final GTGcmManager sharedInstance = new GTGcmManager();

    public void refreshGcmToken(final Context context) {
        Log.i(getClass().getSimpleName(), "About to refresh GCM token");
        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {
                Log.i(getClass().getSimpleName(), "Refreshing GCM token...");
                try {
                    InstanceID instanceID = InstanceID.getInstance(context);
                    String token = instanceID.getToken(context.getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    Log.i(getClass().getSimpleName(), "GCM token refreshed.\nToken: " + token);

                    GTSDK.getMeManager().linkDevice(token, new GTResponseHandler<GTActionCompleteResult>() {

                        @Override
                        public void onSuccess(GTResponse<GTActionCompleteResult> gtResponse) {
                            Log.i(getClass().getSimpleName(), "GCM token linked");
                        }

                        @Override
                        public void onFailure(GTResponse<GTActionCompleteResult> gtResponse) {
                            Log.i(getClass().getSimpleName(), "GCM token could not be linked");
                        }
                    });
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Failed to refresh GCM token", e);
                }
            }
        });
    }

    public void unregisterToken(final Context context) {
        Log.i(getClass().getSimpleName(), "About to unregister GCM token");
        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    InstanceID instanceID = InstanceID.getInstance(context);
                    String token = instanceID.getToken(context.getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    Log.i(getClass().getSimpleName(), "Unregistering GCM token " + token);
                    GTSDK.getMeManager().unlinkDevice(token, new GTResponseHandler<GTActionCompleteResult>() {

                        @Override
                        public void onSuccess(GTResponse<GTActionCompleteResult> gtResponse) {
                            Log.i(getClass().getSimpleName(), "GCM token unlinked");
                        }

                        @Override
                        public void onFailure(GTResponse<GTActionCompleteResult> gtResponse) {
                            Log.i(getClass().getSimpleName(), "GCM token could not be unlinked");
                        }
                    });
                    instanceID.deleteInstanceID();
                    Log.i(getClass().getSimpleName(), "GCM token unregistered");
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Failed to unregister GCM token", e);
                }
            }
        });
    }
}
