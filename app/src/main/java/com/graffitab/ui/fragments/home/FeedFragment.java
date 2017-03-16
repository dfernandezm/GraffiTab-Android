package com.graffitab.ui.fragments.home;

import com.graffitab.R;
import com.graffitab.ui.fragments.streamables.ListStreamablesFragment;
import com.graffitabsdk.sdk.GTSDK;
import com.graffitabsdk.constants.GTConstants;
import com.graffitabsdk.network.common.params.GTQueryParameters;
import com.graffitabsdk.network.common.response.GTResponseHandler;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class FeedFragment extends ListStreamablesFragment {

    @Override
    public int emptyViewImageResource() {
        return R.drawable.empty_feed;
    }

    @Override
    public String emptyViewTitle() {
        return getString(R.string.feed_empty_title);
    }

    @Override
    public String emptyViewSubtitle() {
        return getString(R.string.feed_empty_description);
    }

    @Override
    public void loadItems(boolean isFirstLoad, int offset, GTResponseHandler handler) {
        GTQueryParameters parameters = new GTQueryParameters();
        parameters.addParameter(GTQueryParameters.GTParameterType.offset, offset);
        parameters.addParameter(GTQueryParameters.GTParameterType.limit, GTConstants.MAX_ITEMS);
        GTSDK.getMeManager().getFeed(isFirstLoad, parameters, handler);
    }
}
