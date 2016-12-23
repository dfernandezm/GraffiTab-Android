package com.graffitab.ui.activities.home.streamables.explorer.mapcomponents;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by georgichristov on 20/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class GTClusterItem implements ClusterItem {

    private final LatLng mPosition;

    public GTClusterItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}
