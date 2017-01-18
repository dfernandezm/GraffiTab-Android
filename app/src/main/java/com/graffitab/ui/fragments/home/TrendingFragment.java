package com.graffitab.ui.fragments.home;

import com.graffitab.ui.fragments.streamables.TrendingStreamablesFragment;
import com.graffitabsdk.config.GTSDK;
import com.graffitabsdk.constants.GTConstants;
import com.graffitabsdk.network.common.params.GTQueryParameters;
import com.graffitabsdk.network.common.response.GTResponseHandler;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class TrendingFragment extends TrendingStreamablesFragment {

    @Override
    public void loadItems(boolean isFirstLoad, int offset, GTResponseHandler handler) {
        GTQueryParameters parameters = new GTQueryParameters();
        parameters.addParameter(GTQueryParameters.GTParameterType.OFFSET, offset);
        parameters.addParameter(GTQueryParameters.GTParameterType.LIMIT, GTConstants.MAX_ITEMS);
        GTSDK.getStreamableManager().getPopular(isFirstLoad, parameters, handler);
    }
}
