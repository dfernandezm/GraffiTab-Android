package com.graffitab.ui.adapters.users;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.IntRange;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.graffitab.R;
import com.graffitab.graffitabsdk.model.GTUser;
import com.graffitab.ui.adapters.users.viewholders.ListUserViewHolder;
import com.graffitab.ui.views.recyclerview.components.CustomRecyclerViewAdapter;

import java.util.List;

/**
 * Created by georgichristov on 15/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class ListUsersRecyclerViewAdapter extends CustomRecyclerViewAdapter<GTUser> {

    public ListUsersRecyclerViewAdapter(Context context, List<GTUser> items) {
        super(context, items);
    }

    @Override
    public RecyclerView.ViewHolder onCreateCustomViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user_list, parent, false);
        ListUserViewHolder rcv = new ListUserViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindCustomViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListUserViewHolder customHolder = (ListUserViewHolder) holder;

        final GTUser item = getItem(position);
        customHolder.setItem(item);
    }

    public static class RecyclerViewMargin extends RecyclerView.ItemDecoration {

        private final int columns;

        public RecyclerViewMargin(@IntRange(from = 0) int columns) {
            this.columns = columns;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {}
    }
}
