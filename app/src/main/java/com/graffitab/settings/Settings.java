package com.graffitab.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.graffitab.application.MyApplication;

/**
 * Created by georgichristov on 08/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class Settings {

    private static final String PREFERENCE_TITLE = "GraffiTabPreferences";
    private static final String PREFERENCE_REMEMBER_ME = "PREFERENCE_REMEMBER_ME";
    private static final String PREFERENCE_SHOW_DRAWING_ASSISTANT = "PREFERENCE_SHOW_DRAWING_ASSISTANT";
    private static final String PREFERENCE_SHOWED_ONBOARDING = "PREFERENCE_SHOWED_ONBOARDING";

    public static final Settings settings = new Settings();

    public boolean showedOnboarding() {
        return getPreference(PREFERENCE_SHOWED_ONBOARDING, false);
    }

    public void setShowedOnboarding(boolean value) {
        savePreference(value, PREFERENCE_SHOWED_ONBOARDING);
    }

    public boolean rememberMe() {
        return getPreference(PREFERENCE_REMEMBER_ME, true);
    }

    public void setRememberMe(boolean value) {
        savePreference(value, PREFERENCE_REMEMBER_ME);
    }

    public boolean showDrawingAssistant() {
        return getPreference(PREFERENCE_SHOW_DRAWING_ASSISTANT, true);
    }

    public void setShowDrawingAssistant(boolean value) {
        savePreference(value, PREFERENCE_SHOW_DRAWING_ASSISTANT);
    }

    private void savePreference(boolean value, String key) {
        SharedPreferences sharedPref = MyApplication.getInstance().getSharedPreferences(PREFERENCE_TITLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private void savePreference(String value, String key) {
        SharedPreferences sharedPref = MyApplication.getInstance().getSharedPreferences(PREFERENCE_TITLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private void savePreference(int value, String key) {
        SharedPreferences sharedPref = MyApplication.getInstance().getSharedPreferences(PREFERENCE_TITLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private boolean getPreference(String key, boolean defaultValue) {
        SharedPreferences sharedPref = MyApplication.getInstance().getSharedPreferences(PREFERENCE_TITLE, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, defaultValue);
    }

    private String getPreference(String key, String defaultValue) {
        SharedPreferences sharedPref = MyApplication.getInstance().getSharedPreferences(PREFERENCE_TITLE, Context.MODE_PRIVATE);
        return sharedPref.getString(key, defaultValue);
    }

    private int getPreference(String key, int defaultValue) {
        SharedPreferences sharedPref = MyApplication.getInstance().getSharedPreferences(PREFERENCE_TITLE, Context.MODE_PRIVATE);
        return sharedPref.getInt(key, defaultValue);
    }
}
