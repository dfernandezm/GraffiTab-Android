package com.graffitab.ui.fragments.locations;

import com.graffitabsdk.constants.GTConstants;
import com.graffitabsdk.network.common.params.GTQueryParameters;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.sdk.GTSDK;
import com.graffitabsdk.sdk.events.locations.LocationCreatedEvent;
import com.graffitabsdk.sdk.events.locations.LocationUpdatedEvent;
import com.squareup.otto.Subscribe;

/**
 * Created by georgichristov on 20/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class LocationsFragment extends GenericLocationsFragment {

    @Override
    public void basicInit() {
        super.basicInit();
        GTSDK.registerEventListener(this);
    }

    @Override
    public void onDestroyView() {
        GTSDK.unregisterEventListener(this);
        super.onDestroyView();
    }

    @Override
    public void loadItems(boolean isFirstLoad, int offset, GTResponseHandler handler) {
        GTQueryParameters parameters = new GTQueryParameters();
        parameters.addParameter(GTQueryParameters.GTParameterType.OFFSET, offset);
        parameters.addParameter(GTQueryParameters.GTParameterType.LIMIT, GTConstants.MAX_ITEMS);
        GTSDK.getMeManager().getLocations(isFirstLoad, parameters, handler);
    }

    @Subscribe
    public void locationCreatedEvent(LocationCreatedEvent event) {
        items.add(event.getLocation());
        adapter.setItems(items, getRecyclerView().getRecyclerView());
    }

    @Subscribe
    public void locationUpdatedEvent(LocationUpdatedEvent event) {
        int index = items.indexOf(event.getLocation());
        if (index >= 0) {
            items.set(index, event.getLocation());
            adapter.setItems(items, getRecyclerView().getRecyclerView());
        }
    }
}
