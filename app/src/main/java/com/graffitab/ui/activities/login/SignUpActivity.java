package com.graffitab.ui.activities.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.ui.activities.home.WebActivity;
import com.graffitab.ui.dialog.DialogBuilder;
import com.graffitab.ui.dialog.TaskDialog;
import com.graffitab.ui.dialog.OnOkHandler;
import com.graffitab.utils.activity.ActivityUtils;
import com.graffitab.utils.api.ApiUtils;
import com.graffitab.utils.input.InputValidator;
import com.graffitab.utils.input.KeyboardUtils;
import com.graffitab.utils.text.TextUtils;
import com.graffitabsdk.network.common.result.GTActionCompleteResult;
import com.graffitabsdk.sdk.GTSDK;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by georgichristov on 11/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.firstName) MaterialEditText firstname;
    @BindView(R.id.lastName) MaterialEditText lastname;
    @BindView(R.id.email) MaterialEditText email;
    @BindView(R.id.username) MaterialEditText username;
    @BindView(R.id.password) MaterialEditText password;
    @BindView(R.id.confirmPassword) MaterialEditText confirmPassword;
    @BindView(R.id.terms) TextView terms;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUtils.enableFullScreen(this);
        ActivityUtils.hideActionBar(this);
        ActivityUtils.setOrientation(this);

        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        setupBackgroundImage();
        setupTextFields();
    }

    @OnClick(R.id.terms)
    public void onClickTerms(View view) {
        KeyboardUtils.hideKeyboard(this);
        Intent i = new Intent(this, WebActivity.class);
        i.putExtra(Constants.EXTRA_HTML_FILE, "terms.html");
        i.putExtra(Constants.EXTRA_TITLE, getString(R.string.sign_up_terms_of_use));
        startActivity(i);
    }

    @OnClick(R.id.closeBtn)
    public void onClickClose(View view) {
        KeyboardUtils.hideKeyboard(this);
        finish();
    }

    @OnClick(R.id.signUpBtn)
    public void onClickSignUp(View view) {
        Log.i(getClass().getSimpleName(), "Sign up");
        String fn = firstname.getText().toString();
        String ln = lastname.getText().toString();
        String em = email.getText().toString();
        String un = username.getText().toString();
        String pw = password.getText().toString();
        String cpw = confirmPassword.getText().toString();

        if (InputValidator.validateSignUp(this, fn, ln, em, un, pw, cpw)) {
            KeyboardUtils.hideKeyboard(this);
            TaskDialog.getInstance().showProcessingDialog(this);

            GTSDK.getUserManager().register(fn, ln, em, un, pw, new GTResponseHandler<GTActionCompleteResult>() {

                @Override
                public void onSuccess(GTResponse<GTActionCompleteResult> responseObject) {
                    Log.i(getClass().getSimpleName(), "Successfully registered");
                    TaskDialog.getInstance().hideDialog();
                    DialogBuilder.buildOKDialog(SignUpActivity.this, getString(R.string.sign_up_confirmation_title), getString(R.string.sign_up_confirmation_detail), new OnOkHandler() {

                        @Override
                        public void onClickOk() {
                            finish();
                        }
                    });
                }

                @Override
                public void onFailure(GTResponse<GTActionCompleteResult> responseObject) {
                    Log.e(getClass().getSimpleName(), "Failed to register");
                    TaskDialog.getInstance().hideDialog();
                    DialogBuilder.buildAPIErrorDialog(SignUpActivity.this, getString(R.string.app_name), ApiUtils.localizedErrorReason(responseObject), true, responseObject.getResultCode());
                }
            });
        }
    }

    // Setup

    private void setupTextFields() {
        TextUtils.colorTextViewSubstring(terms, getString(R.string.sign_up_terms_of_use), Color.parseColor("#ddffffff"));
    }

    private void setupBackgroundImage() {
        ActivityUtils.setupBackgroundImage(this, R.drawable.login_full, R.id.background);
    }
}
