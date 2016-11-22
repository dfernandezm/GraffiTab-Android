package com.graffitab.ui.fragments.user.profile;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.graffitab.R;
import com.graffitab.ui.fragments.streamable.ListStreamablesFragment;

/**
 * Created by georgichristov on 21/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class UserProfileFragment extends ListStreamablesFragment {

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_refresh, menu);

        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            Drawable drawable = menuItem.getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new RecyclerView.ItemDecoration() {

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int itemCount = state.getItemCount();
                int position = parent.getChildLayoutPosition(view);

                if (position == 0) {
                    outRect.top = 0;
                    outRect.left = 0;
                    outRect.right = 0;
                    outRect.bottom = -7;
                }
                else
                    UserProfileFragment.super.getItemDecoration().getItemOffsets(outRect, view, parent, state);
            }
        };
    }

    @Override
    public void setupCustomViews() {
        super.setupCustomViews();

        adapter.addHeaderView(R.layout.decoration_header_profile, advancedRecyclerView.getRecyclerView());
    }
}
