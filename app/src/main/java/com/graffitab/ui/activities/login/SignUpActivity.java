package com.graffitab.ui.activities.login;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.ui.activities.home.WebActivity;
import com.graffitab.ui.dialog.DialogBuilder;
import com.graffitab.ui.dialog.TaskDialog;
import com.graffitab.ui.dialog.handlers.OnOkHandler;
import com.graffitab.utils.activity.ActivityUtils;
import com.graffitab.utils.image.BitmapUtils;
import com.graffitab.utils.input.InputValidator;
import com.graffitab.utils.text.TextUtils;
import com.graffitabsdk.config.GTSDK;
import com.graffitabsdk.network.common.GTResponse;
import com.graffitabsdk.network.common.GTResponseHandler;
import com.graffitabsdk.network.common.ResultCode;
import com.rengwuxian.materialedittext.MaterialEditText;

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
        Intent i = new Intent(this, WebActivity.class);
        i.putExtra(Constants.EXTRA_HTML_FILE, "terms.html");
        i.putExtra(Constants.EXTRA_TITLE, getString(R.string.sign_up_terms_of_use));
        startActivity(i);
    }

    @OnClick(R.id.closeBtn)
    public void onClickClose(View view) {
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
            TaskDialog.getInstance().showDialog(null, this, null);

            GTSDK.getUserManager().register(fn, ln, em, un, pw, new GTResponseHandler<String>() {

                @Override
                public void onSuccess(GTResponse<String> responseObject) {
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
                public void onFailure(GTResponse<String> responseObject) {
                    Log.e(getClass().getSimpleName(), "Failed to register");
                    TaskDialog.getInstance().hideDialog();

                    DialogBuilder.buildOKDialog(SignUpActivity.this, getString(R.string.app_name), responseObject.getResultDetail());
                }
            });
        }
    }

    // Setup

    private void setupTextFields() {
        TextUtils.colorTextViewSubstring(terms, getString(R.string.sign_up_terms_of_use), Color.parseColor("#ddffffff"));
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
