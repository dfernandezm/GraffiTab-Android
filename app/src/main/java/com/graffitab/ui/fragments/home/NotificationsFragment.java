package com.graffitab.ui.fragments.home;

import com.graffitab.ui.fragments.notifications.GenericNotificationsFragment;
import com.graffitabsdk.sdk.GTSDK;
import com.graffitabsdk.constants.GTConstants;
import com.graffitabsdk.network.common.params.GTQueryParameters;
import com.graffitabsdk.network.common.response.GTResponseHandler;

/**
 * Created by georgichristov on 30/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class NotificationsFragment extends GenericNotificationsFragment {

    @Override
    public void loadItems(boolean isFirstLoad, int offset, GTResponseHandler handler) {
        GTQueryParameters parameters = new GTQueryParameters();
        parameters.addParameter(GTQueryParameters.GTParameterType.offset, offset);
        parameters.addParameter(GTQueryParameters.GTParameterType.limit, GTConstants.MAX_ITEMS);
        GTSDK.getMeManager().getNotifications(isFirstLoad, parameters, handler);
    }
}
