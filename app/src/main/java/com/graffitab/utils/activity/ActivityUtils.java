package com.graffitab.utils.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

import com.graffitab.R;
import com.graffitab.utils.device.DeviceUtils;
import com.graffitab.utils.image.BitmapUtils;

import java.io.IOException;
import java.io.InputStream;

public class ActivityUtils {

    public static void setupBackgroundImage(Activity activity, int imageResId, int backgroundResId) {
        InputStream is = activity.getResources().openRawResource(imageResId);
        byte[] b;

        try {
            b = BitmapUtils.getBytes(is);
            Drawable drawable = new BitmapDrawable(activity.getResources(), BitmapUtils.decodeSampledBitmapFromBytesForCurrentScreen(b, activity));

            if (activity.findViewById(backgroundResId) != null) {
                ImageView background = (ImageView) activity.findViewById(backgroundResId);
                background.setImageDrawable(drawable);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public static void setOrientation( AppCompatActivity a ) {
		if ( !DeviceUtils.isTablet(a) )
            a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	public static void showAsPopup(AppCompatActivity activity, int width, int height) {
	    //To show activity as dialog and dim the background, you need to declare android:theme="@style/PopupTheme" on for the chosen activity on the manifest
	    activity.getWindow().setFlags(LayoutParams.FLAG_DIM_BEHIND,
	        LayoutParams.FLAG_DIM_BEHIND);
	    LayoutParams params = activity.getWindow().getAttributes(); 
	    params.alpha = 1.0f;
	    params.dimAmount = 0.5f;
	    activity.getWindow().setAttributes(params);

	    // This sets the window size, while working around the IllegalStateException thrown by ActionBarView
	    activity.getWindow().setLayout(width, height);
	}

	public static void hideActionBar(AppCompatActivity activity) {
		activity.requestWindowFeature(Window.FEATURE_ACTION_BAR);
		activity.getSupportActionBar().hide();
	}

	public static void enableFullScreen(AppCompatActivity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	public static void colorMenu(Context context, Menu menu) {
        if (menu == null) return;

        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            Drawable drawable = menuItem.getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(context.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            }
        }
	}

    public static void colorMenu(Context context, Menu menu, int colorResId) {
        if (menu == null) return;

        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            Drawable drawable = menuItem.getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(context.getResources().getColor(colorResId), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }
}
