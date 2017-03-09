package com.graffitab.ui.activities.home.streamables.explorer.staticcontainers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.ui.activities.custom.streamables.BaseStreamablesActivity;
import com.graffitab.ui.fragments.streamables.GenericStreamablesFragment;
import com.graffitab.ui.fragments.streamables.StaticStreamablesFragment;
import com.graffitab.utils.activity.ActivityUtils;
import com.graffitab.utils.device.DeviceUtils;
import com.graffitabsdk.model.GTStreamable;

import java.util.ArrayList;

/**
 * Created by georgichristov on 23/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class StaticClusterActivity extends BaseStreamablesActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;

    private ArrayList<GTStreamable> staticItems = new ArrayList();

    public static void openCluster(Context context, ArrayList<GTStreamable> items) {
        Intent i = new Intent(context, StaticClusterActivity.class);
        i.putExtra(Constants.EXTRA_STREAMABLES, items);
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

        if (DeviceUtils.isTablet(this))
            ActivityUtils.showAsPopup(this, DeviceUtils.pxToDip(this, 500), DeviceUtils.pxToDip(this, 450));
        else
            ActivityUtils.showAsPopup(this, DeviceUtils.pxToDip(this, 350), DeviceUtils.pxToDip(this, 350));
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_dialog_fragment_holder;
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
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);
        super.setupTopBar();
        getSupportActionBar().setTitle(getString(R.string.cluster_streamables_count, staticItems.size()));
    }
}
