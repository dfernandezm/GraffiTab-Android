package com.graffitab.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;

import com.graffitab.R;
import com.graffitab.utils.file.FileUtils;
import com.graffitab.utils.image.BitmapUtils;

import java.util.List;
import java.util.Random;

public class Utils {

	public static void shareImage(final Activity context, final Bitmap bitmap) {
		new Thread() {

			@Override
			public void run() {
                // Process bitmap.
                byte[] bytes = BitmapUtils.getBitmapData(bitmap);
                final Uri uri = FileUtils.saveImageToShare(bytes);

                // Show share dialog.
                context.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (context == null) return;

                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/jpeg");
                        share.putExtra(Intent.EXTRA_STREAM, uri);
                        context.startActivity(Intent.createChooser(share, context.getString(R.string.other_share)));
                    }
                });
			}
		}.start();
	}

	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	public static void runWithDelay(Runnable runnable, int delay) {
		Handler handler = new Handler();
		handler.postDelayed(runnable, delay);
	}

	public static void openUrl(Context c, String url) {
		if (!url.startsWith("http://") && !url.startsWith("https://"))
			   url = "http://" + url;
		
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		c.startActivity(browserIntent);
	}
	
	public static boolean isIntentAvailable( Context context, String action, String type ) {
	    final PackageManager packageManager = context.getPackageManager();
	    final Intent intent = new Intent( action );
	    
	    if ( type != null )
	    	intent.setType( type );
	    
	    List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities( intent, PackageManager.MATCH_DEFAULT_ONLY );
	    
		return (resolveInfo.size() > 0);
	}
	
	public static boolean isIntentAvailable( Context context, Intent i ) {
	    final PackageManager packageManager = context.getPackageManager();
	    
	    List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities( i, PackageManager.MATCH_DEFAULT_ONLY );
	    
		return (resolveInfo != null && resolveInfo.size() > 0);
	}
	
	public static boolean isActivityAvailable( Context context, String packageName, String className ) {
	    final PackageManager packageManager = context.getPackageManager();
	    final Intent intent = new Intent();
	    intent.setClassName( packageName, className );
	    
	    List<ResolveInfo> list = packageManager.queryIntentActivities( intent, PackageManager.MATCH_DEFAULT_ONLY );

	    return (list.size() > 0);
	}
	
	public static boolean isNetworkConnected( Context c ) {
		ConnectivityManager cm = (ConnectivityManager)c.getSystemService( Context.CONNECTIVITY_SERVICE );
		NetworkInfo ni = cm.getActiveNetworkInfo();
		
		return (ni != null);
	}
}
