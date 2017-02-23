package com.graffitab.ui.activities.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.ui.adapters.viewpagers.ViewPagerTabAdapter;
import com.graffitab.ui.fragments.search.SearchGraffitiFragment;
import com.graffitab.ui.fragments.search.SearchUsersFragment;
import com.graffitab.utils.image.ImageUtils;
import com.graffitab.utils.input.KeyboardUtils;

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

    private SearchUsersFragment searchUsersFragment;
    private SearchGraffitiFragment searchGraffitiFragment;

    public static void openSearch(Context context, String query) {
        Intent i = new Intent(context, SearchActivity.class);
        if (query != null)
            i.putExtra(Constants.EXTRA_SEARCH_REQUEST, query);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        // Check for search requests.
        String searchRequest = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            searchRequest = extras.getString(Constants.EXTRA_SEARCH_REQUEST);
            searchRequest = searchRequest.replace("@", "");
            searchRequest = searchRequest.replace("#", "");
        }

        setupTopBar();
        setupViewPager(searchRequest);
        setupTabBar();
        setupSearchView(searchRequest);

        if (searchRequest != null)
            viewPager.setCurrentItem(1);
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

    private void setupViewPager(String searchRequest) {
        searchUsersFragment = new SearchUsersFragment();
        searchGraffitiFragment = new SearchGraffitiFragment();
        if (searchRequest != null) {
            searchUsersFragment.presetSearchQuery(searchRequest);
            searchGraffitiFragment.presetSearchQuery(searchRequest);
        }

        final ViewPagerTabAdapter adapter = new ViewPagerTabAdapter(getSupportFragmentManager());
        adapter.addFragment(searchUsersFragment, getString(R.string.search_people));
        adapter.addFragment(searchGraffitiFragment, getString(R.string.search_graffiti));
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
    }

    private void setupTabBar() {
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText(R.string.search_people);
        tabLayout.getTabAt(1).setText(R.string.search_graffiti);
    }

    private void setupSearchView(String searchRequest) {
        Drawable img = ImageUtils.tintIcon(this, R.drawable.ic_search_white_24dp, getResources().getColor(R.color.colorPrimary));
        img.setBounds(0, 0, img.getIntrinsicWidth(), img.getIntrinsicHeight());
        searchView.setCompoundDrawables(img, null, null, null);
        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyboardUtils.hideKeyboard(SearchActivity.this);
                    String query = textView.getText().toString().trim();
                    query = query.replace("@", "");
                    query = query.replace("#", "");

                    searchUsersFragment.search(query);
                    searchGraffitiFragment.search(query);
                }
                return true;
            }
        });

        if (searchRequest != null)
            searchView.setText(searchRequest);
    }
}
