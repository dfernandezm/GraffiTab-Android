package com.graffitab.ui.fragments.home;

import com.graffitab.R;
import com.graffitab.ui.fragments.streamables.ListStreamablesFragment;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class FeedFragment extends ListStreamablesFragment {

    @Override
    public int emptyViewImageResource() {
        return R.drawable.empty_feed;
    }

    @Override
    public String emptyViewTitle() {
        return getString(R.string.feed_empty_title);
    }

    @Override
    public String emptyViewSubtitle() {
        return getString(R.string.feed_empty_description);
    }
}
