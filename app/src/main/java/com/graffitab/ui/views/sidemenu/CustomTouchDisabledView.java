package com.graffitab.ui.views.sidemenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
class CustomTouchDisabledView extends ViewGroup {

    private View mContent;

    //	private int mMode;
    private boolean mTouchDisabled = false;

    public CustomTouchDisabledView(Context context) {
        this(context, null);
    }

    public CustomTouchDisabledView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setContent(View v) {
        if (mContent != null) {
            this.removeView(mContent);
        }
        mContent = v;
        addView(mContent);
    }

    public View getContent() {
        return mContent;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);

        final int contentWidth = getChildMeasureSpec(widthMeasureSpec, 0, width);
        final int contentHeight = getChildMeasureSpec(heightMeasureSpec, 0, height);
        mContent.measure(contentWidth, contentHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = r - l;
        final int height = b - t;
        mContent.layout(0, 0, width, height);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mTouchDisabled;
    }

    void setTouchDisable(boolean disableTouch) {
        mTouchDisabled = disableTouch;
    }

    boolean isTouchDisabled() {
        return mTouchDisabled;
    }
}
