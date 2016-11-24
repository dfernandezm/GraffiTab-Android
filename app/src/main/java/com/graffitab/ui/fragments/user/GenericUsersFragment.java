package com.graffitab.ui.fragments.user;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.graffitab.R;
import com.graffitab.graffitabsdk.model.GTUser;
import com.graffitab.ui.adapters.users.ListUsersRecyclerViewAdapter;
import com.graffitab.ui.fragments.GenericItemListFragment;
import com.graffitab.ui.views.recyclerview.components.AdvancedEndlessRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecyclerViewItemDecoration;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecyclerViewLayoutConfiguration;

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
    private ViewType previousViewType;
    private boolean initialViewTypeSet = false;

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
        if (previousViewType == null || previousViewType != type) {
            this.viewType = type;
            this.previousViewType = this.viewType;

            if (initialViewTypeSet) // Only reset views once initial viewType has been set.
                didChangeViewType();
        }

        initialViewTypeSet = true; // After the first layout pass, we allow changing the view type.
    }

    // Configuration

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        if (viewType == ViewType.LIST_FULL)
            return new AdvancedRecyclerViewItemDecoration(1, 0);
        return null;
    }

    @Override
    public AdvancedEndlessRecyclerViewAdapter getAdapterForViewType() {
        if (getActivity() == null)
            return null;

        if (viewType == ViewType.LIST_FULL)
            return new ListUsersRecyclerViewAdapter(getActivity(), items);
        return null;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManagerForViewType() {
        if (viewType == ViewType.LIST_FULL)
            return new LinearLayoutManager(getContext());
        return null;
    }

    @Override
    public AdvancedRecyclerViewLayoutConfiguration getLayoutConfiguration() {
        return null;
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
