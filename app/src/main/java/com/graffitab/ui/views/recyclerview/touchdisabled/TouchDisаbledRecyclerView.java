package com.graffitab.ui.views.recyclerview.touchdisabled;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by david on 30/03/2017.
 */

public class TouchDisаbledRecyclerView extends RecyclerView {

    public TouchDisаbledRecyclerView(Context context) {
        super(context);
    }

    public TouchDisаbledRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchDisаbledRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        // Disable touch in this view so it is propagated back to parent
        return false;
    }
}