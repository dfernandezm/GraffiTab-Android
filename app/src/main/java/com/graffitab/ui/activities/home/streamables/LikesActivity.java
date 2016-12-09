package com.graffitab.ui.activities.home.streamables;

import com.graffitab.R;
import com.graffitab.ui.activities.custom.users.GenericUsersActivity;

/**
 * Created by georgichristov on 09/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class LikesActivity extends GenericUsersActivity {

    @Override
    public void setupTopBar() {
        super.setupTopBar();
        getSupportActionBar().setTitle(R.string.likes_likers);
    }
}
