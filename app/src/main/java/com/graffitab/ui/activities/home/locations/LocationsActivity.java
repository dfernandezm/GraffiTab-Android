package com.graffitab.ui.activities.home.locations;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;

import com.github.clans.fab.FloatingActionButton;
import com.graffitab.R;
import com.graffitab.ui.fragments.location.ListLocationsFragment;

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
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_locations);
        ButterKnife.bind(this);

        setupTopBar();
        setupContent();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_home, menu);
//
//        for (int i = 0; i < menu.size(); i++) {
//            MenuItem menuItem = menu.getItem(i);
//            Drawable drawable = menuItem.getIcon();
//            if (drawable != null) {
//                drawable.mutate();
//                drawable.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
//            }
//        }
//
//        return true;
//    }

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
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new ListLocationsFragment());
        transaction.commit();
    }
}
