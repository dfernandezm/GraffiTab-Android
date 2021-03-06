package com.graffitab.ui.fragments.locations;

import com.graffitabsdk.constants.GTConstants;
import com.graffitabsdk.network.common.params.GTQueryParameters;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.sdk.GTSDK;

/**
 * Created by georgichristov on 20/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class LocationsFragment extends GenericLocationsFragment {

    @Override
    public void loadItems(boolean isFirstLoad, int offset, GTResponseHandler handler) {
        GTQueryParameters parameters = new GTQueryParameters();
        parameters.addParameter(GTQueryParameters.GTParameterType.offset, offset);
        parameters.addParameter(GTQueryParameters.GTParameterType.limit, GTConstants.MAX_ITEMS);
        GTSDK.getMeManager().getLocations(isFirstLoad, parameters, handler);
    }
}
