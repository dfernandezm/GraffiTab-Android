package com.graffitab.ui.fragments.streamable;

import com.graffitab.R;
import com.graffitab.graffitabsdk.model.GTAsset;
import com.graffitab.graffitabsdk.model.GTStreamable;
import com.graffitab.ui.fragments.GenericItemListFragment;
import com.graffitab.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public abstract class GenericStreamablesFragment extends GenericItemListFragment<GTStreamable> {

    public enum ViewType {GRID, TRENDING, SWIMLANE, LIST_FULL}

    private ViewType viewType;

    public void basicInit() {
        setViewType(ViewType.GRID);
    }

    @Override
    public int emptyViewImageResource() {
        return -1;
    }

    @Override
    public String emptyViewTitle() {
        return getString(R.string.other_empty_no_posts);
    }

    @Override
    public String emptyViewSubtitle() {
        return getString(R.string.other_empty_no_posts_description);
    }

    public void setViewType(ViewType type) {
        this.viewType = type;
    }

    // Loading

    @Override
    public List<GTStreamable> generateDummyData() {
        List<GTStreamable> loaded = new ArrayList();
        for (int i = 0; i < 25; i++) {
            GTStreamable streamable = new GTStreamable();
            GTAsset asset = new GTAsset();
            asset.thumbnailWidth = Utils.randInt(300, 400);
            asset.thumbnailHeight = Utils.randInt(500, 1024);
            streamable.asset = asset;
            loaded.add(streamable);
        }
        return loaded;
    }
}
