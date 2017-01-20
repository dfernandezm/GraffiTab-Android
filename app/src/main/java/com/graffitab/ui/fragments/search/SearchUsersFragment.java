package com.graffitab.ui.fragments.search;

import com.graffitab.ui.fragments.users.ListUsersFragment;
import com.graffitab.utils.display.DisplayUtils;
import com.graffitabsdk.config.GTSDK;
import com.graffitabsdk.constants.GTConstants;
import com.graffitabsdk.network.common.params.GTQueryParameters;
import com.graffitabsdk.network.common.response.GTResponseHandler;

/**
 * Created by georgichristov on 18/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class SearchUsersFragment extends ListUsersFragment {

    @Override
    public void loadItems(boolean isFirstLoad, int offset, GTResponseHandler handler) {
        GTQueryParameters parameters = new GTQueryParameters();
        parameters.addParameter(GTQueryParameters.GTParameterType.OFFSET, offset);
        parameters.addParameter(GTQueryParameters.GTParameterType.LIMIT, GTConstants.MAX_ITEMS);
        GTSDK.getUserManager().getMostActiveUsers(isFirstLoad, parameters, handler);
    }

    @Override
    public void setupCustomViews() {
        super.setupCustomViews();

        int padding = DisplayUtils.pxToDip(getContext(), 10);
        getRecyclerView().setPadding(padding, padding, padding, 0);
    }
}
