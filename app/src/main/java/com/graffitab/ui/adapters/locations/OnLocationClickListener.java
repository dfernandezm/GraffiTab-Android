package com.graffitab.ui.adapters.locations;

import com.graffitabsdk.model.GTLocation;

/**
 * Created by georgichristov on 26/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public interface OnLocationClickListener {

    void onRowSelected(GTLocation location);
    void onMenuSelected(GTLocation location);
}
