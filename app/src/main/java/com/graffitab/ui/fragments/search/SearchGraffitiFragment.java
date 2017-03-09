package com.graffitab.ui.fragments.search;

import com.graffitab.ui.fragments.streamables.GridStreamablesFragment;
import com.graffitabsdk.sdk.GTSDK;
import com.graffitabsdk.constants.GTConstants;
import com.graffitabsdk.network.common.params.GTQueryParameters;
import com.graffitabsdk.network.common.response.GTResponseHandler;

/**
 * Created by georgichristov on 18/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class SearchGraffitiFragment extends GridStreamablesFragment {

    private String searchQuery;

    public void search(String query) {
        presetSearchQuery(query);
        reload();
    }

    public void presetSearchQuery(String query) {
        this.searchQuery = query;
    }

    @Override
    public void loadItems(boolean isFirstLoad, int offset, GTResponseHandler handler) {
        GTQueryParameters parameters = new GTQueryParameters();
        parameters.addParameter(GTQueryParameters.GTParameterType.offset, offset);
        parameters.addParameter(GTQueryParameters.GTParameterType.limit, GTConstants.MAX_ITEMS);

        if (searchQuery == null) // Load default search results.
            GTSDK.getStreamableManager().getPopular(isFirstLoad, parameters, handler);
        else {
            getRecyclerView().beginRefreshing();
            parameters.addParameter(GTQueryParameters.GTParameterType.query, searchQuery);
            GTSDK.getStreamableManager().search(parameters, handler);
        }
    }
}
