package com.graffitab.ui.fragments.location;

import com.graffitab.R;
import com.graffitab.graffitabsdk.model.GTLocation;
import com.graffitab.ui.fragments.GenericItemListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public abstract class GenericLocationsFragment extends GenericItemListFragment<GTLocation> {

    public enum ViewType {LIST_FULL}

    private ViewType viewType;

    public void basicInit() {
        setViewType(ViewType.LIST_FULL);
    }

    @Override
    public int emptyViewImageResource() {
        return -1;
    }

    @Override
    public String emptyViewTitle() {
        return getString(R.string.other_empty_no_locations);
    }

    @Override
    public String emptyViewSubtitle() {
        return getString(R.string.other_empty_no_locations_description);
    }

    public void setViewType(ViewType type) {
        this.viewType = type;
    }

    // Loading

    @Override
    public List<GTLocation> generateDummyData() {
        List<GTLocation> loaded = new ArrayList();
        Random random = new Random();
        for (int i = 0; i < 25; i++) {
            GTLocation location = new GTLocation();
            loaded.add(location);
        }
        return loaded;
    }
}
