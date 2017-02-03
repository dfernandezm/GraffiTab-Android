package com.graffitab.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.graffitab.application.MyApplication;
import com.graffitab.constants.Constants;

/**
 * Created by georgichristov on 08/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class Settings {

    public static final Settings settings = new Settings();

    public boolean hasAskedForPermission(String permissionKey) {
        return getPreference(permissionKey, false);
    }

    public void setAskedForPermission(String permissionKey, boolean value) {
        savePreference(value, permissionKey);
    }

    public boolean rememberMe() {
        return getPreference(Constants.PREFERENCE_REMEMBER_ME, false);
    }

    public void setRememberMe(boolean value) {
        savePreference(value, Constants.PREFERENCE_REMEMBER_ME);
    }

    public boolean showDrawingAssistant() {
        return getPreference(Constants.PREFERENCE_SHOW_DRAWING_ASSISTANT, true);
    }

    public void setShowDrawingAssistant(boolean value) {
        savePreference(value, Constants.PREFERENCE_SHOW_DRAWING_ASSISTANT);
    }

    private void savePreference(boolean value, String key) {
        SharedPreferences sharedPref = MyApplication.getInstance().getSharedPreferences(Constants.PREFERENCE_TITLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    private void savePreference(String value, String key) {
        SharedPreferences sharedPref = MyApplication.getInstance().getSharedPreferences(Constants.PREFERENCE_TITLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void savePreference(int value, String key) {
        SharedPreferences sharedPref = MyApplication.getInstance().getSharedPreferences(Constants.PREFERENCE_TITLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private boolean getPreference(String key, boolean defaultValue) {
        SharedPreferences sharedPref = MyApplication.getInstance().getSharedPreferences(Constants.PREFERENCE_TITLE, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, defaultValue);
    }

    private String getPreference(String key, String defaultValue) {
        SharedPreferences sharedPref = MyApplication.getInstance().getSharedPreferences(Constants.PREFERENCE_TITLE, Context.MODE_PRIVATE);
        return sharedPref.getString(key, defaultValue);
    }

    private int getPreference(String key, int defaultValue) {
        SharedPreferences sharedPref = MyApplication.getInstance().getSharedPreferences(Constants.PREFERENCE_TITLE, Context.MODE_PRIVATE);
        return sharedPref.getInt(key, defaultValue);
    }
}
