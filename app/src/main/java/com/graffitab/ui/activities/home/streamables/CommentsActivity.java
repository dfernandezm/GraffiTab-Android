package com.graffitab.ui.activities.home.streamables;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.graffitab.constants.Constants;
import com.graffitab.ui.activities.custom.comments.GenericCommentsActivity;
import com.graffitab.ui.fragments.comments.GenericCommentsFragment;
import com.graffitab.ui.fragments.comments.ListCommentsFragment;
import com.graffitabsdk.model.GTStreamable;

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
        GenericCommentsFragment fragment = new ListCommentsFragment();
        fragment.setStreamable(streamable);
        return fragment;
    }
}
