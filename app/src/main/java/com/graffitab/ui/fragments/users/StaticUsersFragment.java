package com.graffitab.ui.fragments.users;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.graffitab.constants.Constants;
import com.graffitabsdk.model.GTUser;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.network.service.user.response.GTListUsersResponse;
import okhttp3.internal.huc.HttpsURLConnectionImpl;

import java.util.ArrayList;

/**
 * Created by david on 28/03/2017.
 */

public class StaticUsersFragment extends ListUsersFragment {

        private ArrayList<GTUser> staticItems;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            Bundle arguments = getArguments();
            this.staticItems = (ArrayList<GTUser>) arguments.getSerializable(Constants.EXTRA_USERS);
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void loadItems(boolean isFirstLoad, int offset, GTResponseHandler handler) {
            canLoadMore = false;
            GTListUsersResponse response = new GTListUsersResponse();
            response.items = staticItems;
            response.resultsCount = staticItems.size();
            response.limit = staticItems.size();
            response.offset = 0;

            GTResponse<GTListUsersResponse> resp = new GTResponse<>();
            resp.setObject(response);
            resp.setIsSuccessful(true);
            resp.setStatusCode(HttpsURLConnectionImpl.HTTP_OK);
            handler.onSuccess(resp);
        }
}
