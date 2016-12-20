package com.graffitab.ui.activities.home.streamables.explorer.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.graffitab.R;
import com.graffitab.application.MyApplication;

/**
 * Created by georgichristov on 20/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class GTClusterRenderer extends DefaultClusterRenderer<GTClusterItem> {

    private IconGenerator iconGenerator;

    public GTClusterRenderer(Context context, GoogleMap map, ClusterManager<GTClusterItem> clusterManager) {
        super(context, map, clusterManager);

        this.iconGenerator = new IconGenerator(context);
    }

    @Override
    protected void onBeforeClusterItemRendered(GTClusterItem item, MarkerOptions markerOptions) {
        final Drawable itemIcon = MyApplication.getInstance().getResources().getDrawable(R.drawable.marker);
        iconGenerator.setBackground(itemIcon);
        Bitmap icon = iconGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }

    @Override
    protected int getColor(int clusterSize) {
        if (clusterSize <= 5)
            return Color.parseColor("#87bad3");
        else if (clusterSize > 5 && clusterSize < 10)
            return Color.parseColor("#5b9fc1");
        else
            return MyApplication.getInstance().getResources().getColor(R.color.colorPrimary);
    }
}
