package com.graffitab.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.graffitab.R;
import com.graffitab.config.AppConfig;
import com.graffitab.ui.activities.home.settings.SettingsActivity;
import com.graffitab.utils.input.KeyboardUtils;
import com.graffitabsdk.sdk.GTSDK;
import com.graffitabsdk.network.common.GTResultCode;

import java.util.Date;

public class DialogBuilder {

    private static Date lastErrorDate;

	public static void buildOKDialog(Context context, String title, String message) {
		if (context == null) return;
		buildOKDialog(context, title, message, null);
	}

    public static void buildAPIErrorDialog(Activity context, String title, String message, GTResultCode resultCode) {
        if (context == null) return;
        buildAPIErrorDialog(context, title, message, false, resultCode, null);
    }

    public static void buildAPIErrorDialog(Activity context, String title, String message, boolean forceShow, GTResultCode resultCode) {
        if (context == null) return;
        buildAPIErrorDialog(context, title, message, forceShow, resultCode, null);
    }

	public static void buildAPIErrorDialog(final Activity context, String title, String message, boolean forceShow, final GTResultCode resultCode, final OnOkHandler handler) {
        if (context == null) return;

        // Define custom action to listen for logout events.
		OnOkHandler action = new OnOkHandler() {

			@Override
			public void onClickOk() {
                if ((resultCode == GTResultCode.USER_NOT_LOGGED_IN || resultCode == GTResultCode.USER_NOT_IN_EXPECTED_STATE) && GTSDK.getAccountManager().isUserLoggedIn())
                    SettingsActivity.logout(context);
                else if (handler != null)
                    handler.onClickOk();
			}
		};

        if (forceShow)
            buildOKDialog(context, title, message, action);
        else {
            Date errorDate = new Date();
            boolean shouldShowError = false;
            if (lastErrorDate == null) { // Error has not been shown.
                shouldShowError = true;
                lastErrorDate = errorDate;
            }
            else { // Show error only after error interval has passed.
                long seconds = (errorDate.getTime() - lastErrorDate.getTime()) / 1000;
                if (seconds > AppConfig.configuration.apiErrorInterval) {
                    shouldShowError = true;
                    lastErrorDate = errorDate;
                }
            }

            if (shouldShowError)
                buildOKDialog(context, title, message, action);
        }
    }

	public static void buildOKDialog(Context context, String title, String message, final OnOkHandler handler) {
        if (context == null) return;

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
        if (context == null) return;

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
	
	public static void buildUsernameDialog(final Context context, final OnYesNoInputHandler handler) {
        if (context == null) return;

		// Create dialog.
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getString(R.string.sign_up_confirmation_title));
		
		// Attach buttons.
		builder.setMessage(context.getString(R.string.login_username_prompt))
			   .setPositiveButton(context.getString(R.string.other_done), null)
			   .setNegativeButton(context.getString(R.string.other_cancel), null);
		
		// Set custom layout.
		final FrameLayout frameView = new FrameLayout(context);
		builder.setView(frameView);

		final AlertDialog alertDialog = builder.create();
		LayoutInflater inflater = alertDialog.getLayoutInflater();
		View dialoglayout = inflater.inflate(R.layout.dialog_input_username, frameView);
		
		final EditText inputField = (EditText) dialoglayout.findViewById(R.id.usernameField);

		// Attach button listeners.
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.other_done), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				KeyboardUtils.hideKeyboard(context, inputField);
				String text = inputField.getText().toString().trim();

				if (handler != null) {
					if (text.length() > 0)
						handler.onClickYes(text);
				}
			}
		});
		alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.other_cancel), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
                KeyboardUtils.hideKeyboard(context, inputField);
                if (handler != null)
                    handler.onClickNo();
			}
		});
		
		// Show dialog.
		alertDialog.show();
	}
}
