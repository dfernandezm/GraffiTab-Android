package com.graffitab.utils.Input;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyboardUtils {

	public static void hideKeyboard(Activity c) {
		try {
			InputMethodManager imm = (InputMethodManager) c.getSystemService(Activity.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(c.getCurrentFocus().getWindowToken(), 0);
		} catch (Exception e) {}
	}
	
	public static void showKeyboard(Context c, EditText editText) {
		InputMethodManager imm = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
	}
	
	public static void hideKeyboard(Context c, EditText editText) {
		try {
			InputMethodManager imm = (InputMethodManager) c.getSystemService(Activity.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		} catch (Exception e) {}
	}
}
