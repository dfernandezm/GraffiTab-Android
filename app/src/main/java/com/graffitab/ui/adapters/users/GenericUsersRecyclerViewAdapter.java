package com.graffitab.ui.adapters.users;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.graffitab.R;
import com.graffitab.ui.adapters.users.viewholders.ListUserViewHolder;
import com.graffitab.ui.fragments.users.GenericUsersFragment.ViewType;
import com.graffitab.ui.views.recyclerview.components.AdvancedEndlessRecyclerViewAdapter;
import com.graffitabsdk.model.GTUser;

import java.util.List;

/**
 * Created by georgichristov on 15/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class GenericUsersRecyclerViewAdapter extends AdvancedEndlessRecyclerViewAdapter<GTUser> {

    private final int VIEW_TYPE_LIST_FULL = 0;

    private ViewType itemViewType;
    private OnUserClickListener clickListener;

    public GenericUsersRecyclerViewAdapter(Context context, List<GTUser> items, RecyclerView recyclerView) {
        super(context, items, recyclerView);
    }

    public void setViewType(ViewType type) {
        this.itemViewType = type;
    }

    public void setClickListener(OnUserClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public int getViewType(int position) {
        switch (itemViewType) {
            case LIST_FULL: return VIEW_TYPE_LIST_FULL;
        }

        return VIEW_TYPE_LIST_FULL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LIST_FULL) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user_list, parent, false);
            final ListUserViewHolder rcv = new ListUserViewHolder(layoutView);
            rcv.setClickListener(clickListener);
            return rcv;
        }
        else {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user_list, parent, false);
            final ListUserViewHolder rcv = new ListUserViewHolder(layoutView);
            rcv.setClickListener(clickListener);
            return rcv;
        }
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final GTUser item = getItem(position);

        if (holder instanceof ListUserViewHolder) {
            ListUserViewHolder customHolder = (ListUserViewHolder) holder;
            customHolder.setItem(item);
        }
    }
}
