package com.graffitab.ui.activities.home.me;

import com.graffitab.R;
import com.graffitab.ui.activities.custom.followersactivity.BaseFollowersActivity;
import com.graffitab.ui.fragments.streamables.GenericStreamablesFragment;
import com.graffitab.ui.fragments.streamables.GridStreamablesFragment;
import com.graffitabsdk.constants.GTConstants;
import com.graffitabsdk.network.common.params.GTQueryParameters;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.sdk.GTSDK;

/**
 * Created by david on 16/02/2017.
 */

public class FollowersActivity extends BaseFollowersActivity {

    //TODO: followers fragment
    @Override
    public GenericStreamablesFragment getFragment() {
        return new ContentFragment();
    }

    // Setup

    @Override
    public void setupTopBar() {
        super.setupTopBar();
        getSupportActionBar().setTitle(R.string.followers);
    }

    //TODO: Extend the GenericFollowersActivityFragment directly
    public static class ContentFragment extends GridStreamablesFragment {

        @Override
        public void loadItems(boolean isFirstLoad, int offset, GTResponseHandler handler) {
            GTQueryParameters parameters = new GTQueryParameters();
            parameters.addParameter(GTQueryParameters.GTParameterType.OFFSET, offset);
            parameters.addParameter(GTQueryParameters.GTParameterType.LIMIT, GTConstants.MAX_ITEMS);

            //TODO: getFollowersActivity on the SDK
            GTSDK.getMeManager().getPrivatePosts(isFirstLoad, parameters, handler);
        }
    }
}
