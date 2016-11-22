package com.graffitab.ui.views.recyclerview.components;

import android.graphics.Rect;
import android.support.annotation.IntRange;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by georgichristov on 21/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class AdvancedRecyclerViewItemDecoration extends RecyclerView.ItemDecoration {

    private final int spanCount;
    private int spacing;

    public AdvancedRecyclerViewItemDecoration(@IntRange(from = 0) int columns, @IntRange(from = 0) int spacing) {
        this.spacing = spacing;
        this.spanCount = columns;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getPaddingLeft() != spacing) {
            parent.setPadding(spacing, spacing, spacing, spacing);
            parent.setClipToPadding(false);
        }

        outRect.top = spacing;
        outRect.bottom = spacing;
        outRect.left = spacing;
        outRect.right = spacing;
    }
}
