package com.graffitab.ui.activities.home.me.edit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitab.ui.activities.custom.CameraUtilsActivity;
import com.graffitab.ui.activities.home.me.PrivateStreamablesActivity;
import com.graffitab.utils.activity.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by georgichristov on 01/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class EditProfileActivity extends CameraUtilsActivity {

    @BindView(R.id.avatar) ImageView avatar;
    @BindView(R.id.cover) ImageView cover;
    @BindView(R.id.firstName) TextView firstName;
    @BindView(R.id.lastName) TextView lastName;
    @BindView(R.id.email) TextView email;
    @BindView(R.id.about) TextView about;
    @BindView(R.id.website) TextView website;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_edit);
        ButterKnife.bind(this);

        setupTopBar();
    }

    @OnClick(R.id.avatarLayout)
    public void onClickAvatar(View view) {
        showImagePicker(avatar);
    }

    @OnClick(R.id.coverLayout)
    public void onClickCover(View view) {
        showImagePicker(cover);
    }

    @OnClick(R.id.changePasswordLayout)
    public void onClickChangePassword(View view) {

    }

    @OnClick(R.id.privateLayout)
    public void onClickPrivate(View view) {
        startActivity(new Intent(this, PrivateStreamablesActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        ActivityUtils.colorMenu(this, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        else if (item.getItemId() == R.id.action_save) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Pair<Integer, Integer> calculateAspectRatio(View targetView) {
        if (targetView == avatar)
            return super.calculateAspectRatio(targetView);

        int ratioW = 5;
        int ratioH = 3;
        return new Pair<>(ratioW, ratioH);
    }

    // Setup

    private void setupTopBar() {
        getSupportActionBar().setTitle(getString(R.string.edit_profile));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
