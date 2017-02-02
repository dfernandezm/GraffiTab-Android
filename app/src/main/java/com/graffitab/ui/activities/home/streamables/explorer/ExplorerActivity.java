package com.graffitab.ui.activities.home.streamables.explorer;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.graffitab.R;
import com.graffitab.config.AppConfig;
import com.graffitab.constants.Constants;
import com.graffitab.managers.GTLocationManager;
import com.graffitab.ui.activities.home.me.locations.CreateLocationActivity;
import com.graffitab.ui.activities.home.streamables.StreamableDetailsActivity;
import com.graffitab.ui.activities.home.streamables.explorer.mapcomponents.GTClusterItem;
import com.graffitab.ui.activities.home.streamables.explorer.mapcomponents.GTClusterRenderer;
import com.graffitab.ui.activities.home.streamables.explorer.staticcontainers.ClusterActivity;
import com.graffitab.ui.dialog.DialogBuilder;
import com.graffitab.ui.dialog.TaskDialog;
import com.graffitab.ui.dialog.handlers.OnYesNoHandler;
import com.graffitab.utils.activity.ActivityUtils;
import com.graffitab.utils.api.ApiUtils;
import com.graffitabsdk.config.GTSDK;
import com.graffitabsdk.model.GTStreamable;
import com.graffitabsdk.network.common.params.GTQueryParameters;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.network.service.location.response.GTLocationResponse;
import com.graffitabsdk.network.service.streamable.response.GTListStreamablesResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.graffitab.R.id.map;
import static com.graffitab.ui.activities.home.me.locations.CreateLocationActivity.findAddressForLocation;

public class ExplorerActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean shouldCheckForCurrentLocation = true;
    private ClusterManager<GTClusterItem> mClusterManager;
    private Timer timer;
    private Location customLocation;
    private ArrayList<GTStreamable> items = new ArrayList<>();
    private int previousMarkerSize = 0;

    public static void openForLocation(Context context, double latitude, double longitude) {
        Intent i = new Intent(context, ExplorerActivity.class);
        i.putExtra(Constants.EXTRA_LATITUDE, latitude);
        i.putExtra(Constants.EXTRA_LONGITUDE, longitude);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUtils.hideActionBar(this);
        setContentView(R.layout.activity_explorer);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            double lat = extras.getDouble(Constants.EXTRA_LATITUDE);
            double lon = extras.getDouble(Constants.EXTRA_LONGITUDE);
            if (lat != 0.0 && lon != 0.0) {
                customLocation = new Location(LocationManager.GPS_PROVIDER);
                customLocation.setLatitude(lat);
                customLocation.setLongitude(lon);
            }
        }

        setupMapView();
    }

    @Override
    protected void onDestroy() {
        shouldCheckForCurrentLocation = false;
        stopTimer();

        super.onDestroy();
    }

    @OnClick(R.id.backBtn)
    public void onClickBack(View view) {
        finish();
    }

    @OnClick(R.id.gridBtn)
    public void onClickGrid(View view) {
        ClusterActivity.openCluster(ExplorerActivity.this, items);
    }

    @OnClick(R.id.locate)
    public void onClickLocate(View view) {
        zoomToLocation(GTLocationManager.sharedInstance.getLastKnownLocation());
    }

    @OnClick(R.id.createLocationBtn)
    public void onClickCreateLocation(View view) {
        DialogBuilder.buildYesNoDialog(this, getString(R.string.app_name), getString(R.string.explorer_save_location_prompt), getString(R.string.other_save), getString(R.string.other_cancel), new OnYesNoHandler() {

            @Override
            public void onClickYes() {
                saveLocation();
            }

            @Override
            public void onClickNo() {}
        });
    }

    private void saveLocation() {
        final LatLng center = mMap.getCameraPosition().target;
        TaskDialog.getInstance().showDialog(getString(R.string.other_processing), this, null);
        findAddressForLocation(this, center.latitude, center.longitude, new CreateLocationActivity.OnAddressFoundListener() {

            @Override
            public void onAddressFound(String address) {
                GTSDK.getMeManager().createLocation(address, center.latitude, center.longitude, new GTResponseHandler<GTLocationResponse>() {

                    @Override
                    public void onSuccess(GTResponse<GTLocationResponse> gtResponse) {
                        TaskDialog.getInstance().hideDialog();
                        DialogBuilder.buildOKDialog(ExplorerActivity.this, getString(R.string.app_name), getString(R.string.create_location_success));
                    }

                    @Override
                    public void onFailure(GTResponse<GTLocationResponse> gtResponse) {
                        TaskDialog.getInstance().hideDialog();
                        DialogBuilder.buildAPIErrorDialog(ExplorerActivity.this, getString(R.string.app_name), ApiUtils.localizedErrorReason(gtResponse), true, gtResponse.getResultCode());
                    }
                });
            }

            @Override
            public void onAddressNotFound() {
                TaskDialog.getInstance().hideDialog();
                DialogBuilder.buildOKDialog(ExplorerActivity.this, getString(R.string.app_name), getString(R.string.create_location_no_matches));
            }
        });
    }

    // Loading

    private void refreshMap() {
        Log.i(getClass().getSimpleName(), "Refreshing map...");
        LatLng center = mMap.getCameraPosition().target;
        GTQueryParameters parameters = new GTQueryParameters();
        parameters.addParameter(GTQueryParameters.GTParameterType.LATITUDE, center.latitude + "");
        parameters.addParameter(GTQueryParameters.GTParameterType.LONGITUDE, center.longitude + "");
        parameters.addParameter(GTQueryParameters.GTParameterType.RADIUS, AppConfig.configuration.locationRadius + "");
        GTSDK.getStreamableManager().searchLocation(parameters, new GTResponseHandler<GTListStreamablesResponse>() {

            @Override
            public void onSuccess(GTResponse<GTListStreamablesResponse> gtResponse) {
                processAnnotations(gtResponse.getObject().items);
            }

            @Override
            public void onFailure(GTResponse<GTListStreamablesResponse> gtResponse) {
                DialogBuilder.buildAPIErrorDialog(ExplorerActivity.this, getString(R.string.app_name), ApiUtils.localizedErrorReason(gtResponse), gtResponse.getResultCode());
            }
        });
    }

    private void processAnnotations(List<GTStreamable> streamables) {
        for (GTStreamable streamable : streamables) {
            if (!items.contains(streamable)) {
                GTClusterItem offsetItem = new GTClusterItem(streamable, streamable.latitude, streamable.longitude);
                mClusterManager.addItem(offsetItem);
                items.add(streamable);
            }
        }

        if (previousMarkerSize != items.size())
            mClusterManager.cluster();
        previousMarkerSize = items.size();
    }

    // Timer

    private void startTimer() {
        stopTimer();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        refreshMap();
                    }
                });
            }
        }, 0, 3000);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    // Map

    private void awaitCurrentLocation() {
        new Thread() {

            @Override
            public void run() {
                // Loop until we find a location or we close the current activity.
                while (GTLocationManager.sharedInstance.getLastKnownLocation() == null && shouldCheckForCurrentLocation) {
                    try {
                        Log.i(ExplorerActivity.this.getClass().getSimpleName(), "Polling for location...");
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(ExplorerActivity.this.getClass().getSimpleName(), "Interrupted polling for location...", e);
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

        startTimer();
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

        // Setup cluster manager.
        mClusterManager = new ClusterManager<GTClusterItem>(this, mMap);
        mClusterManager.setRenderer(new GTClusterRenderer(this, mMap, mClusterManager));
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<GTClusterItem>() {

            @Override
            public boolean onClusterClick(Cluster<GTClusterItem> cluster) {
                // Get cluster items.
                ArrayList<GTStreamable> clusterItems = new ArrayList<>();
                for (GTClusterItem item : cluster.getItems())
                    clusterItems.add(item.getStreamable());
                ClusterActivity.openCluster(ExplorerActivity.this, clusterItems);
                return true;
            }
        });
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<GTClusterItem>() {

            @Override
            public boolean onClusterItemClick(GTClusterItem gtClusterItem) {
                StreamableDetailsActivity.openStreamableDetails(ExplorerActivity.this, gtClusterItem.getStreamable());
                return true;
            }
        });
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
    }
}
