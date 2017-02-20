package com.graffitab.ui.activities.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.graffitab.R;
import com.graffitab.settings.Settings;
import com.graffitab.ui.activities.home.HomeActivity;
import com.graffitab.ui.activities.login.LoginActivity;
import com.graffitab.ui.activities.onboard.OnboardActivity;
import com.graffitab.utils.Utils;
import com.graffitab.utils.activity.ActivityUtils;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.network.service.user.response.GTUserResponse;
import com.graffitabsdk.sdk.GTSDK;

import butterknife.ButterKnife;

/**
 * Created by georgichristov on 11/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class SplashActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ONBOARDING = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUtils.hideActionBar(this);

        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        Utils.runWithDelay(new Runnable() {

            @Override
            public void run() {
                checkOnboarding();
            }
        }, 300);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ONBOARDING) {
            Settings.settings.setShowedOnboarding(true);
            checkLoginStatus();
        }
    }

    private void showLoginScreen() {
        startActivity(new Intent(getBaseContext(), LoginActivity.class));
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void showHomeScreen() {
        startActivity(new Intent(getBaseContext(), HomeActivity.class));
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    // Login

    private void checkOnboarding() {
        if (!Settings.settings.showedOnboarding()) {
            startActivityForResult(new Intent(this, OnboardActivity.class), REQUEST_CODE_ONBOARDING);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        else
            checkLoginStatus();
    }

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
}
