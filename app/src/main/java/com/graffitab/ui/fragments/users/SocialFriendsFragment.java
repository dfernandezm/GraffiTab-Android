package com.graffitab.ui.fragments.users;

/**
 * Created by david on 05/04/2017.
 */

public abstract class SocialFriendsFragment extends ListUsersFragment {

    @Override
    public void basicInit() {
        super.basicInit();
        setViewType(ViewType.SOCIAL_FRIENDS);
    }
}
