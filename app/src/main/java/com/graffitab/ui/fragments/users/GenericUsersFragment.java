package com.graffitab.ui.fragments.users;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.graffitab.R;
import com.graffitab.application.MyApplication;
import com.graffitab.ui.activities.home.users.ProfileActivity;
import com.graffitab.ui.adapters.users.GenericUsersRecyclerViewAdapter;
import com.graffitab.ui.adapters.users.OnUserClickListener;
import com.graffitab.ui.adapters.users.viewholders.UserViewHolder;
import com.graffitab.ui.fragments.GenericItemListFragment;
import com.graffitab.ui.views.recyclerview.components.AdvancedEndlessRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecyclerViewItemDecoration;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecyclerViewLayoutConfiguration;
import com.graffitabsdk.model.GTUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public abstract class GenericUsersFragment extends GenericItemListFragment<GTUser> implements OnUserClickListener {

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
    }

    public void switchViewType(ViewType type) {
        setViewType(type);

        ((GenericUsersRecyclerViewAdapter) adapter).setViewType(viewType);
        configureLayout();
    }

    @Override
    public void onRowSelected(GTUser user, int adapterPosition) {
        startActivity(new Intent(getActivity(), ProfileActivity.class));
    }

    @Override
    public void onToggleFollow(GTUser user, UserViewHolder holder, int adapterPosition) {
        user.followedByCurrentUser = !user.followedByCurrentUser;
        adapter.notifyDataSetChanged();
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
        GenericUsersRecyclerViewAdapter customAdapter = new GenericUsersRecyclerViewAdapter(MyApplication.getInstance(), items, getRecyclerView().getRecyclerView());
        customAdapter.setViewType(viewType);
        customAdapter.setClickListener(this);
        return customAdapter;
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
