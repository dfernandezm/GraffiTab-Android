package com.graffitab.ui.activities.home.me;

import com.graffitab.R;
import com.graffitab.ui.activities.custom.followersactivity.BaseFollowersActivity;
import com.graffitab.ui.fragments.followersactivity.GenericFollowersActivityFragment;
import com.graffitabsdk.constants.GTConstants;
import com.graffitabsdk.network.common.params.GTQueryParameters;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.sdk.GTSDK;

/**
 * Created by david on 16/02/2017.
 */

public class FollowersActivity extends BaseFollowersActivity {

    @Override
    public GenericFollowersActivityFragment getFragment() {
        return new ContentFragment();
    }

    // Setup

    @Override
    public void setupTopBar() {
        super.setupTopBar();
        getSupportActionBar().setTitle(R.string.followers);
    }

    public static class ContentFragment extends GenericFollowersActivityFragment {

        @Override
        public void loadItems(boolean isFirstLoad, int offset, GTResponseHandler handler) {
            GTQueryParameters parameters = new GTQueryParameters();
            parameters.addParameter(GTQueryParameters.GTParameterType.offset, offset)
                    .addParameter(GTQueryParameters.GTParameterType.limit, GTConstants.MAX_ITEMS)
                    .addParameter(GTQueryParameters.GTParameterType.numberOfItemsInGroup, GTConstants.NUMBER_OF_ITEMS_IN_GROUP);

           GTSDK.getMeManager().getFollowersActivity(isFirstLoad, parameters, handler);
        }
    }
}
