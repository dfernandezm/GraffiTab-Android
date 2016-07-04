package com.graffitab.utils;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

public class Utils {

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
