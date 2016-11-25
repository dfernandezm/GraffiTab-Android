package com.graffitab.ui.adapters.profile;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;

import com.graffitab.R;
import com.graffitab.graffitabsdk.model.GTStreamable;
import com.graffitab.ui.adapters.streamables.GridStreamablesRecyclerViewAdapter;
import com.graffitab.ui.adapters.viewpagers.ProfileViewPagerAdapter;

import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by georgichristov on 25/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class UserProfileHeaderGridAdapter extends GridStreamablesRecyclerViewAdapter {

    public UserProfileHeaderGridAdapter(Context context, List<GTStreamable> items) {
        super(context, items);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindHeaderViewHolder(holder, position);

        ViewPager viewPager = (ViewPager) holder.itemView.findViewById(R.id.viewpager);
        CircleIndicator circleIndicator = (CircleIndicator) holder.itemView.findViewById(R.id.indicator);

        PagerAdapter adapter = new ProfileViewPagerAdapter(context, viewPager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ProfileViewPagerAdapter.ProfilePagerChangeListener(viewPager));
        circleIndicator.setViewPager(viewPager);
    }
}
