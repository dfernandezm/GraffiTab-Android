package com.graffitab.permissions;

import android.Manifest;
import android.app.Activity;

import com.anthonycr.grant.PermissionsManager;

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
        STORAGE
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
        }
    }

    private void checkCameraPermission(Activity context, OnPermissionResultListener callback) {
        String permission = Manifest.permission.CAMERA;
        if (!PermissionsManager.hasPermission(context, permission))
            GTPermissionActivity.openPermissionRequest(context, PermissionType.CAMERA, permission);
        else
            callback.onPermissionGranted();
    }

    private void checkLocationPermission(Activity context, OnPermissionResultListener callback) {
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        if (!PermissionsManager.hasPermission(context, permission))
            GTPermissionActivity.openPermissionRequest(context, PermissionType.LOCATION, permission);
        else
            callback.onPermissionGranted();
    }

    private void checkStoragePermission(Activity context, OnPermissionResultListener callback) {
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (!PermissionsManager.hasPermission(context, permission))
            GTPermissionActivity.openPermissionRequest(context, PermissionType.STORAGE, permission);
        else
            callback.onPermissionGranted();
    }

    public interface OnPermissionResultListener {
        void onPermissionGranted();
        void onPermissionDenied();
        void onDecideLater();
    }
}
