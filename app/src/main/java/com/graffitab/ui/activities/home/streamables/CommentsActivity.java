package com.graffitab.ui.activities.home.streamables;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.ui.activities.custom.comments.BaseCommentsActivity;
import com.graffitab.ui.fragments.comments.GenericCommentsFragment;
import com.graffitab.ui.fragments.comments.ListCommentsFragment;
import com.graffitab.utils.input.KeyboardUtils;
import com.graffitabsdk.model.GTStreamable;

import butterknife.BindView;

/**
 * Created by georgichristov on 09/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class CommentsActivity extends BaseCommentsActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;

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
    public int getLayoutResId() {
        return R.layout.activity_dialog_fragment_holder;
    }

    @Override
    protected void onPause() {
        KeyboardUtils.hideKeyboard(this);
        super.onPause();
    }

    @Override
    public GenericCommentsFragment getFragment() {
        GenericCommentsFragment fragment = new ListCommentsFragment();
        fragment.setStreamable(streamable);
        return fragment;
    }

    // Setup

    @Override
    public void setupTopBar() {
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);
        super.setupTopBar();
    }
}
