package com.graffitab.ui.activities.splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.graffitab.R;
import com.graffitab.graffitabsdk.config.GTConfig;
import com.graffitab.graffitabsdk.config.GTSDKConfig;
import com.graffitab.graffitabsdk.dagger.DaggerInjector;
import com.graffitab.graffitabsdk.managers.api.GTApiManager;
import com.graffitab.graffitabsdk.managers.api.GTUserManager;
import com.graffitab.ui.activities.home.HomeActivity;
import com.graffitab.ui.activities.login.LoginActivity;
import com.graffitab.utils.Utils;
import com.graffitab.utils.activity.ActivityUtils;
import com.graffitab.utils.display.BitmapUtils;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

/**
 * Created by georgichristov on 11/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class SplashActivity extends AppCompatActivity {

    @Inject
    GTUserManager gtUserManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUtils.enableFullScreen(this);
        ActivityUtils.hideActionBar(this);

        setContentView(R.layout.activity_splash);

        setupSDK();
        setupBackgroundImage();

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
        if (isLoggedIn()) {
            showHomeScreen();
        } else {
            showLoginScreen();
        }
    }

    // Setup
    private void setupSDK() {
        GTConfig config = GTConfig.defaultConfig();
        config.logEnabled = true;
        // Put in a common place?
        DaggerInjector.get(getApplication(), config).inject(this);
    }

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

    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("LOGIN_STATUS",Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("loggedIn", false);
        //return false;
    }
}
