package com.graffitab.application;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.graffitabsdk.config.GTSDK;

import java.util.Locale;

import static android.content.Intent.ACTION_LOCALE_CHANGED;

/**
 * Created by georgichristov on 06/07/16.
 */
public class MyApplication extends Application {

    private static Application instance = null;

    private MyBroadcastReceiver receiver;

    public static Application getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        // Initialize the SDK before executing any other operations.
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        setupBroadcastReceivers();
    }

    // Setup

    private void setupBroadcastReceivers() {
        receiver = new MyBroadcastReceiver();
        registerReceiver(receiver, new IntentFilter(ACTION_LOCALE_CHANGED));
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == ACTION_LOCALE_CHANGED) {
                GTSDK.setLanguage(Locale.getDefault().getLanguage());
            }
        }
    }
}
