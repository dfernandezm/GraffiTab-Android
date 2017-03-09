package com.graffitab.ui.activities.home.streamables.explorer.staticcontainers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.ui.activities.custom.streamables.ToggleStreamablesActivity;
import com.graffitab.ui.fragments.streamables.GenericStreamablesFragment;
import com.graffitab.ui.fragments.streamables.StaticStreamablesFragment;
import com.graffitabsdk.model.GTStreamable;

import java.util.ArrayList;

/**
 * Created by georgichristov on 23/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class StaticStreamablesActivity extends ToggleStreamablesActivity {

    private ArrayList<GTStreamable> staticItems;

    public static void open(Context context, ArrayList<GTStreamable> streamables) {
        Intent i = new Intent(context, StaticStreamablesActivity.class);
        i.putExtra(Constants.EXTRA_STREAMABLES, streamables);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getSerializable(Constants.EXTRA_STREAMABLES) != null) {
            staticItems = (ArrayList<GTStreamable>) extras.getSerializable(Constants.EXTRA_STREAMABLES);
        }
        else {
            finish();
            return;
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_fragment_holder;
    }

    @Override
    public GenericStreamablesFragment getFragment() {
        GenericStreamablesFragment fragment = new StaticStreamablesFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.EXTRA_STREAMABLES, staticItems);
        fragment.setArguments(args);
        return fragment;
    }

    // Setup

    @Override
    public void setupTopBar() {
        super.setupTopBar();
        getSupportActionBar().setTitle(getString(R.string.static_streamables_count, staticItems.size()));
    }

}
