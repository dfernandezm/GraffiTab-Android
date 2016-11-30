package com.graffitab.ui.adapters.profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.graffitab.graffitabsdk.model.GTStreamable;
import com.graffitab.ui.adapters.streamables.GenericStreamablesRecyclerViewAdapter;

import java.util.List;

/**
 * Created by georgichristov on 25/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class UserProfileHeaderAdapter extends GenericStreamablesRecyclerViewAdapter {

    public UserProfileHeaderAdapter(Context context, List<GTStreamable> items, RecyclerView recyclerView) {
        super(context, items, recyclerView);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindHeaderViewHolder(holder, position);
    }
}
