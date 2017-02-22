package com.graffitab.ui.fragments.users;

import com.graffitab.R;
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
    public int getLayoutResId() {
        if (DeviceUtils.isTablet(getActivity()))
            return R.layout.fragment_refreshable_recyclerview_users;
        return super.getLayoutResId();
    }

    @Override
    public void setupCustomViews() {
        super.setupCustomViews();

        int padding = DeviceUtils.pxToDip(getContext(), 10);
        getRecyclerView().setPadding(0, padding, 0, 0);
    }
}
