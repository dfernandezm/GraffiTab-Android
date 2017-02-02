package com.graffitab.ui.activities.home.streamables.explorer.mapcomponents;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.graffitabsdk.model.GTStreamable;

/**
 * Created by georgichristov on 20/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class GTClusterItem implements ClusterItem {

    private final LatLng mPosition;
    private GTStreamable streamable;

    public GTClusterItem(GTStreamable streamable, double lat, double lng) {
        this.mPosition = new LatLng(lat, lng);
        this.streamable = streamable;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public GTStreamable getStreamable() {
        return streamable;
    }
}
