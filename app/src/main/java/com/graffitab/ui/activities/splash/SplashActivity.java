package com.graffitab.ui.activities.splash;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.graffitab.R;
import com.graffitab.config.AppConfig;
import com.graffitab.ui.activities.home.HomeActivity;
import com.graffitab.ui.activities.login.LoginActivity;
import com.graffitab.utils.Utils;
import com.graffitab.utils.activity.ActivityUtils;
import com.graffitab.utils.image.BitmapUtils;
import com.graffitabsdk.config.GTSDK;
import com.graffitabsdk.model.GTUser;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.network.service.user.response.GTUserResponse;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by georgichristov on 11/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUtils.enableFullScreen(this);
        ActivityUtils.hideActionBar(this);

        setContentView(R.layout.activity_splash);

        setupBackgroundImage();

        configureApp();

        Utils.runWithDelay(new Runnable() {

            @Override
            public void run() {
                checkLoginStatus();
            }
        }, 1000);
    }

    private void showLoginScreen() {
        startActivity(new Intent(getBaseContext(), LoginActivity.class));
        finish();
//        overridePendingTransition(R.anim.slow_fade_in, R.anim.fade_out);
    }

    private void showHomeScreen() {
        startActivity(new Intent(getBaseContext(), HomeActivity.class));
        finish();
//        overridePendingTransition(R.anim.slow_fade_in, R.anim.fade_out);
    }

    // Login

    private void checkLoginStatus() {
        if (GTSDK.getAccountManager().isUserLoggedIn()) {
            Log.i(getClass().getSimpleName(), "User logged in");
            if (Utils.isNetworkConnected(this)) { // Online, so refresh profile
                Log.i(getClass().getSimpleName(), "Refreshing profile");
                GTSDK.getMeManager().getMyFullProfile(true, new GTResponseHandler<GTUserResponse>() {

                    @Override
                    public void onSuccess(GTResponse<GTUserResponse> gtResponse) {
                        Log.i(getClass().getSimpleName(), "Profile refreshed. Showing Home screen");
                        showHomeScreen();
                    }

                    @Override
                    public void onFailure(GTResponse<GTUserResponse> responseObject) {
                        Log.e(getClass().getSimpleName(), "Failed to refresh profile. Showing Home screen");
                        showHomeScreen();
                    }

                    @Override
                    public void onCache(GTResponse<GTUserResponse> gtResponse) {
                        Log.i(getClass().getSimpleName(), "User is cached\nUser: " + gtResponse.getObject().user);
                    }
                });
            }
            else { // Offline, so show home screen
                Log.i(getClass().getSimpleName(), "No network connection. Showing Home screen");
                showHomeScreen();
            }
        }
        else {
            Log.i(getClass().getSimpleName(), "User not logged in. Showing login screen");
            showLoginScreen();
        }
    }

    // Configure

    private void configureApp() {
        AppConfig.configuration.configureApp();
    }

    // Setup

    private void setupBackgroundImage() {
        int resId = R.drawable.splash;

        InputStream is = getResources().openRawResource(resId);
        byte[] b;

        try {
            b = BitmapUtils.getBytes(is);
            Drawable drawable = new BitmapDrawable(getResources(), BitmapUtils.decodeSampledBitmapFromBytesForCurrentScreen(b, getBaseContext()));

            ImageView background = (ImageView) findViewById(R.id.background);
            background.setImageDrawable(drawable);
        } catch (IOException e) {}
    }
}
