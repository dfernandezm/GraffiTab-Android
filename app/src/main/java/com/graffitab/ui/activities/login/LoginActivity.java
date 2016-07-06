package com.graffitab.ui.activities.login;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitab.ui.activities.custom.facebook.FacebookUtilsActivity;
import com.graffitab.utils.TextUtils;
import com.graffitab.utils.activity.ActivityUtils;
import com.graffitab.utils.display.BitmapUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by georgichristov on 06/07/16.
 */
public class LoginActivity extends FacebookUtilsActivity {

    private EditText usernameField;
    private EditText passwordField;
    private TextView signUpField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUtils.enableFullScreen(this);
        ActivityUtils.hideActionBar(this);
        ActivityUtils.setOrientation(this);

        setContentView(R.layout.activity_login);

        usernameField = (EditText) findViewById(R.id.username);
        passwordField = (EditText) findViewById(R.id.password);
        signUpField = (TextView) findViewById(R.id.signUp);

        setupBackgroundImage();
        setupTextFields();
    }

    public void onClickLogin(View view) {
        Log.i(getClass().getSimpleName(), "Logging in");
    }

    public void onClickFacebookLogin(View view) {
        Log.i(getClass().getSimpleName(), "Logging in with Facebook");

        facebookLogin(true, new FacebookLoginCallback() {

            @Override
            public void loginComplete() {
                Log.i(getClass().getSimpleName(), "Facebook login complete. Checking user profile");
            }
        });
    }

    public void onClickResetPassword(View view) {
        Log.i(getClass().getSimpleName(), "Resetting password");
    }

    public void onClickSignUp(View view) {
        Log.i(getClass().getSimpleName(), "Signing up");
    }

    // Setup

    private void setupTextFields() {
        TextUtils.colorTextViewSubstring(signUpField, getString(R.string.login_sign_up), Color.parseColor("#ddffffff"));
    }

    private void setupBackgroundImage() {
        int resId = R.drawable.login;

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
