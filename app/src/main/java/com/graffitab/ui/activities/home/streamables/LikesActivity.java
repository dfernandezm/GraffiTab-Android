package com.graffitab.ui.activities.home.streamables;

import com.graffitab.R;
import com.graffitab.ui.activities.custom.users.GenericUsersActivity;
import com.graffitab.ui.fragments.users.GenericUsersFragment;

/**
 * Created by georgichristov on 09/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class LikesActivity extends GenericUsersActivity {

    @Override
    public GenericUsersFragment getFragment() {
        return null;
    }

    // Setup

    @Override
    public void setupTopBar() {
        super.setupTopBar();
        getSupportActionBar().setTitle(R.string.likes_likers);
    }
}
