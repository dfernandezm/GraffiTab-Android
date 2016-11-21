package com.graffitab.ui.fragments.location;

import com.graffitab.utils.display.DisplayUtils;

/**
 * Created by georgichristov on 20/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class ListLocationsFragment extends GenericLocationsFragment {

    @Override
    public void basicInit() {
        setViewType(GenericLocationsFragment.ViewType.LIST_FULL);
    }

    @Override
    public void setupCustomViews() {
        super.setupCustomViews();

        int padding = DisplayUtils.pxToDip(getContext(), 10);
        getRecyclerView().setPadding(padding, padding, padding, 0);
    }
}
