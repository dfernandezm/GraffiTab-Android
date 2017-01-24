package com.graffitab.ui.activities.home.streamables.explorer.staticcontainers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.graffitab.R;
import com.graffitab.ui.activities.custom.streamables.GenericStreamablesActivity;
import com.graffitab.ui.fragments.streamables.GenericStreamablesFragment;
import com.graffitab.utils.activity.ActivityUtils;
import com.graffitab.utils.display.DisplayUtils;
import com.graffitabsdk.model.GTStreamable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by georgichristov on 23/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class ClusterActivity extends GenericStreamablesActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;

    private List<GTStreamable> staticItems = new ArrayList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (DisplayUtils.isTablet(this))
            ActivityUtils.showAsPopup(this, DisplayUtils.pxToDip(this, 500), DisplayUtils.pxToDip(this, 450));
        else
            ActivityUtils.showAsPopup(this, DisplayUtils.pxToDip(this, 350), DisplayUtils.pxToDip(this, 350));
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_dialog_fragment_holder;
    }

    @Override
    public GenericStreamablesFragment getFragment() {
        return null;
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
