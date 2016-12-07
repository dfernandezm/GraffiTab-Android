package com.graffitab.utils.Input;


import android.content.Context;

import com.graffitab.R;
import com.graffitab.ui.dialog.DialogBuilder;
import com.graffitab.utils.text.TextUtils;

public class InputValidator {

	public static boolean validateLogin(Context context, String username, String password) {
        if (username.length() <= 0) {
            DialogBuilder.buildOKDialog(context, context.getString(R.string.app_name), context.getString(R.string.login_error_username));
            return false;
        }
        if (password.length() <= 0) {
            DialogBuilder.buildOKDialog(context, context.getString(R.string.app_name), context.getString(R.string.login_error_password));
            return false;
        }
        return true;
    }

    public static boolean validateSignUp(Context context, String firstname, String lastname, String email, String username, String password, String confirmPassword) {
        if (firstname.length() <= 0) {
            DialogBuilder.buildOKDialog(context, context.getString(R.string.app_name), context.getString(R.string.sign_up_error_firstname));
            return false;
        }
        if (lastname.length() <= 0) {
            DialogBuilder.buildOKDialog(context, context.getString(R.string.app_name), context.getString(R.string.sign_up_error_lastname));
            return false;
        }
        if (email.length() <= 0) {
            DialogBuilder.buildOKDialog(context, context.getString(R.string.app_name), context.getString(R.string.sign_up_error_email));
            return false;
        }
        if (username.length() <= 0) {
            DialogBuilder.buildOKDialog(context, context.getString(R.string.app_name), context.getString(R.string.sign_up_error_username));
            return false;
        }
        if (password.length() <= 0) {
            DialogBuilder.buildOKDialog(context, context.getString(R.string.app_name), context.getString(R.string.sign_up_error_password));
            return false;
        }
        if (!TextUtils.isValidEmailAddress(email)) {
            DialogBuilder.buildOKDialog(context, context.getString(R.string.app_name), context.getString(R.string.sign_up_error_invalid_email));
            return false;
        }
        if (!password.equals(confirmPassword)) {
            DialogBuilder.buildOKDialog(context, context.getString(R.string.app_name), context.getString(R.string.sign_up_error_confirm_password));
            return false;
        }
        return true;
    }
}
