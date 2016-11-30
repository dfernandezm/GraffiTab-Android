package com.graffitab.ui.activities.custom.streamables;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.graffitab.R;
import com.graffitab.ui.fragments.streamable.GenericStreamablesFragment;
import com.graffitab.utils.activity.ActivityUtils;

/**
 * Created by georgichristov on 24/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public abstract class ToggleStreamablesActivity extends GenericStreamablesActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toggle_streamables, menu);

        ActivityUtils.colorMenu(this, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        else if (item.getItemId() == R.id.action_toggle) {
            if (getContent().getViewType() == GenericStreamablesFragment.ViewType.GRID)
                getContent().switchViewType(GenericStreamablesFragment.ViewType.LIST_FULL);
            else
                getContent().switchViewType(GenericStreamablesFragment.ViewType.GRID);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Setup


    @Override
    public void setupContent(GenericStreamablesFragment contentFragment) {
        super.setupContent(contentFragment);

        getContent().hasOptionsMenu = false;
    }
}
