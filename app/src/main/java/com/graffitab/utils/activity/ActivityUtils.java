package com.graffitab.utils.activity;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.graffitab.utils.display.DisplayUtils;

public class ActivityUtils {

	public static void setOrientation( AppCompatActivity a ) {
		if ( !DisplayUtils.isTablet(a) )
            a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	public static void showAsPopup(AppCompatActivity activity, int width, int height) {
	    //To show activity as dialog and dim the background, you need to declare android:theme="@style/PopupTheme" on for the chosen activity on the manifest
	    activity.requestWindowFeature(Window.FEATURE_ACTION_BAR);
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
}
