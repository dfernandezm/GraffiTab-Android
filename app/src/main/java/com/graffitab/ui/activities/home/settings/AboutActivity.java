package com.graffitab.ui.activities.home.settings;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.ui.activities.home.WebActivity;

/**
 * Created by georgichristov on 07/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class AboutActivity extends AppCompatActivity {

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
        getSupportActionBar().setTitle(getString(R.string.settings_about));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static class MyPreferenceFragment extends PreferenceFragment {

        private Preference releaseInformationPreference;
        private Preference versionPreference;
        private Preference buildPreference;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.activity_settings_about);

            releaseInformationPreference = findPreference("releaseInformation");
            versionPreference = findPreference("version");
            buildPreference = findPreference("build");

            bindPreferences();
            loadPreferences();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            view.setBackgroundColor(Color.WHITE);

            return view;
        }

        private void bindPreferences() {
            releaseInformationPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent i = new Intent(getActivity(), WebActivity.class);
                    i.putExtra(Constants.EXTRA_HTML_FILE, "release_notes.html");
                    i.putExtra(Constants.EXTRA_TITLE, getString(R.string.settings_release_information));
                    startActivity(i);
                    return true;
                }
            });
        }

        private void loadPreferences() {
            try {
                String versionName = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
                int versionCode = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionCode;
                versionPreference.setSummary(versionName);
                buildPreference.setSummary(versionCode + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
