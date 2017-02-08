package com.graffitab.permissions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
import com.graffitab.R;
import com.graffitab.constants.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by georgichristov on 03/02/2017
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class GTPermissionActivity extends AppCompatActivity {

    @BindView(R.id.title) TextView title;
    @BindView(R.id.image) ImageView image;
    @BindView(R.id.descriptionLbl) TextView descriptionLbl;
    @BindView(R.id.accessBtn) Button accessBtn;

    private GTPermissions.PermissionType permissionType;
    private String[] permissions;

    public static void openPermissionRequest(Activity context, GTPermissions.PermissionType permissionType, String[] permissions) {
        Intent i = new Intent(context, GTPermissionActivity.class);
        i.putExtra(Constants.EXTRA_PERMISSIONS, permissions);
        i.putExtra(Constants.EXTRA_PERMISSION_TYPE, permissionType);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_permission);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.get(Constants.EXTRA_PERMISSION_TYPE) != null && extras.get(Constants.EXTRA_PERMISSIONS) != null) {
            permissionType = (GTPermissions.PermissionType) extras.getSerializable(Constants.EXTRA_PERMISSION_TYPE);
            permissions = (String[]) extras.getSerializable(Constants.EXTRA_PERMISSIONS);
        }
        else {
            finish();
            return;
        }

        loadPermissionData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    @OnClick(R.id.accessBtn)
    public void onClickAccess(View view) {
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this, permissions, new PermissionsResultAction() {

            @Override
            public void onGranted() {
                GTPermissions.manager.permissionGranted();
                finish();
            }

            @Override
            public void onDenied(String permission) {
                GTPermissions.manager.permissionDenied();
                finish();
            }
        });
    }

    @OnClick(R.id.laterBtn)
    public void onClickLater(View view) {
        GTPermissions.manager.permissionLater();
        finish();
    }

    // Loading

    private void loadPermissionData() {
        switch (permissionType) {
            case LOCATION: {
                title.setText(getString(R.string.permissions_location_title));
                descriptionLbl.setText(getString(R.string.permissions_location_description));
                accessBtn.setText(getString(R.string.permissions_location_button));
                image.setImageResource(R.drawable.permission_location);
                break;
            }
            case CAMERA: {
                title.setText(getString(R.string.permissions_camera_title));
                descriptionLbl.setText(getString(R.string.permissions_camera_description));
                accessBtn.setText(getString(R.string.permissions_camera_button));
                image.setImageResource(R.drawable.permission_camera);
                break;
            }
            case STORAGE: {
                title.setText(getString(R.string.permissions_storage_title));
                descriptionLbl.setText(getString(R.string.permissions_storage_description));
                accessBtn.setText(getString(R.string.permissions_storage_button));
                image.setImageResource(R.drawable.permission_storage);
                break;
            }
            case CAMERA_STORAGE: {
                title.setText(getString(R.string.permissions_camera_storage_title));
                descriptionLbl.setText(getString(R.string.permissions_camera_storage_description));
                accessBtn.setText(getString(R.string.permissions_camera_storage_button));
                image.setImageResource(R.drawable.permission_camera);
                break;
            }
        }
    }
}
