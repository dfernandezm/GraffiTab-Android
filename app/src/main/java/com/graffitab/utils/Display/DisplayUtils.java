package com.graffitab.utils.display;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.graffitab.R;

public class DisplayUtils {

	public static boolean isLandscape( Context c ) {
		return getScreenOrientation( c ) == Configuration.ORIENTATION_LANDSCAPE;
	}
	
	public static int getScreenOrientation(Context context) {
		Display display = getScreenDisplay(context);
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		
		int orientation = Configuration.ORIENTATION_UNDEFINED;

		if (width < height)
			orientation = Configuration.ORIENTATION_PORTRAIT;
		else
			orientation = Configuration.ORIENTATION_LANDSCAPE;

		return orientation;
	}
	
	public static int pxToDip( Context c, int px ) {
		return (int)TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, px, c.getResources().getDisplayMetrics() );
	}
	
	public static int dpToPx(Context c, int dp) {
	    DisplayMetrics displayMetrics = c.getResources().getDisplayMetrics();
	    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
	    return px;
	}
	
	public static int getRawDimension( Context c, int resId ) {
		return (int) (c.getResources().getDimension(resId) / c.getResources().getDisplayMetrics().density);
	}
	
	public static Display getScreenDisplay( Context ctxt ) {
		WindowManager wm = (WindowManager) ctxt.getSystemService( Context.WINDOW_SERVICE );
		Display display = wm.getDefaultDisplay();
		
		return display;
	}
	
	public static int getScreenWidth(Context c) {
		Display display = getScreenDisplay(c);
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		
		return width;
	}
	
	public static int getScreenHeight(Context c) {
		Display display = getScreenDisplay(c);
		Point size = new Point();
		display.getSize(size);
		int height = size.y;
		
		return height;
	}
	
	public static boolean isTablet(Context c) {
		return c.getResources().getBoolean(R.bool.isTablet);
	}
}
