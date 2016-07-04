package com.graffitab.ui.activities.splash;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.graffitab.R;
import com.graffitab.graffitabsdk.config.GTConfig;
import com.graffitab.graffitabsdk.config.GTSDKConfig;
import com.graffitab.utils.activity.ActivityUtils;
import com.graffitab.utils.display.BitmapUtils;

import java.io.IOException;
import java.io.InputStream;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUtils.enableFullScreen(this);
        ActivityUtils.hideActionBar(this);

        setContentView(R.layout.activity_splash);

        setupSDK();
        setupBackgroundImage();
    }

    // Setup

    private void setupSDK() {
        GTConfig config = GTConfig.defaultConfig();
        config.logEnabled = true;

        GTSDKConfig.sharedInstance.setConfig(config);
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
}
