package com.graffitab.ui.activities.home.me;

import com.graffitab.R;
import com.graffitab.ui.activities.custom.streamables.ToggleStreamablesActivity;
import com.graffitab.ui.fragments.streamables.GenericStreamablesFragment;
import com.graffitab.ui.fragments.streamables.GridStreamablesFragment;
import com.graffitabsdk.constants.GTConstants;
import com.graffitabsdk.network.common.params.GTQueryParameters;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.sdk.GTSDK;
import com.graffitabsdk.sdk.events.streamables.GTStreamableMarkedPrivateEvent;
import com.graffitabsdk.sdk.events.streamables.GTStreamableMarkedPublicEvent;
import com.squareup.otto.Subscribe;

/**
 * Created by georgichristov on 04/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class PrivateStreamablesActivity extends ToggleStreamablesActivity {

    @Override
    public GenericStreamablesFragment getFragment() {
        return new ContentFragment();
    }

    // Setup

    @Override
    public void setupTopBar() {
        super.setupTopBar();

        getSupportActionBar().setTitle(R.string.private_posts);
    }

    public static class ContentFragment extends GridStreamablesFragment {

        private Object eventListener;

        @Override
        public void basicInit() {
            super.basicInit();
            eventListener = new Object() {

                @Subscribe
                public void streamableMarkedPrivateEvent(GTStreamableMarkedPrivateEvent event) {
                    if (!items.contains(event.getStreamable())) {
                        items.add(0, event.getStreamable());
                        adapter.setItems(items, getRecyclerView().getRecyclerView());
                    }
                }

                @Subscribe
                public void streamableMarkedPublicEvent(GTStreamableMarkedPublicEvent event) {
                    int index = items.indexOf(event.getStreamable());
                    if (index >= 0)
                        removeItemAtIndex(index);
                }
            };
            GTSDK.registerEventListener(eventListener);
        }

        @Override
        public void onDestroyView() {
            GTSDK.unregisterEventListener(eventListener);
            super.onDestroyView();
        }

        @Override
        public void loadItems(boolean isFirstLoad, int offset, GTResponseHandler handler) {
            GTQueryParameters parameters = new GTQueryParameters();
            parameters.addParameter(GTQueryParameters.GTParameterType.OFFSET, offset);
            parameters.addParameter(GTQueryParameters.GTParameterType.LIMIT, GTConstants.MAX_ITEMS);
            GTSDK.getMeManager().getPrivatePosts(isFirstLoad, parameters, handler);
        }
    }
}
