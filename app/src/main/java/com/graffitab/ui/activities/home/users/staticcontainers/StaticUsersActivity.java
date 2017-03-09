package com.graffitab.ui.activities.home.users.staticcontainers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.ui.activities.custom.users.BaseUsersActivity;
import com.graffitab.ui.fragments.users.GenericUsersFragment;
import com.graffitab.ui.fragments.users.StaticUsersFragment;
import com.graffitabsdk.model.GTUser;

import java.util.ArrayList;

/**
 * Created by georgichristov on 23/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class StaticUsersActivity extends BaseUsersActivity {

    private ArrayList<GTUser> staticItems;

    public static void open(Context context, ArrayList<GTUser> users) {
        Intent i = new Intent(context, StaticUsersActivity.class);
        i.putExtra(Constants.EXTRA_USERS, users);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getSerializable(Constants.EXTRA_USERS) != null) {
            staticItems = (ArrayList<GTUser>) extras.getSerializable(Constants.EXTRA_USERS);
        }
        else {
            finish();
            return;
        }
        super.onCreate(savedInstanceState);
    }



    @Override
    public GenericUsersFragment getFragment() {
        GenericUsersFragment fragment = new StaticUsersFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.EXTRA_USERS, staticItems);
        fragment.setArguments(args);
        return fragment;
    }

    // Setup

    @Override
    public void setupTopBar() {
        super.setupTopBar();
        getSupportActionBar().setTitle(getString(R.string.static_users_count, staticItems.size()));
    }

}
