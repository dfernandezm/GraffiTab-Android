package com.graffitab.ui.activities.home.streamables.explorer.staticcontainers;

import com.graffitab.R;
import com.graffitab.ui.activities.custom.streamables.GenericStreamablesActivity;

/**
 * Created by georgichristov on 23/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class StaticStreamablesActivity extends GenericStreamablesActivity {

    // Setup

    @Override
    public void setupTopBar() {
        super.setupTopBar();

        getSupportActionBar().setTitle(getString(R.string.static_streamables_count, getContent().items.size()));
    }
}
