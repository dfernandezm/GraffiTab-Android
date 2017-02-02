package com.graffitab.managers;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.graffitab.application.MyApplication;
import com.graffitab.config.AppConfig;

/**
 * Created by georgichristov on 17/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class GTLocationManager implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final GTLocationManager sharedInstance = new GTLocationManager();

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location lastLocation;

    private GTLocationManager() {
        buildGoogleApiClient();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(getClass().getSimpleName(), "Google API connected");
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(AppConfig.configuration.locationUpdateInterval);

        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } catch (SecurityException e) {

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(getClass().getSimpleName(), "Google API connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(getClass().getSimpleName(), "Google API connection failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(getClass().getSimpleName(), "Location changed");
        lastLocation = location;
    }

    public Location getLastKnownLocation() {
        return lastLocation;
    }

    public void startLocationUpdates() {
        stopLocationUpdates();
        mGoogleApiClient.connect();
    }

    public void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(MyApplication.getInstance())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
}
