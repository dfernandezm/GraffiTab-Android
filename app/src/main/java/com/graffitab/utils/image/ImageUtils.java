package com.graffitab.utils.image;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.graffitab.R;
import com.graffitabsdk.model.GTUser;
import com.squareup.picasso.Picasso;

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

    public static void setAvatar(ImageView imageView, GTUser user) {
        int p = R.drawable.default_avatar;
        if (user.hasAvatar())
            Picasso.with(imageView.getContext()).load(user.avatar.thumbnail).placeholder(p).error(p).into(imageView);
        else
            Picasso.with(imageView.getContext()).load(p).placeholder(p).into(imageView);
    }

    public static void setCover(ImageView imageView, GTUser user) {
        int p = R.drawable.login_full;
        if (user.hasCover())
            Picasso.with(imageView.getContext()).load(user.cover.link).placeholder(p).placeholder(p).error(p).into(imageView);
        else
            Picasso.with(imageView.getContext()).load(p).placeholder(p).into(imageView);
    }
}
