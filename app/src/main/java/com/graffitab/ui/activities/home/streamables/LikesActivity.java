package com.graffitab.ui.activities.home.streamables;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.ui.activities.custom.users.GenericUsersActivity;
import com.graffitab.ui.fragments.users.GenericUsersFragment;
import com.graffitab.ui.fragments.users.ListUsersFragment;
import com.graffitabsdk.config.GTSDK;
import com.graffitabsdk.constants.GTConstants;
import com.graffitabsdk.model.GTStreamable;
import com.graffitabsdk.network.common.params.GTQueryParameters;
import com.graffitabsdk.network.common.response.GTResponseHandler;

/**
 * Created by georgichristov on 09/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class LikesActivity extends GenericUsersActivity {

    private GTStreamable streamable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getSerializable(Constants.EXTRA_STREAMABLE) != null) {
            streamable = (GTStreamable) extras.getSerializable(Constants.EXTRA_STREAMABLE);
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
        args.putInt(Constants.EXTRA_STREAMABLE, streamable.id);
        fragment.setArguments(args);
        return fragment;
    }

    // Setup

    @Override
    public void setupTopBar() {
        super.setupTopBar();
        getSupportActionBar().setTitle(R.string.likes_likers);
    }

    public static class ContentFragment extends ListUsersFragment {

        private int streamableId;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            Bundle arguments = getArguments();
            this.streamableId = arguments.getInt(Constants.EXTRA_STREAMABLE);
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void loadItems(boolean isFirstLoad, int offset, GTResponseHandler handler) {
            GTQueryParameters parameters = new GTQueryParameters();
            parameters.addParameter(GTQueryParameters.GTParameterType.OFFSET, offset);
            parameters.addParameter(GTQueryParameters.GTParameterType.LIMIT, GTConstants.MAX_ITEMS);
            GTSDK.getStreamableManager().getLikers(streamableId, isFirstLoad, parameters, handler);
        }
    }
}
