package com.graffitab.ui.activities.home.users;

import com.graffitab.R;
import com.graffitab.ui.activities.custom.users.BaseUsersActivity;
import com.graffitab.ui.fragments.users.GenericUsersFragment;
import com.graffitab.ui.fragments.users.SocialFriendsFragment;
import com.graffitabsdk.constants.GTConstants;
import com.graffitabsdk.model.GTUser;
import com.graffitabsdk.model.GTUserSocialFriendsContainer;
import com.graffitabsdk.network.common.params.GTQueryParameters;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.network.common.result.GTListItemsResult;
import com.graffitabsdk.sdk.GTSDK;

/**
 * Created by david on 05/04/2017.
 */

public class FacebookFriendsActivity extends BaseUsersActivity {

    @Override
    public void setupTopBar() {
        super.setupTopBar();
        getSupportActionBar().setTitle(R.string.facebook_friends_title);
    }

    @Override
    public GenericUsersFragment getFragment() {
       return new ContentFragment();
    }

    public static class ContentFragment extends SocialFriendsFragment {

        @Override
        public void loadItems(boolean isFirstLoad, int offset, final GTResponseHandler handler) {
            GTQueryParameters parameters = new GTQueryParameters();
            parameters.addParameter(GTQueryParameters.GTParameterType.offset, offset);
            parameters.addParameter(GTQueryParameters.GTParameterType.limit, GTConstants.MAX_ITEMS);
            GTSDK.getMeManager().findFacebookFriends(false, parameters, new GTResponseHandler<GTUserSocialFriendsContainer>() {
                @Override
                public void onSuccess(GTResponse gtResponse) {
                    // Massive hack to get 'users' into 'items' to follow the Generic Lists framework
                    GTUserSocialFriendsContainer socialFriendsContainer = (GTUserSocialFriendsContainer) gtResponse.getObject();

                    ((GTListItemsResult<GTUser>) gtResponse.getObject()).items = socialFriendsContainer.users;;
                    handler.onSuccess(gtResponse);
                }

                @Override
                public void onFailure(GTResponse gtResponse) {
                    handler.onFailure(gtResponse);
                }

                @Override
                public void onCache(GTResponse gtResponse) {
                    handler.onCache(gtResponse);
                }
            });
        }
    }
}
