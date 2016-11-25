package com.graffitab.ui.activities.home;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.graffitab.R;
import com.graffitab.ui.adapters.viewpagers.ViewPagerTabAdapter;
import com.graffitab.ui.fragments.search.SearchGraffitiFragment;
import com.graffitab.ui.fragments.search.SearchUsersFragment;
import com.graffitab.utils.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 13/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.viewpager) ViewPager viewPager;

    EditText searchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        setupTopBar();
        setupViewPager();
        setupTabBar();
        setupSearchView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Setup

    private void setupTopBar() {
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");

        View searchLayout = LayoutInflater.from(this).inflate(R.layout.view_search, null);
        searchView = (EditText) searchLayout.findViewById(R.id.searchView);
        toolbar.addView(searchLayout);
    }

    private void setupViewPager() {
        final ViewPagerTabAdapter adapter = new ViewPagerTabAdapter(getSupportFragmentManager());
        adapter.addFragment(new SearchUsersFragment(), getString(R.string.search_people));
        adapter.addFragment(new SearchGraffitiFragment(), getString(R.string.search_graffiti));
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
    }

    private void setupTabBar() {
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText(R.string.search_people);
        tabLayout.getTabAt(1).setText(R.string.search_graffiti);
    }

    private void setupSearchView() {
        Drawable img = ImageUtils.tintIcon(this, R.drawable.ic_search_white_24dp, getResources().getColor(R.color.colorPrimary));
        img.setBounds(0, 0, img.getIntrinsicWidth(), img.getIntrinsicHeight());
        searchView.setCompoundDrawables(img, null, null, null);
    }
}
