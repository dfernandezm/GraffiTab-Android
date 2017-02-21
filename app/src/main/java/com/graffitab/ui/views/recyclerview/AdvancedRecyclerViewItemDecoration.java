package com.graffitab.ui.views.recyclerview;

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
    private boolean padEdges = true;

    public AdvancedRecyclerViewItemDecoration() {
        this.spacing = 0;
        this.spanCount = 1;
    }

    public AdvancedRecyclerViewItemDecoration(@IntRange(from = 0) int columns, @IntRange(from = 0) int spacing) {
        this.spacing = spacing;
        this.spanCount = columns;
    }

    public void setPadEdges(boolean padEdges) {
        this.padEdges = padEdges;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (padEdges && parent.getPaddingLeft() != spacing) {
            parent.setPadding(spacing, spacing, spacing, spacing);
            parent.setClipToPadding(false);
        }

        outRect.top = spacing;
        outRect.bottom = spacing;
        outRect.left = spacing;
        outRect.right = spacing;
    }
}
