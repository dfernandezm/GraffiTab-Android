package com.graffitab.ui.activities.home.streamables.explorer.staticcontainers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.ui.activities.custom.streamables.BaseStreamablesActivity;
import com.graffitab.ui.fragments.streamables.GenericStreamablesFragment;
import com.graffitab.ui.fragments.streamables.GridStreamablesFragment;
import com.graffitab.utils.activity.ActivityUtils;
import com.graffitab.utils.device.DeviceUtils;
import com.graffitabsdk.model.GTStreamable;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.network.service.streamable.response.GTListStreamablesResponse;

import java.util.ArrayList;

import butterknife.BindView;
import okhttp3.internal.huc.HttpsURLConnectionImpl;

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
        i.putExtra(Constants.EXTRA_CLUSTER, items);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getSerializable(Constants.EXTRA_CLUSTER) != null) {
            staticItems = (ArrayList<GTStreamable>) extras.getSerializable(Constants.EXTRA_CLUSTER);
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
        GenericStreamablesFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.EXTRA_CLUSTER, staticItems);
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

    public static class ContentFragment extends GridStreamablesFragment {

        private ArrayList<GTStreamable> staticItems;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            Bundle arguments = getArguments();
            this.staticItems = (ArrayList<GTStreamable>) arguments.getSerializable(Constants.EXTRA_CLUSTER);
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void loadItems(boolean isFirstLoad, int offset, GTResponseHandler handler) {
            GTListStreamablesResponse response = new GTListStreamablesResponse();
            response.items = staticItems;
            response.resultsCount = staticItems.size();
            response.limit = staticItems.size();
            response.offset = 0;

            // Create fake response, containing the local cluster items.
            GTResponse<GTListStreamablesResponse> resp = new GTResponse<>();
            resp.setObject(response);
            resp.setIsSuccessful(true);
            resp.setStatusCode(HttpsURLConnectionImpl.HTTP_OK);
            handler.onSuccess(resp);
            canLoadMore = false;
        }
    }
}
