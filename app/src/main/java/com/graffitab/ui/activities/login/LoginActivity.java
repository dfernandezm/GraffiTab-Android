package com.graffitab.ui.activities.login;

import android.content.Intent;
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
import com.graffitab.ui.activities.home.HomeActivity;
import com.graffitab.utils.text.TextUtils;
import com.graffitab.utils.activity.ActivityUtils;
import com.graffitab.utils.display.BitmapUtils;
import com.graffitab.utils.input.InputValidator;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by georgichristov on 11/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class LoginActivity extends FacebookUtilsActivity {

    @BindView(R.id.username) EditText usernameField;
    @BindView(R.id.password) EditText passwordField;
    @BindView(R.id.signUp) TextView signUpField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUtils.enableFullScreen(this);
        ActivityUtils.hideActionBar(this);
        ActivityUtils.setOrientation(this);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        setupBackgroundImage();
        setupTextFields();
    }

    @OnClick(R.id.loginBtn)
    public void onClickLogin(View view) {
        Log.i(getClass().getSimpleName(), "Logging in");
        String un = usernameField.getText().toString();
        String pw = passwordField.getText().toString();

        if (InputValidator.validateLogin(this, un, pw)) {
//            TaskDialog.getInstance().showDialog(null, this, null);
            showHomeScreen();
        }
    }

    @OnClick(R.id.loginFacebookBtn)
    public void onClickFacebookLogin(View view) {
        Log.i(getClass().getSimpleName(), "Logging in with Facebook");

        facebookLogin(true, new FacebookLoginCallback() {

            @Override
            public void loginComplete() {
            Log.i(getClass().getSimpleName(), "Facebook login complete. Checking user profile");
            }
        });
    }

    @OnClick(R.id.forgottenPassword)
    public void onClickResetPassword(View view) {
        Log.i(getClass().getSimpleName(), "Resetting password");
        startActivity(new Intent(this, ResetPasswordActivity.class));
    }

    @OnClick(R.id.signUp)
    public void onClickSignUp(View view) {
        Log.i(getClass().getSimpleName(), "Signing up");
        startActivity(new Intent(this, SignUpActivity.class));
    }

    private void showHomeScreen() {
        startActivity(new Intent(getBaseContext(), HomeActivity.class));

        finish();

        overridePendingTransition(R.anim.slow_fade_in, R.anim.fade_out);
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
