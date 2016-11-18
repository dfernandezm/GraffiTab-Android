package com.graffitab.ui.fragments.home;

import com.graffitab.R;
import com.graffitab.ui.fragments.notification.ListNotificationsFragment;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class NotificationsFragment extends ListNotificationsFragment {

    @Override
    public String emptyViewTitle() {
        return getString(R.string.notifications_empty_title);
    }

    @Override
    public String emptyViewSubtitle() {
        return getString(R.string.notifications_empty_description);
    }
}
