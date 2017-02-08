package com.graffitab.application;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.graffitab.config.AppConfig;
import com.graffitab.managers.GTLocationManager;
import com.graffitab.permissions.GTPermissions;
import com.graffitabsdk.sdk.GTSDK;

import java.lang.ref.WeakReference;
import java.util.Locale;

import static android.content.Intent.ACTION_LOCALE_CHANGED;

/**
 * Created by georgichristov on 12/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class MyApplication extends Application {

    private static WeakReference<Application> instance = null;
    private MyBroadcastReceiver receiver;

    public static Application getInstance() {
        return instance != null ? instance.get() : null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = new WeakReference<Application>(this);

        // Initialize the SDK before executing any other operations.
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        AppConfig.configuration.configureApp();

        setupBroadcastReceivers();
        setupLocationManager();
    }

    // Setup

    private void setupBroadcastReceivers() {
        receiver = new MyBroadcastReceiver();
        registerReceiver(receiver, new IntentFilter(ACTION_LOCALE_CHANGED));
    }

    private void setupLocationManager() {
        if (GTPermissions.manager.hasPermission(this, GTPermissions.PermissionType.LOCATION)) // Start location updates only if we have permission.
            GTLocationManager.sharedInstance.startLocationUpdates();
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_LOCALE_CHANGED))
                GTSDK.setLanguage(Locale.getDefault().getLanguage()); // Set the SDK language when the device language changes.
        }
    }
}
