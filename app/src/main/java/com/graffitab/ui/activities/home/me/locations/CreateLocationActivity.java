package com.graffitab.ui.activities.home.me.locations;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.managers.GTLocationManager;
import com.graffitab.ui.dialog.DialogBuilder;
import com.graffitab.ui.dialog.TaskDialog;
import com.graffitab.ui.dialog.handlers.OnOkHandler;
import com.graffitab.utils.Utils;
import com.graffitab.utils.activity.ActivityUtils;
import com.graffitab.utils.api.ApiUtils;
import com.graffitab.utils.input.KeyboardUtils;
import com.graffitabsdk.config.GTSDK;
import com.graffitabsdk.model.GTLocation;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.network.service.location.response.GTLocationResponse;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.graffitab.R.id.map;

/**
 * Created by georgichristov on 21/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class CreateLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.searchField) EditText searchField;

    private GoogleMap mMap;
    private boolean shouldCheckForCurrentLocation = true;
    private boolean isSearching = false;
    private Location customLocation;
    private GTLocation toEdit;

    public static void openForLocation(Context context, GTLocation location) {
        Intent i = new Intent(context, CreateLocationActivity.class);
        i.putExtra(Constants.EXTRA_LOCATION, location);
        context.startActivity(i);
    }

    public static void findAddressForLocation(final Activity context, final double latitude, final double longitude, final OnAddressFoundListener listener) {
        new Thread() {

            @Override
            public void run() {
                Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
                try {
                    List<Address> addresses = geoCoder.getFromLocation(latitude, longitude, 1);
                    if (addresses.size() > 0) {
                        Address address = addresses.get(0);
                        final String addressString = parseAddress(address);

                        context.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if (listener != null)
                                    listener.onAddressFound(addressString);
                            }
                        });
                    }
                    else {
                        context.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if (listener != null)
                                    listener.onAddressNotFound();
                            }
                        });
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static String parseAddress(Address address) {
        String addressString = "";
        int i;
        for(i = 0; i < address.getMaxAddressLineIndex() - 1; i++) {
            addressString += address.getAddressLine(i) + ", ";
        }
        addressString += address.getAddressLine(i);
        return addressString;
    }

    public static void findAddress(final Activity context, final String address, final OnLocationFoundListener listener) {
        new Thread() {

            @Override
            public void run() {
                Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
                try {
                    List<Address> addresses = geoCoder.getFromLocationName(address , 1);
                    if (addresses.size() > 0) {
                        final double lat = addresses.get(0).getLatitude();
                        final double lng = addresses.get(0).getLongitude();
                        final Location temp = new Location(LocationManager.GPS_PROVIDER);
                        temp.setLatitude(lat);
                        temp.setLongitude(lng);

                        context.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if (listener != null)
                                    listener.onLocationFound(temp);
                            }
                        });
                    }
                    else {
                        context.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if (listener != null)
                                    listener.onLocationNotFound();
                            }
                        });
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUtils.hideActionBar(this);
        setContentView(R.layout.activity_create_location);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Serializable serializable = extras.getSerializable(Constants.EXTRA_LOCATION);
            if (serializable != null) {
                toEdit = (GTLocation) serializable;
                customLocation = new Location(LocationManager.GPS_PROVIDER);
                customLocation.setLatitude(toEdit.latitude);
                customLocation.setLongitude(toEdit.longitude);
            }
        }

        setupMapView();
        setupTextFields();
    }

    @Override
    protected void onDestroy() {
        shouldCheckForCurrentLocation = false;

        super.onDestroy();
    }

    @OnClick(R.id.backBtn)
    public void onClickBack(View view) {
        finish();
    }

    @OnClick(R.id.fab)
    public void onClickCreateLocation(View view) {
        final LatLng center = mMap.getCameraPosition().target;
        TaskDialog.getInstance().showDialog(getString(R.string.other_processing), this, null);
        findAddressForLocation(this, center.latitude, center.longitude, new OnAddressFoundListener() {

            @Override
            public void onAddressFound(String address) {
                if (customLocation != null) { // Edit location.
                    GTSDK.getMeManager().editLocation(toEdit.id, address, center.latitude, center.longitude, new GTResponseHandler<GTLocationResponse>() {

                        @Override
                        public void onSuccess(GTResponse<GTLocationResponse> gtResponse) {
                            finishSuccessfulSave();
                        }

                        @Override
                        public void onFailure(GTResponse<GTLocationResponse> gtResponse) {
                            finishSaveWithError(gtResponse);
                        }
                    });
                }
                else { // Create location.
                    GTSDK.getMeManager().createLocation(address, center.latitude, center.longitude, new GTResponseHandler<GTLocationResponse>() {

                        @Override
                        public void onSuccess(GTResponse<GTLocationResponse> gtResponse) {
                            finishSuccessfulSave();
                        }

                        @Override
                        public void onFailure(GTResponse<GTLocationResponse> gtResponse) {
                            finishSaveWithError(gtResponse);
                        }
                    });
                }
            }

            @Override
            public void onAddressNotFound() {
                TaskDialog.getInstance().hideDialog();
                DialogBuilder.buildOKDialog(CreateLocationActivity.this, getString(R.string.app_name), getString(R.string.create_location_no_matches));
            }
        });
    }

    @OnClick(R.id.searchBtn)
    public void onClickToggleSearch(View view) {
        isSearching = !isSearching;

        if (isSearching) {
            searchField.setVisibility(View.VISIBLE);
            Utils.runWithDelay(new Runnable() {

                @Override
                public void run() {
                    searchField.requestFocus();
                    KeyboardUtils.showKeyboard(CreateLocationActivity.this, searchField);
                }
            }, 100);
        }
        else {
            searchField.setVisibility(View.GONE);
            Utils.runWithDelay(new Runnable() {

                @Override
                public void run() {
                    KeyboardUtils.hideKeyboard(CreateLocationActivity.this, searchField);
                }
            }, 100);
        }
    }

    private void finishSuccessfulSave() {
        TaskDialog.getInstance().hideDialog();
        DialogBuilder.buildOKDialog(this, getString(R.string.app_name), getString(R.string.create_location_success), new OnOkHandler() {

            @Override
            public void onClickOk() {
                finish();
            }
        });
    }

    private void finishSaveWithError(GTResponse<GTLocationResponse> gtResponse) {
        TaskDialog.getInstance().hideDialog();
        DialogBuilder.buildAPIErrorDialog(this, getString(R.string.app_name), ApiUtils.localizedErrorReason(gtResponse), true, gtResponse.getResultCode());
    }

    // Map

    private void awaitCurrentLocation() {
        new Thread() {

            @Override
            public void run() {
                // Loop until we find a location or we close the current activity.
                while (GTLocationManager.sharedInstance.getLastKnownLocation() == null && shouldCheckForCurrentLocation) {
                    try {
                        Log.i(CreateLocationActivity.this.getClass().getSimpleName(), "Polling for location...");
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(CreateLocationActivity.this.getClass().getSimpleName(), "Interrupted polling for location...", e);
                    }
                }

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Location location = GTLocationManager.sharedInstance.getLastKnownLocation();
                        zoomToLocation(location);
                    }
                });
            }
        }.start();
    }

    private void zoomToLocation(Location location) {
        if (location != null) { // Make sure we have a valid location.
            Log.i(getClass().getSimpleName(), "Zooming to location (latitude=" + location.getLatitude() + ", longitude=" + location.getLongitude() + ")");
            LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(sydney, 16);
            mMap.animateCamera(update);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        setupMapOnceAvailable();

        if (customLocation != null)
            zoomToLocation(customLocation);
        else
            awaitCurrentLocation();
    }

    // Search

    private void search(String query) {
        TaskDialog.getInstance().showDialog(getString(R.string.other_processing), this, null);
        findAddress(this, query, new OnLocationFoundListener() {

            @Override
            public void onLocationFound(Location location) {
                TaskDialog.getInstance().hideDialog();
                zoomToLocation(location);
            }

            @Override
            public void onLocationNotFound() {
                TaskDialog.getInstance().hideDialog();
                DialogBuilder.buildOKDialog(CreateLocationActivity.this, getString(R.string.app_name), getString(R.string.create_location_no_matches));
            }
        });
    }

    // Setup

    private void setupMapView() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
    }

    private void setupMapOnceAvailable() {
        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            e.printStackTrace();
            Log.e(getClass().getSimpleName(), "Location permission not granted", e);
        }

        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
    }

    private void setupTextFields() {
        searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (searchField.getText().toString().length() > 0) {
                        KeyboardUtils.hideKeyboard(CreateLocationActivity.this);
                        search(searchField.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public interface OnAddressFoundListener {
        void onAddressFound(String address);
        void onAddressNotFound();
    }

    public interface OnLocationFoundListener {
        void onLocationFound(Location location);
        void onLocationNotFound();
    }
}
