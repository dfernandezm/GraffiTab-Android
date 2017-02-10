package com.graffitab.ui.activities.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.FacebookException;
import com.graffitab.R;
import com.graffitab.ui.activities.custom.facebook.FacebookUtilsActivity;
import com.graffitab.ui.activities.home.HomeActivity;
import com.graffitab.ui.dialog.DialogBuilder;
import com.graffitab.ui.dialog.OnYesNoInputHandler;
import com.graffitab.ui.dialog.TaskDialog;
import com.graffitab.utils.activity.ActivityUtils;
import com.graffitab.utils.api.ApiUtils;
import com.graffitab.utils.input.InputValidator;
import com.graffitab.utils.input.KeyboardUtils;
import com.graffitab.utils.text.TextUtils;
import com.graffitabsdk.model.GTExternalProvider;
import com.graffitabsdk.network.common.GTResultCode;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.network.common.result.GTActionCompleteResult;
import com.graffitabsdk.network.service.user.response.GTUserResponse;
import com.graffitabsdk.sdk.GTSDK;

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
        KeyboardUtils.hideKeyboard(this);
        login();
    }

    @OnClick(R.id.loginFacebookBtn)
    public void onClickFacebookLogin(View view) {
        KeyboardUtils.hideKeyboard(this);
        loginFacebook(true);
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

    // Login

    private void login() {
        Log.i(getClass().getSimpleName(), "Logging in");
        String un = usernameField.getText().toString();
        String pw = passwordField.getText().toString();

        if (InputValidator.validateLogin(this, un, pw)) {
            KeyboardUtils.hideKeyboard(this);
            TaskDialog.getInstance().showDialog(getString(R.string.other_processing), this, null);

            GTSDK.getUserManager().login(un, pw, new GTResponseHandler<GTUserResponse>() {

                @Override
                public void onSuccess(GTResponse<GTUserResponse> responseObject) {
                    if (usernameField == null) return; // View is destroyed.

                    Log.i(getClass().getSimpleName(), "Logged in");
                    refreshCurrentUserAndFinishLogin();
                }

                @Override
                public void onFailure(GTResponse<GTUserResponse> responseObject) {
                    if (usernameField == null) return; // View is destroyed.

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

    private void loginFacebook(boolean forceLogin) {
        Log.i(getClass().getSimpleName(), "Logging in with Facebook");

        getFacebookMe(forceLogin, new FacebookMeCallback() {

            @Override
            public void onProfileFetched(final String firstName, final String lastName, final String userId, final String email, final String accessToken) {
                Log.i(getClass().getSimpleName(), "First name: " + firstName + "\nLast name: " + lastName + "\nID: " + userId + "\nEmail: " + email);

                GTSDK.getUserManager().login(userId, accessToken, GTExternalProvider.GTExternalProviderType.FACEBOOK, new GTResponseHandler<GTUserResponse>() {

                    @Override
                    public void onSuccess(GTResponse<GTUserResponse> responseObject) {
                        if (usernameField == null) return; // View is destroyed.

                        Log.i(getClass().getSimpleName(), "Logged in with Facebook");
                        refreshCurrentUserAndFinishLogin();
                    }

                    @Override
                    public void onFailure(GTResponse<GTUserResponse> responseObject) {
                        if (usernameField == null) return; // View is destroyed.

                        Log.e(getClass().getSimpleName(), "Failed to login with Facebook");
                        TaskDialog.getInstance().hideDialog();

                        if (responseObject.getResultCode() == GTResultCode.USER_NOT_LOGGED_IN) { // This error means that thre isn't a user with those external credentials.
                            DialogBuilder.buildUsernameDialog(LoginActivity.this, new OnYesNoInputHandler() {

                                @Override
                                public void onClickYes(String value) {
                                    signUpWithFacebook(firstName, lastName, userId, email, accessToken, value);
                                }

                                @Override
                                public void onClickNo() {}
                            });
                            return;
                        }
                        DialogBuilder.buildAPIErrorDialog(LoginActivity.this, getString(R.string.app_name), ApiUtils.localizedErrorReason(responseObject), true, responseObject.getResultCode());
                    }
                });
            }

            @Override
            public void onCancel() {
                TaskDialog.getInstance().hideDialog();
                Log.i(getClass().getSimpleName(), "Facebook login cancelled");
            }

            @Override
            public void onError(FacebookException exception) {
                TaskDialog.getInstance().hideDialog();
                Log.e(getClass().getSimpleName(), "Failed to login with Facebook", exception);
            }
        });
    }

    // Sign up

    private void signUpWithFacebook(String firstName, String lastName, String userId, String email, String accessToken, String username) {
        TaskDialog.getInstance().showDialog(getString(R.string.other_processing), this, null);

        GTSDK.getUserManager().register(GTExternalProvider.GTExternalProviderType.FACEBOOK, userId, accessToken, firstName, lastName, email, username, new GTResponseHandler<GTActionCompleteResult>() {

            @Override
            public void onSuccess(GTResponse<GTActionCompleteResult> responseObject) {
                if (usernameField == null) return; // View is destroyed.

                Log.i(getClass().getSimpleName(), "Sign up successful. Attempting login");
                loginFacebook(false);
            }

            @Override
            public void onFailure(GTResponse<GTActionCompleteResult> responseObject) {
                if (usernameField == null) return; // View is destroyed.

                Log.e(getClass().getSimpleName(), "Failed to register with Facebook");
                TaskDialog.getInstance().hideDialog();

                DialogBuilder.buildAPIErrorDialog(LoginActivity.this, getString(R.string.app_name), ApiUtils.localizedErrorReason(responseObject), true, responseObject.getResultCode());
            }
        });
    }

    private void refreshCurrentUserAndFinishLogin() {
        Log.i(getClass().getSimpleName(), "Refreshing profile");
        GTSDK.getMeManager().getMyFullProfile(false, new GTResponseHandler<GTUserResponse>() {

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
