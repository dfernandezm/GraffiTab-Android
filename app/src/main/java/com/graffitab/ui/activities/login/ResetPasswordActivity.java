package com.graffitab.ui.activities.login;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.graffitab.R;
import com.graffitab.utils.activity.ActivityUtils;
import com.graffitab.utils.image.BitmapUtils;
import com.graffitab.utils.text.TextUtils;

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
public class ResetPasswordActivity extends AppCompatActivity {

    @BindView(R.id.email) EditText emailField;
    @BindView(R.id.resetBtn) Button resetBtn;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUtils.enableFullScreen(this);
        ActivityUtils.hideActionBar(this);
        ActivityUtils.setOrientation(this);

        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);

        setupBackgroundImage();
        setupTextViews();
    }

    @OnClick(R.id.closeBtn)
    public void onClickClose(View view) {
        finish();
    }

    @OnClick(R.id.resetBtn)
    public void onClickReset(View view) {
        Log.i(getClass().getSimpleName(), "Reset password");
        String em = emailField.getText().toString();

        if (TextUtils.isValidEmailAddress(em)) {
            finish();
        }
    }

    // Setup

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

    private void setupTextViews() {
        emailField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isValidEmailAddress(editable.toString())) {
                    resetBtn.setBackgroundResource(R.drawable.rounded_corner_password_reset_enabled);
                    resetBtn.setTextColor(Color.WHITE);
                }
                else {
                    resetBtn.setBackgroundResource(R.drawable.rounded_corner_password_reset_disabled);
                    resetBtn.setTextColor(Color.parseColor("#e0e0e0"));
                }
            }
        });
    }
}
