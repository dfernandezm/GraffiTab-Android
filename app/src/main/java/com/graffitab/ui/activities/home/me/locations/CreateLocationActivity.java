package com.graffitab.ui.activities.home.me.locations;

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
import com.graffitab.managers.GTLocationManager;
import com.graffitab.ui.dialog.DialogBuilder;
import com.graffitab.ui.dialog.TaskDialog;
import com.graffitab.utils.Utils;
import com.graffitab.utils.activity.ActivityUtils;
import com.graffitab.utils.input.KeyboardUtils;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUtils.hideActionBar(this);
        setContentView(R.layout.activity_create_location);
        ButterKnife.bind(this);

        setupMapView();
        setupTextFields();

        awaitCurrentLocation();
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
        finish();
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

    // Search

    private void findAddress(final String address) {
        TaskDialog.getInstance().showDialog(getString(R.string.other_processing), this, null);
        new Thread() {

            @Override
            public void run() {
                Geocoder geoCoder = new Geocoder(CreateLocationActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = geoCoder.getFromLocationName(address , 1);
                    if (addresses.size() > 0) {
                        final double lat = addresses.get(0).getLatitude();
                        final double lng = addresses.get(0).getLongitude();

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                TaskDialog.getInstance().hideDialog();

                                Location temp = new Location(LocationManager.GPS_PROVIDER);
                                temp.setLatitude(lat);
                                temp.setLongitude(lng);
                                zoomToLocation(temp);
                            }
                        });
                    }
                    else {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                TaskDialog.getInstance().hideDialog();

                                DialogBuilder.buildOKDialog(CreateLocationActivity.this, getString(R.string.app_name), getString(R.string.create_location_no_matches));
                            }
                        });
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
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
                        findAddress(searchField.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });
    }
}
