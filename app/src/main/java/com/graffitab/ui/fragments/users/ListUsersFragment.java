package com.graffitab.ui.fragments.users;

import com.graffitab.utils.device.DeviceUtils;

/**
 * Created by georgichristov on 15/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public abstract class ListUsersFragment extends GenericUsersFragment {

    @Override
    public void basicInit() {
        super.basicInit();
        setViewType(ViewType.LIST_FULL);
    }

    @Override
    public void setupCustomViews() {
        super.setupCustomViews();

        if (DeviceUtils.isTablet(getActivity())) {
            int padding = DeviceUtils.pxToDip(getContext(), 10);
            getRecyclerView().setPadding(0, padding, 0, 0);
        }
    }
}
