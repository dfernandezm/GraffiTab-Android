package com.graffitab.ui.fragments.search;

import com.graffitab.ui.fragments.users.ListUsersFragment;
import com.graffitab.utils.device.DeviceUtils;
import com.graffitabsdk.sdk.GTSDK;
import com.graffitabsdk.constants.GTConstants;
import com.graffitabsdk.network.common.params.GTQueryParameters;
import com.graffitabsdk.network.common.response.GTResponseHandler;

/**
 * Created by georgichristov on 18/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class SearchUsersFragment extends ListUsersFragment {

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
            GTSDK.getUserManager().getMostActiveUsers(isFirstLoad, parameters, handler);
        else {
            getRecyclerView().beginRefreshing();
            parameters.addParameter(GTQueryParameters.GTParameterType.query, searchQuery);
            GTSDK.getUserManager().search(parameters, handler);
        }
    }

    @Override
    public void setupCustomViews() {
        super.setupCustomViews();

        int padding = DeviceUtils.pxToDip(getContext(), 10);
        getRecyclerView().setPadding(padding, padding, padding, 0);
    }
}
