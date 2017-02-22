package com.graffitab.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.graffitab.application.MyApplication;
import com.graffitab.config.AppConfig;

import java.util.Date;
import java.util.concurrent.TimeUnit;

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
    private static final String PREFERENCE_SHOWED_AVATAR_PROMPT = "PREFERENCE_SHOWED_AVATAR_PROMPT";
    private static final String PREFERENCE_SHOWED_FEEDBACK_ONBOARDING = "PREFERENCE_SHOWED_FEEDBACK_ONBOARDING";
    private static final String PREFERENCE_FIRST_START_DATE = "PREFERENCE_FIRST_START_DATE";

    public static final Settings settings = new Settings();

    public boolean shouldShowFeedbackOnboarding() {
        Log.i(getClass().getSimpleName(), "Checking feedback onboarding");
        long timestamp = getPreference(PREFERENCE_FIRST_START_DATE, -1L);
        if (timestamp == -1L) // We don't have a first launch date yet.
            savePreference(new Date().getTime(), PREFERENCE_FIRST_START_DATE);

        timestamp = getPreference(PREFERENCE_FIRST_START_DATE, -1L);
        if (timestamp > 0) {
            Log.i(getClass().getSimpleName(), "Found timestamp " + timestamp);
            Date now = new Date();
            long diff = now.getTime() - timestamp;
            long daysBetween = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            Log.i(getClass().getSimpleName(), "Days between launches " + daysBetween);
            return daysBetween >= AppConfig.configuration.onboardingFeedbackDaysTrigger;
        }
        return false;
    }

    public boolean showedFeedbackOnboarding() {
        return getPreference(PREFERENCE_SHOWED_FEEDBACK_ONBOARDING, false);
    }

    public void setShowedFeedbackOnboarding(boolean value) {
        savePreference(value, PREFERENCE_SHOWED_FEEDBACK_ONBOARDING);
    }

    public boolean showedAvatarPrompt() {
        return getPreference(PREFERENCE_SHOWED_AVATAR_PROMPT, false);
    }

    public void setShowedAvatarPrompt(boolean value) {
        savePreference(value, PREFERENCE_SHOWED_AVATAR_PROMPT);
    }

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

    private void savePreference(long value, String key) {
        SharedPreferences sharedPref = MyApplication.getInstance().getSharedPreferences(PREFERENCE_TITLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(key, value);
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

    private long getPreference(String key, long defaultValue) {
        SharedPreferences sharedPref = MyApplication.getInstance().getSharedPreferences(PREFERENCE_TITLE, Context.MODE_PRIVATE);
        return sharedPref.getLong(key, defaultValue);
    }
}
