package com.graffitab.ui.activities.home.settings;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.managers.GTGcmManager;
import com.graffitab.settings.Settings;
import com.graffitab.ui.activities.home.WebActivity;
import com.graffitab.ui.activities.home.me.edit.EditPasswordActivity;
import com.graffitab.ui.activities.home.me.edit.EditProfileActivity;
import com.graffitab.ui.activities.home.users.UserLikesActivity;
import com.graffitab.ui.activities.login.LoginActivity;
import com.graffitab.ui.dialog.DialogBuilder;
import com.graffitab.ui.dialog.TaskDialog;
import com.graffitab.ui.dialog.handlers.OnYesNoHandler;
import com.graffitab.utils.Utils;
import com.graffitabsdk.sdk.GTSDK;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.instabug.library.Instabug;

/**
 * Created by georgichristov on 04/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class SettingsActivity extends AppCompatActivity {

    public static void logout(final Activity activity) {
        final Runnable handlerBlock = new Runnable() {

            @Override
            public void run() {
                TaskDialog.getInstance().hideDialog();

                // Clear any data on disk for the current user.

                // Show login screen.
                Intent intent = new Intent(activity, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                activity.finish();
                activity.overridePendingTransition(R.anim.slow_fade_in, R.anim.fade_out);
            }
        };

        TaskDialog.getInstance().showDialog(activity.getString(R.string.other_processing), activity, null);

        Utils.runWithDelay(new Runnable() { // Give time to the loading dialog to appear.

            @Override
            public void run() {
                // Unregister GCM token.
                GTGcmManager.sharedInstance.unregisterToken(activity);

                // Logout user.
                GTSDK.getUserManager().logout(new GTResponseHandler<Void>() {

                    @Override
                    public void onSuccess(GTResponse<Void> gtResponse) {
                        handlerBlock.run();
                    }

                    @Override
                    public void onFailure(GTResponse<Void> responseObject) {
                        handlerBlock.run();
                    }
                });
            }
        }, 300);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

        setupTopBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Setup

    private void setupTopBar() {
        getSupportActionBar().setTitle(getString(R.string.home_settings));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static class MyPreferenceFragment extends PreferenceFragment {

        private Preference editProfilePreference;
        private Preference changePasswordPreference;
        private Preference userLikesPreference;
        private CheckBoxPreference rememberMePreference;

        private Preference cachePreference;
        private CheckBoxPreference assistantPreference;

        private Preference helpPreference;
        private Preference reportPreference;

        private Preference termsPreference;
        private Preference eulaPreference;
        private Preference aboutPreference;

        private Preference logoutPreference;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.activity_settings);

            editProfilePreference = findPreference("editProfile");
            changePasswordPreference = findPreference("changePassword");
            userLikesPreference = findPreference("likedPosts");
            rememberMePreference = (CheckBoxPreference) findPreference("rememberMe");
            cachePreference = findPreference("clearCache");
            assistantPreference = (CheckBoxPreference) findPreference("drawingAssistant");
            helpPreference = findPreference("helpCenter");
            reportPreference = findPreference("reportProblem");
            termsPreference = findPreference("terms");
            eulaPreference = findPreference("eula");
            aboutPreference = findPreference("about");
            logoutPreference = findPreference("logout");

            bindPreferences();
            loadPreferences();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            view.setBackgroundColor(Color.WHITE);

            return view;
        }

        private void loadPreferences() {
            rememberMePreference.setChecked(Settings.settings.rememberMe());
            assistantPreference.setChecked(Settings.settings.showDrawingAssistant());
        }

        private void bindPreferences() {
            cachePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    DialogBuilder.buildYesNoDialog(getActivity(), getString(R.string.app_name), getString(R.string.settings_prompt_cache), getString(R.string.settings_clear_cache), getString(R.string.other_cancel), new OnYesNoHandler() {

                        @Override
                        public void onClickYes() {
                            GTSDK.invalidateCache();
                        }

                        @Override
                        public void onClickNo() {}
                    });
                    return true;
                }
            });
            assistantPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Settings.settings.setShowDrawingAssistant(assistantPreference.isChecked());
                    return true;
                }
            });
            rememberMePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Settings.settings.setRememberMe(rememberMePreference.isChecked());
                    return true;
                }
            });
            editProfilePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getActivity(), EditProfileActivity.class));
                    return true;
                }
            });
            changePasswordPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getActivity(), EditPasswordActivity.class));
                    return true;
                }
            });
            userLikesPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent i = new Intent(getActivity(), UserLikesActivity.class);
                    i.putExtra(Constants.EXTRA_USER, GTSDK.getAccountManager().getLoggedInUser());
                    startActivity(i);
                    return true;
                }
            });
            helpPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Instabug.invoke();
                    return true;
                }
            });
            reportPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Instabug.invoke();
                    return true;
                }
            });
            termsPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent i = new Intent(getActivity(), WebActivity.class);
                    i.putExtra(Constants.EXTRA_HTML_FILE, "terms.html");
                    i.putExtra(Constants.EXTRA_TITLE, getString(R.string.sign_up_terms_of_use));
                    startActivity(i);
                    return true;
                }
            });
            eulaPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent i = new Intent(getActivity(), WebActivity.class);
                    i.putExtra(Constants.EXTRA_HTML_FILE, "eula.html");
                    i.putExtra(Constants.EXTRA_TITLE, getString(R.string.settings_eula));
                    startActivity(i);
                    return true;
                }
            });
            aboutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getActivity(), AboutActivity.class));
                    return true;
                }
            });
            logoutPreference.setSummary(GTSDK.getAccountManager().getLoggedInUser().fullName());
            logoutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    DialogBuilder.buildYesNoDialog(getActivity(), getString(R.string.app_name), getString(R.string.settings_prompt_logout), getString(R.string.settings_logout), getString(R.string.other_cancel), new OnYesNoHandler() {

                        @Override
                        public void onClickYes() {
                            logout(getActivity());
                        }

                        @Override
                        public void onClickNo() {}
                    });
                    return true;
                }
            });
        }
    }
}
