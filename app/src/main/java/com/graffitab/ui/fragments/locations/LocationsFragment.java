package com.graffitab.ui.fragments.locations;

import com.graffitab.application.MyApplication;
import com.graffitab.utils.display.DisplayUtils;

/**
 * Created by georgichristov on 20/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class LocationsFragment extends GenericLocationsFragment {

    @Override
    public void setupCustomViews() {
        super.setupCustomViews();

        int padding = DisplayUtils.pxToDip(MyApplication.getInstance(), 10);
        getRecyclerView().setPadding(padding, padding, padding, 0);
    }
}
