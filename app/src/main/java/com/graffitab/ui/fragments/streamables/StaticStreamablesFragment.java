package com.graffitab.ui.fragments.streamables;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.graffitab.constants.Constants;
import com.graffitabsdk.model.GTStreamable;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.network.service.streamable.response.GTListStreamablesResponse;

import java.util.ArrayList;

import okhttp3.internal.huc.HttpsURLConnectionImpl;

/**
 * Created by david on 28/03/2017.
 */

public class StaticStreamablesFragment extends GridStreamablesFragment {

    private ArrayList<GTStreamable> staticItems;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        this.staticItems = (ArrayList<GTStreamable>) arguments.getSerializable(Constants
                .EXTRA_STREAMABLES);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void loadItems(boolean isFirstLoad, int offset, GTResponseHandler handler) {
        canLoadMore = false;
        GTListStreamablesResponse response = new GTListStreamablesResponse();
        response.items = staticItems;
        response.resultsCount = staticItems.size();
        response.limit = staticItems.size();
        response.offset = 0;

        GTResponse<GTListStreamablesResponse> resp = new GTResponse<>();
        resp.setObject(response);
        resp.setIsSuccessful(true);
        resp.setStatusCode(HttpsURLConnectionImpl.HTTP_OK);
        handler.onSuccess(resp);
    }
}
