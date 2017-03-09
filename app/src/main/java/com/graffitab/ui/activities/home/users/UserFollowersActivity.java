package com.graffitab.ui.activities.home.users;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.ui.activities.custom.users.BaseUsersActivity;
import com.graffitab.ui.fragments.users.GenericUsersFragment;
import com.graffitab.ui.fragments.users.ListUsersFragment;
import com.graffitabsdk.sdk.GTSDK;
import com.graffitabsdk.constants.GTConstants;
import com.graffitabsdk.model.GTUser;
import com.graffitabsdk.network.common.params.GTQueryParameters;
import com.graffitabsdk.network.common.response.GTResponseHandler;

/**
 * Created by georgichristov on 24/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class UserFollowersActivity extends BaseUsersActivity {

    private GTUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getSerializable(Constants.EXTRA_USER) != null) {
            user = (GTUser) extras.getSerializable(Constants.EXTRA_USER);
        }
        else {
            finish();
            return;
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public GenericUsersFragment getFragment() {
        GenericUsersFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.EXTRA_USER, user.id);
        fragment.setArguments(args);
        return fragment;
    }

    // Setup

    @Override
    public void setupTopBar() {
        super.setupTopBar();

        getSupportActionBar().setTitle(R.string.followers);
    }

    public static class ContentFragment extends ListUsersFragment {

        private int userId;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            Bundle arguments = getArguments();
            this.userId = arguments.getInt(Constants.EXTRA_USER);
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void loadItems(boolean isFirstLoad, int offset, GTResponseHandler handler) {
            GTQueryParameters parameters = new GTQueryParameters();
            parameters.addParameter(GTQueryParameters.GTParameterType.offset, offset);
            parameters.addParameter(GTQueryParameters.GTParameterType.limit, GTConstants.MAX_ITEMS);
            GTSDK.getUserManager().getFollowers(userId, isFirstLoad, parameters, handler);
        }
    }
}
