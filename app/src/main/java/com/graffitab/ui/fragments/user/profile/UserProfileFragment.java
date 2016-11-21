package com.graffitab.ui.fragments.user.profile;

import com.graffitab.R;
import com.graffitab.ui.fragments.streamable.ListStreamablesFragment;

/**
 * Created by georgichristov on 21/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class UserProfileFragment extends ListStreamablesFragment {

    @Override
    public void setupCustomViews() {
        super.setupCustomViews();

        adapter.addHeaderView(R.layout.decoration_header_profile, advancedRecyclerView.getRecyclerView());
    }
}
