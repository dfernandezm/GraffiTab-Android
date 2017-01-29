package com.graffitab.ui.activities.home.streamables;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.graffitab.constants.Constants;
import com.graffitab.ui.activities.custom.comments.GenericCommentsActivity;
import com.graffitab.ui.fragments.comments.GenericCommentsFragment;
import com.graffitab.ui.fragments.comments.ListCommentsFragment;
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
public class CommentsActivity extends GenericCommentsActivity {

    private GTStreamable streamable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            streamable = (GTStreamable) extras.getSerializable(Constants.EXTRA_STREAMABLE);
        }
        else finish();

        super.onCreate(savedInstanceState);
    }

    @Override
    public GenericCommentsFragment getFragment() {
        GenericCommentsFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.EXTRA_STREAMABLE, streamable.id);
        fragment.setArguments(args);
        return fragment;
    }

    public static class ContentFragment extends ListCommentsFragment {

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
            GTSDK.getStreamableManager().getComments(streamableId, isFirstLoad, parameters, handler);
        }
    }
}
