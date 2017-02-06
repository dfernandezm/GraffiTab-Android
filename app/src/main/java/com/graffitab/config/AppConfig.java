package com.graffitab.config;

import com.graffitab.R;
import com.graffitab.application.MyApplication;
import com.graffitabsdk.sdk.GTConfig;
import com.graffitabsdk.sdk.GTSDK;
import com.instabug.library.Feature;
import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;

import java.util.Locale;

/**
 * Created by georgichristov on 06/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class AppConfig {

    public static final AppConfig configuration = new AppConfig();

    public final boolean isPlayStore = false;
    public final boolean useAnalytics = true;
    public final int maxUndoActions = 10;
    public final int onboardingFeedbackDaysTrigger = 2;

    public final int locationUpdateInterval = 20 * 1000; // 20 seconds.
    public final int locationRadius = 500 * 1000; // Distance in kilometers.
    public final int locationTimeout = 10; // Waiting time for location to become available for publishing.
    public final int mapInitialZoomLevel = 16;
    public final int mapRefreshRate = 3 * 1000;

    public final boolean logEnabled = true;
    public final boolean httpEnabled = true;
//    public final String customUrl = null;
    public final String customUrl = "dev.graffitab.com";
//    public final String customUrl = "localhost:8091";

    public void configureApp() {
        configureTestFramework();
        configureSDK();
        configureAnalytics();
        configureInstabug();
    }

    // GraffiTab SDK

    private void configureSDK() {
        GTConfig config = GTConfig.defaultConfig();
        config.logEnabled = logEnabled;
        config.httpsEnabled = httpEnabled;
        config.language = Locale.getDefault().getLanguage(); // Set language to whatever is the chosen device language.

        if (customUrl != null)
            config.domain = customUrl;

        GTSDK.init(MyApplication.getInstance(), config);
    }

    // DeployGate

    private void configureTestFramework() {
        if (!isPlayStore) {
            // No-op.
        }
    }

    // Google Analytics

    private void configureAnalytics() {
        if (useAnalytics) {
            // No-op.
        }
    }

    // Instabug

    private void configureInstabug() {
        new Instabug.Builder(MyApplication.getInstance(), MyApplication.getInstance().getString(R.string.instabug_token))
                .setInvocationEvent(InstabugInvocationEvent.SHAKE)
                .setIntroMessageEnabled(false)
                .setCrashReportingState(Feature.State.DISABLED) // Disbale crash reporting, as we have Google Analytics.
                .build();
    }
}
