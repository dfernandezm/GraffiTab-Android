package com.graffitab.ui.activities.home.me.locations;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;

import com.github.clans.fab.FloatingActionButton;
import com.graffitab.R;
import com.graffitab.ui.fragments.location.LocationsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 20/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class LocationsActivity extends AppCompatActivity {

    @BindView(R.id.fab) FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_locations);
        ButterKnife.bind(this);

        setupTopBar();
        setupContent();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Setup

    private void setupTopBar() {
        getSupportActionBar().setTitle(getString(R.string.locations));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupContent() {
        LocationsFragment content = new LocationsFragment();
        content.hasOptionsMenu = true;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, content);
        transaction.commit();
    }
}
