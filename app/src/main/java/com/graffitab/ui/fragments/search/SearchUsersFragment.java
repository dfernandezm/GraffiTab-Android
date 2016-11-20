package com.graffitab.ui.fragments.search;

import com.graffitab.ui.fragments.user.ListUsersFragment;
import com.graffitab.utils.display.DisplayUtils;

/**
 * Created by georgichristov on 18/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class SearchUsersFragment extends ListUsersFragment {

    @Override
    public void setupCustomViews() {
        super.setupCustomViews();

        int padding = DisplayUtils.pxToDip(getContext(), 10);
        getRecyclerView().setPadding(padding, padding, padding, 0);
    }
}
