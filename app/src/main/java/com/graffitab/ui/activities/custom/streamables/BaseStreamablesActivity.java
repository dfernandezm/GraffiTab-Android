package com.graffitab.ui.activities.custom.streamables;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;

import com.graffitab.R;
import com.graffitab.ui.fragments.streamables.GenericStreamablesFragment;

import butterknife.ButterKnife;

/**
 * Created by georgichristov on 24/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public abstract class BaseStreamablesActivity extends AppCompatActivity {

    private GenericStreamablesFragment content;

    public abstract GenericStreamablesFragment getFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResId());
        ButterKnife.bind(this);

        setupTopBar();
        setupContent(getFragment());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public int getLayoutResId() {
        return R.layout.activity_fragment_holder;
    }

    public GenericStreamablesFragment getContent() {
        return content;
    }

    // Setup

    public void setupTopBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setupContent(GenericStreamablesFragment contentFragment) {
        content = contentFragment;
        content.hasOptionsMenu = true;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, content);
        transaction.commit();
    }
}
