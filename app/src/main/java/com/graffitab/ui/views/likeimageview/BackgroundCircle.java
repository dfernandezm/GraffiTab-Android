package com.graffitab.ui.views.likeimageview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by georgichristov on 16/02/2017
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class BackgroundCircle extends View {

    public BackgroundCircle(Context context) {
        super(context);
    }

    public BackgroundCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BackgroundCircle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredWidth() > getMeasuredHeight())
            setMeasuredDimension(getMeasuredHeight(), getMeasuredHeight());
        else
            setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
