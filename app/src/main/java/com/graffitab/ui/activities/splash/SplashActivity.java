package com.graffitab.ui.activities.splash;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.graffitab.R;
import com.graffitab.config.AppConfig;
import com.graffitab.ui.activities.home.HomeActivity;
import com.graffitab.ui.activities.login.LoginActivity;
import com.graffitab.utils.Utils;
import com.graffitab.utils.activity.ActivityUtils;
import com.graffitab.utils.display.BitmapUtils;
import com.graffitabsdk.config.GTSDK;

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
        overridePendingTransition(R.anim.slow_fade_in, R.anim.fade_out);
    }

    private void showHomeScreen() {
        startActivity(new Intent(getBaseContext(), HomeActivity.class));
        finish();
        overridePendingTransition(R.anim.slow_fade_in, R.anim.fade_out);
    }

    // Login

    private void checkLoginStatus() {
        if (GTSDK.getAccountManager().isUserLoggedIn()) {
            showHomeScreen();
        }
        else
            showLoginScreen();
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
