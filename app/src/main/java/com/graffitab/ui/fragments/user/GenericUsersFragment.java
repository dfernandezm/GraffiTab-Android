package com.graffitab.ui.fragments.user;

import com.graffitab.R;
import com.graffitab.graffitabsdk.model.GTUser;
import com.graffitab.ui.fragments.GenericItemListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public abstract class GenericUsersFragment extends GenericItemListFragment<GTUser> {

    public enum ViewType {LIST_FULL}

    private ViewType viewType;

    public void basicInit() {
        setViewType(ViewType.LIST_FULL);
    }

    @Override
    public int emptyViewImageResource() {
        return -1;
    }

    @Override
    public String emptyViewTitle() {
        return getString(R.string.other_empty_no_users);
    }

    @Override
    public String emptyViewSubtitle() {
        return getString(R.string.other_empty_no_users_description);
    }

    public void setViewType(ViewType type) {
        this.viewType = type;
        configureLayout();
    }

    // Loading

    @Override
    public List<GTUser> generateDummyData() {
        List<GTUser> loaded = new ArrayList();
        Random random = new Random();
        for (int i = 0; i < 25; i++) {
            GTUser user = new GTUser();
            user.followedByCurrentUser = random.nextBoolean();
            loaded.add(user);
        }
        return loaded;
    }
}
