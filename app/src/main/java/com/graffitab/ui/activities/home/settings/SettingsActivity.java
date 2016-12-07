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
import com.instabug.library.Instabug;

/**
 * Created by georgichristov on 04/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class SettingsActivity extends AppCompatActivity {

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

        private Preference reportPreference;
        private Preference termsPreference;
        private Preference eulaPreference;
        private Preference aboutPreference;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.activity_settings);

            reportPreference = findPreference("reportProblem");
            termsPreference = findPreference("terms");
            eulaPreference = findPreference("eula");
            aboutPreference = findPreference("about");

            bindPreferenced();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            view.setBackgroundColor(Color.WHITE);

            return view;
        }

        private void bindPreferenced() {
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
        }
    }
}
