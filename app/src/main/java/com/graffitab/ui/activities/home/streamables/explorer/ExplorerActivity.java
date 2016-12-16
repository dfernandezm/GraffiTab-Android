package com.graffitab.ui.activities.home.streamables.explorer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.graffitab.R;
import com.graffitab.utils.activity.ActivityUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.graffitab.R.id.map;

public class ExplorerActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUtils.hideActionBar(this);
        setContentView(R.layout.activity_explorer);
        ButterKnife.bind(this);

        setupMapView();
    }

    @OnClick(R.id.backBtn)
    public void onClickBack(View view) {
        finish();
    }

    @OnClick(R.id.gridBtn)
    public void onClickGrid(View view) {

    }

    @OnClick(R.id.locate)
    public void onClickLocate(View view) {

    }

    @OnClick(R.id.createLocationBtn)
    public void onClickCreateLocation(View view) {

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
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
