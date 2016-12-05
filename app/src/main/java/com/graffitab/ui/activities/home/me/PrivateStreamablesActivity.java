package com.graffitab.ui.activities.home.me;

import com.graffitab.R;
import com.graffitab.ui.activities.custom.streamables.ToggleStreamablesActivity;

/**
 * Created by georgichristov on 04/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class PrivateStreamablesActivity extends ToggleStreamablesActivity {

    // Setup

    @Override
    public void setupTopBar() {
        super.setupTopBar();

        getSupportActionBar().setTitle(R.string.private_posts);
    }
}
