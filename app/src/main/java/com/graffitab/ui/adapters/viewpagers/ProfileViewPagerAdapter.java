package com.graffitab.ui.adapters.viewpagers;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitabsdk.model.GTUser;

/**
 * Created by georgichristov on 25/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class ProfileViewPagerAdapter extends PagerAdapter {

    private TextView aboutField;
    private TextView websiteField;
    private View separator;

    private Context context;
    private ViewPager pager;
    private GTUser user;

    public ProfileViewPagerAdapter(Context context, ViewPager pager, GTUser user) {
        this.context = context;
        this.pager = pager;
        this.user = user;
    }

    public void setUser(GTUser user) {
        this.user = user;
        loadUserAboutData();
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

            aboutField = (TextView) layout.findViewById(R.id.about);
            websiteField = (TextView) layout.findViewById(R.id.website);
            separator = layout.findViewById(R.id.separator);

            loadUserAboutData();

            container.addView(layout);
        }
        return layout;
    }

    private void loadUserAboutData() {
        separator.setVisibility(user.about != null && user.website != null ? View.VISIBLE : View.GONE);
        aboutField.setVisibility(user.about != null ? View.VISIBLE : View.GONE);
        aboutField.setText(user.about != null ? user.about : "");
        websiteField.setVisibility(user.website != null ? View.VISIBLE : View.GONE);
        websiteField.setText(user.website != null ? user.website : "");
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
            if (position == previousPosition && positionOffset > 0) // Dim background when pager is swiped.
                pager.setBackgroundColor(Color.argb((int)(positionOffset * 120), 0, 0, 0));
            previousPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {}

        @Override
        public void onPageSelected(int position) {}
    }
}
