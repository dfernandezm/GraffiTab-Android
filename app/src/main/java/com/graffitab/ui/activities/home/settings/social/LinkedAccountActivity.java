package com.graffitab.ui.activities.home.settings.social;

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
import com.graffitab.ui.dialog.DialogBuilder;
import com.graffitab.ui.dialog.OnYesNoHandler;
import com.graffitab.ui.dialog.TaskDialog;
import com.graffitab.utils.api.ApiUtils;
import com.graffitabsdk.model.GTExternalProvider;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.network.service.user.response.GTUserResponse;
import com.graffitabsdk.sdk.GTSDK;

/**
 * Created by georgichristov on 13/02/2017
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class LinkedAccountActivity extends AppCompatActivity {

    private GTExternalProvider.GTExternalProviderType type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(Constants.EXTRA_EXTERNAL_TYPE))
            type = GTExternalProvider.GTExternalProviderType.valueOf(extras.getString(Constants.EXTRA_EXTERNAL_TYPE));
        else {
            finish();
            return;
        }

        setupContentFragment();
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
        String typeString = type.name().toLowerCase();
        String title = typeString.substring(0, 1).toUpperCase() + typeString.substring(1);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupContentFragment() {
        MyPreferenceFragment fragment = new MyPreferenceFragment();
        fragment.setType(type);
        getFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment {

        private Preference findFriendsPreference;
        private Preference unlinkPreference;

        private GTExternalProvider.GTExternalProviderType type;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.activity_linked_account);

            findFriendsPreference = findPreference("findFriends");
            unlinkPreference = findPreference("unlink");

            bindPreferences();
        }

        public void setType(GTExternalProvider.GTExternalProviderType type) {
            this.type = type;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            view.setBackgroundColor(Color.WHITE);

            return view;
        }

        private void bindPreferences() {
            findFriendsPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    return true;
                }
            });
            unlinkPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    DialogBuilder.buildYesNoDialog(getActivity(), getString(R.string.app_name), getString(R.string.linked_accounts_unlink_prompt), getString(R.string.linked_accounts_unlink), getString(R.string.other_cancel), new OnYesNoHandler() {

                        @Override
                        public void onClickYes() {
                            unlinkAccount();
                        }

                        @Override
                        public void onClickNo() {}
                    });
                    return true;
                }
            });
        }

        private void unlinkAccount() {
            TaskDialog.getInstance().showDialog(getString(R.string.other_processing), getActivity(), null);
            GTSDK.getMeManager().unlinkExternalProvider(type, new GTResponseHandler<GTUserResponse>() {

                @Override
                public void onSuccess(GTResponse<GTUserResponse> gtResponse) {
                    TaskDialog.getInstance().hideDialog();
                    DialogBuilder.buildOKToast(getActivity(), getString(R.string.linked_accounts_unlink_success));
                    if (getActivity() != null)
                        getActivity().finish();
                }

                @Override
                public void onFailure(GTResponse<GTUserResponse> gtResponse) {
                    TaskDialog.getInstance().hideDialog();
                    DialogBuilder.buildAPIErrorDialog(getActivity(), getString(R.string.app_name), ApiUtils.localizedErrorReason(gtResponse), true, gtResponse.getResultCode());
                }
            });
        }
    }
}
