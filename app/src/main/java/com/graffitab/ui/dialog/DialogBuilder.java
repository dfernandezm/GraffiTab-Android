package com.graffitab.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.graffitab.R;
import com.graffitab.ui.dialog.handlers.OnOkHandler;
import com.graffitab.ui.dialog.handlers.OnYesNoHandler;
import com.graffitab.ui.dialog.handlers.OnYesNoInputHandler;

import java.util.Date;

public class DialogBuilder {

    private static Date lastErrorDate;

	public static void buildOKDialog(Context context, String title, String message) {
		buildOKDialog(context, title, message, null);
	}

    public static void buildAPIErrorDialog(Context context, String title, String message) {
        buildAPIErrorDialog(context, title, message, false, null);
    }

    public static void buildAPIErrorDialog(Context context, String title, String message, boolean forceShow) {
        buildAPIErrorDialog(context, title, message, forceShow, null);
    }

	public static void buildAPIErrorDialog(Context context, String title, String message, boolean forceShow, final OnOkHandler handler) {
        if (forceShow)
            buildOKDialog(context, title, message, null);
        else {
            Date errorDate = new Date();
            boolean shouldShowError = false;
            if (lastErrorDate == null) {
                shouldShowError = true;
                lastErrorDate = errorDate;
            }
            else {
                long seconds = (errorDate.getTime() - lastErrorDate.getTime()) / 1000;
                if (seconds > 30) {
                    shouldShowError = true;
                    lastErrorDate = errorDate;
                }
            }

            if (shouldShowError)
                buildOKDialog(context, title, message, null);
        }
    }

	public static void buildOKDialog(Context context, String title, String message, final OnOkHandler handler) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message)
			   .setTitle(title)
		       .setCancelable(false)
		       .setPositiveButton("OK", new OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   if (handler != null)
		        		   handler.onClickOk();
					
		        	   dialog.dismiss();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public static void buildYesNoDialog(Context context, String title, String message, String yesTitle, String noTitle, final OnYesNoHandler handler) {
		OnClickListener dialogClickListener = new OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE: {
                    // Yes button clicked.

                    if (handler != null)
                        handler.onClickYes();

                    break;
                }
                case DialogInterface.BUTTON_NEGATIVE: {
                    // No button clicked.

                    if (handler != null)
                        handler.onClickNo();

                    break;
                }
            }
		    }
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message).setPositiveButton(yesTitle, dialogClickListener)
		    .setNegativeButton(noTitle, dialogClickListener).show();
	}
	
	public static void buildYesNoInputDialog(Context context, String hint, String title, String message, String yesTitle, String noTitle, final OnYesNoInputHandler handler) {
		// Create dialog.
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		
		// Attach buttons.
		builder.setMessage(message)
			   .setPositiveButton(yesTitle, null)
			   .setNegativeButton(noTitle, null);
		
		// Set custom layout.
		final FrameLayout frameView = new FrameLayout(context);
		builder.setView(frameView);

		final AlertDialog alertDialog = builder.create();
		LayoutInflater inflater = alertDialog.getLayoutInflater();
		View dialoglayout = inflater.inflate(R.layout.dialog_input, frameView);
		
		final EditText inputField = (EditText) dialoglayout.findViewById(R.id.codeField);
		inputField.setHint(hint);

		// Attach button listeners.
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, yesTitle, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			if (handler != null)
				handler.onClickYes(inputField.getText().toString());
			}
		});
		alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, noTitle, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			if (handler != null)
				handler.onClickNo();
			}
		});
		
		// Show dialog.
		alertDialog.show();
	}
}
