package com.graffitab.ui.fragments.user.profile;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.graffitab.R;
import com.graffitab.ui.fragments.streamable.ListStreamablesFragment;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecyclerViewItemDecoration;
import com.graffitab.utils.ImageUtils;

/**
 * Created by georgichristov on 21/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class UserProfileFragment extends ListStreamablesFragment {

    private Drawable actionBarDrawable;

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
                    outRect.bottom = 2;
                }
                else {
                    AdvancedRecyclerViewItemDecoration decoration = (AdvancedRecyclerViewItemDecoration) UserProfileFragment.super.getItemDecoration();
                    decoration.setPadEdges(false);
                    decoration.getItemOffsets(outRect, view, parent, state);
                }
            }
        };
    }

    @Override
    public void setupCustomViews() {
        super.setupCustomViews();

        adapter.addHeaderView(R.layout.decoration_header_profile, advancedRecyclerView.getRecyclerView());

        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        final ActionBar actionBar = activity.getSupportActionBar();

        if (actionBarDrawable == null) { // This can be called when viewType is switching, so make sure it is initialized only once.
            actionBarDrawable = new ColorDrawable(0xffffffff);
            actionBarDrawable.setAlpha(0);
            actionBar.setBackgroundDrawable(actionBarDrawable);
        }

        getRecyclerView().getRecyclerView().setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                RecyclerView.ViewHolder holder = recyclerView.findViewHolderForLayoutPosition(0);
                if (holder != null) {
                    int offset = -holder.itemView.getTop();

                    final int headerHeight = holder.itemView.getHeight() / 2 - actionBar.getHeight();
                    final float ratio = (float) Math.min(Math.max(offset, 0), headerHeight) / headerHeight;
                    final int newAlpha = (int) (ratio * 255);
                    actionBarDrawable.setAlpha(newAlpha);

                    if (offset > 400) {
                        String title = "Georgi Christov";
                        String subtitle = "@georgi";

                        Spannable coloredTitle = new SpannableString(title);
                        coloredTitle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 0, coloredTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        Spannable coloredSubtitle = new SpannableString(subtitle);
                        coloredSubtitle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorMetadata)), 0, coloredSubtitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                        activity.getSupportActionBar().setTitle(coloredTitle);
                        activity.getSupportActionBar().setSubtitle(coloredSubtitle);

                        activity.getSupportActionBar().setHomeAsUpIndicator(ImageUtils.tintIcon(getContext(), R.drawable.ic_arrow_back_black_24dp, getResources().getColor(R.color.colorPrimary)));
                    }
                    else {
                        activity.getSupportActionBar().setTitle("");
                        activity.getSupportActionBar().setSubtitle("");

                        activity.getSupportActionBar().setHomeAsUpIndicator(ImageUtils.tintIcon(getContext(), R.drawable.ic_arrow_back_black_24dp, getResources().getColor(R.color.colorWhite)));
                    }
                }
            }
        });
    }
}
