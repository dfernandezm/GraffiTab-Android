package com.graffitab.ui.activities.custom.comments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;

import com.graffitab.R;
import com.graffitab.ui.fragments.comments.GenericCommentsFragment;
import com.graffitab.ui.fragments.comments.ListCommentsFragment;

import butterknife.ButterKnife;

/**
 * Created by georgichristov on 09/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class GenericCommentsActivity extends AppCompatActivity {

    private GenericCommentsFragment content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_holder);
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

    public GenericCommentsFragment getContent() {
        return content;
    }

    // Setup

    public void setupTopBar() {
        getSupportActionBar().setTitle(R.string.comments);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupContent() {
        content = new ListCommentsFragment();
        content.hasOptionsMenu = true;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, content);
        transaction.commit();
    }
}
