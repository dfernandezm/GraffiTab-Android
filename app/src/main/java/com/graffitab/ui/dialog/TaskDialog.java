package com.graffitab.ui.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

import com.graffitab.R;

public class TaskDialog {

	public static TaskDialog td;
	
	private ProgressDialog dialog;
	private AsyncTask<?,?,?> task;
	
	private TaskDialog() {
		
	}
	
	public static TaskDialog getInstance() {
		if ( td == null )
			td = new TaskDialog();
		
		return td;
	}
	
	public void showDialog(String msg, Activity a, AsyncTask<?,?,?> task) {
		this.task = task;

		dialog = ProgressDialog.show(a.getParent() != null ? a.getParent() : a, "", msg == null ? a.getString(R.string.other_processing) : msg, true);
		dialog.setCancelable(true);
		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
			if (TaskDialog.this.task != null)
				TaskDialog.this.task.cancel(true);
			}
		});
	}
	
	public void hideDialog() {
		try {
			if ( dialog != null ) {
				dialog.dismiss();
				dialog = null;
			}
		} catch (Exception e) {}
	}
}
