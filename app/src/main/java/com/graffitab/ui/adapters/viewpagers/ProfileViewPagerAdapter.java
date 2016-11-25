package com.graffitab.ui.adapters.viewpagers;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.graffitab.R;

/**
 * Created by georgichristov on 25/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class ProfileViewPagerAdapter extends PagerAdapter {

    private Context context;
    private ViewPager pager;

    public ProfileViewPagerAdapter(Context context, ViewPager pager) {
        this.context = context;
        this.pager = pager;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);

        ViewGroup layout;
        if (position == 0) {
            layout = (ViewGroup) inflater.inflate(R.layout.pager_user_profile_empty, container, false);
            container.addView(layout);
        }
        else {
            layout = (ViewGroup) inflater.inflate(R.layout.pager_user_profile_about, container, false);
            container.addView(layout);
        }
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public static class ProfilePagerChangeListener implements ViewPager.OnPageChangeListener {

        private ViewPager pager;
        private int previousPosition = 0;

        public ProfilePagerChangeListener(ViewPager pager) {
            this.pager = pager;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (position == previousPosition)
                pager.setBackgroundColor(Color.argb((int)(positionOffset * 120), 0, 0, 0));
            previousPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public void onPageSelected(int position) {
        }
    }
}
