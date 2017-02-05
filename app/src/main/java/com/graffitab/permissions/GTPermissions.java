package com.graffitab.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import com.anthonycr.grant.PermissionsManager;

import static com.graffitab.permissions.GTPermissions.PermissionType.CAMERA_STORAGE;

/**
 * Created by georgichristov on 03/02/2017
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class GTPermissions {

    public static final GTPermissions manager = new GTPermissions();

    public enum PermissionType {
        CAMERA,
        LOCATION,
        STORAGE,
        CAMERA_STORAGE
    }

    private OnPermissionResultListener callback;

    public void permissionDenied() {
        callback.onPermissionDenied();
    }

    public void permissionGranted() {
        callback.onPermissionGranted();
    }

    public void permissionLater() {
        callback.onDecideLater();
    }

    public void checkPermission(Activity context, PermissionType type, OnPermissionResultListener callback) {
        this.callback = callback;
        switch (type) {
            case CAMERA:
                checkCameraPermission(context, callback);
                break;
            case LOCATION:
                checkLocationPermission(context, callback);
                break;
            case STORAGE:
                checkStoragePermission(context, callback);
                break;
            case CAMERA_STORAGE:
                checkCameraStoragePermission(context, callback);
                break;
        }
    }

    public boolean hasPermission(Context context, PermissionType type) {
        switch (type) {
            case CAMERA:
                return PermissionsManager.hasPermission(context, Manifest.permission.CAMERA);
            case LOCATION:
                return PermissionsManager.hasPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
            case STORAGE:
                return PermissionsManager.hasPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            case CAMERA_STORAGE:
                return PermissionsManager.hasAllPermissions(context, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA});
        }
        return false;
    }

    private void checkCameraPermission(Activity context, OnPermissionResultListener callback) {
        String permission = Manifest.permission.CAMERA;
        String[] permissions = new String[]{permission};
        if (!PermissionsManager.hasPermission(context, permission))
            GTPermissionActivity.openPermissionRequest(context, PermissionType.CAMERA, permissions);
        else
            callback.onPermissionGranted();
    }

    private void checkLocationPermission(Activity context, OnPermissionResultListener callback) {
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        String[] permissions = new String[]{permission};
        if (!PermissionsManager.hasPermission(context, permission))
            GTPermissionActivity.openPermissionRequest(context, PermissionType.LOCATION, permissions);
        else
            callback.onPermissionGranted();
    }

    private void checkStoragePermission(Activity context, OnPermissionResultListener callback) {
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String[] permissions = new String[]{permission};
        if (!PermissionsManager.hasPermission(context, permission))
            GTPermissionActivity.openPermissionRequest(context, PermissionType.STORAGE, permissions);
        else
            callback.onPermissionGranted();
    }

    private void checkCameraStoragePermission(Activity context, OnPermissionResultListener callback) {
        String storagePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String cameraPermission = Manifest.permission.CAMERA;
        String[] permissions = new String[]{cameraPermission, storagePermission};
        if (!PermissionsManager.hasAllPermissions(context, permissions))
            GTPermissionActivity.openPermissionRequest(context, CAMERA_STORAGE, permissions);
        else
            callback.onPermissionGranted();
    }

    public interface OnPermissionResultListener {
        void onPermissionGranted();
        void onPermissionDenied();
        void onDecideLater();
    }
}
