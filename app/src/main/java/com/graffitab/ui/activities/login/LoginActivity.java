package com.graffitab.ui.activities.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitab.ui.activities.custom.facebook.FacebookUtilsActivity;
import com.graffitab.ui.activities.home.HomeActivity;
import com.graffitab.ui.dialog.DialogBuilder;
import com.graffitab.ui.dialog.TaskDialog;
import com.graffitab.utils.activity.ActivityUtils;
import com.graffitab.utils.api.ApiUtils;
import com.graffitab.utils.input.InputValidator;
import com.graffitab.utils.input.KeyboardUtils;
import com.graffitab.utils.text.TextUtils;
import com.graffitabsdk.config.GTSDK;
import com.graffitabsdk.network.common.GTResultCode;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.network.service.user.response.GTUserResponse;

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
            KeyboardUtils.hideKeyboard(this);
            TaskDialog.getInstance().showDialog(null, this, null);

            GTSDK.getUserManager().login(un, pw, new GTResponseHandler<GTUserResponse>() {

                @Override
                public void onSuccess(GTResponse<GTUserResponse> responseObject) {
                    Log.i(getClass().getSimpleName(), "Logged in");
                    refreshCurrentUserAndFinishLogin();
                }

                @Override
                public void onFailure(GTResponse<GTUserResponse> responseObject) {
                    Log.e(getClass().getSimpleName(), "Failed to login");
                    TaskDialog.getInstance().hideDialog();

                    if (responseObject.getResultCode() == GTResultCode.USER_NOT_LOGGED_IN) {
                        DialogBuilder.buildAPIErrorDialog(LoginActivity.this, getString(R.string.app_name), getString(R.string.login_error_credentials), true, responseObject.getResultCode());
                        return;
                    }
                    DialogBuilder.buildAPIErrorDialog(LoginActivity.this, getString(R.string.app_name), ApiUtils.localizedErrorReason(responseObject), true, responseObject.getResultCode());
                }
            });
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
//        overridePendingTransition(R.anim.slow_fade_in, R.anim.fade_out);
    }

    private void refreshCurrentUserAndFinishLogin() {
        Log.i(getClass().getSimpleName(), "Refreshing profile");
        GTSDK.getMeManager().getMe(false, new GTResponseHandler<GTUserResponse>() {

            @Override
            public void onSuccess(GTResponse<GTUserResponse> gtResponse) {
                Log.i(getClass().getSimpleName(), "Profile refreshed. Showing Home screen");
                TaskDialog.getInstance().hideDialog();
                showHomeScreen();
            }

            @Override
            public void onFailure(GTResponse<GTUserResponse> responseObject) {
                Log.e(getClass().getSimpleName(), "Failed to refresh profile. Showing Home screen");
                TaskDialog.getInstance().hideDialog();
                DialogBuilder.buildAPIErrorDialog(LoginActivity.this, getString(R.string.app_name), ApiUtils.localizedErrorReason(responseObject), true, responseObject.getResultCode());
            }
        });
    }

    // Setup

    private void setupTextFields() {
        TextUtils.colorTextViewSubstring(signUpField, getString(R.string.login_sign_up), Color.parseColor("#ddffffff"));
    }

    private void setupBackgroundImage() {
        ActivityUtils.setupBackgroundImage(this, R.drawable.login_full, R.id.background);
    }
}
