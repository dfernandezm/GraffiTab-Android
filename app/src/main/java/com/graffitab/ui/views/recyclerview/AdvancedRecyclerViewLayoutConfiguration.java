package com.graffitab.ui.views.recyclerview;

/**
 * Created by georgichristov on 22/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class AdvancedRecyclerViewLayoutConfiguration {

    private int spanCount;

    public AdvancedRecyclerViewLayoutConfiguration(int spanCount) {
        this.spanCount = spanCount;
    }

    public int getSpanCount() {
        return spanCount;
    }
}
