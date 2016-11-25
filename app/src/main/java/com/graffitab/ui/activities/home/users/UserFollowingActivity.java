package com.graffitab.ui.activities.home.users;

import com.graffitab.R;
import com.graffitab.ui.activities.custom.users.GenericUsersActivity;

/**
 * Created by georgichristov on 24/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class UserFollowingActivity extends GenericUsersActivity {

    // Setup

    @Override
    public void setupTopBar() {
        super.setupTopBar();

        getSupportActionBar().setTitle(R.string.following);
    }
}
