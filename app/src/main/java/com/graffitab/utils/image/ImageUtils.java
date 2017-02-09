package com.graffitab.utils.image;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class ImageUtils {

    public static Drawable tintIcon(Context context, int resource, int color) {
        Drawable upArrow = context.getResources().getDrawable(resource);
        upArrow = upArrow.mutate();
        upArrow.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        return upArrow;
    }
}
