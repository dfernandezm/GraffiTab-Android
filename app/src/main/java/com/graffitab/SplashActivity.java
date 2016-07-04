package com.graffitab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.graffitab.graffitabsdk.config.GTConfig;
import com.graffitab.graffitabsdk.config.GTSDKConfig;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        setupSDK();
    }

    // Setup

    private void setupSDK() {
        GTConfig config = GTConfig.defaultConfig();
        config.logEnabled = true;

        GTSDKConfig.sharedInstance.setConfig(config);
    }
}
