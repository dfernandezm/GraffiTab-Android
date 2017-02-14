package com.graffitab.ui.activities.home.settings.social;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookException;
import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.ui.activities.custom.facebook.FacebookUtilsActivity;
import com.graffitab.ui.dialog.DialogBuilder;
import com.graffitab.ui.dialog.TaskDialog;
import com.graffitab.utils.api.ApiUtils;
import com.graffitab.utils.image.ImageUtils;
import com.graffitabsdk.model.GTExternalProvider;
import com.graffitabsdk.model.GTUser;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.network.service.user.response.GTUserResponse;
import com.graffitabsdk.sdk.GTSDK;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by georgichristov on 13/02/2017
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class LinkedAccountsActivity extends FacebookUtilsActivity {

    @BindView(R.id.facebookImg) ImageView facebookImg;
    @BindView(R.id.facebookLbl) TextView facebookLbl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_linked_accounts);
        ButterKnife.bind(this);

        setupTopBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLinkedAccounts();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.facebookBtn)
    public void onClickFacebook(View view) {
        GTUser user = GTSDK.getAccountManager().getLoggedInUser();
        boolean fLinked = user.hasLinkedAccount(GTExternalProvider.GTExternalProviderType.FACEBOOK);

        final Runnable openLinkedAccountRunnable = new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(LinkedAccountsActivity.this, LinkedAccountActivity.class);
                i.putExtra(Constants.EXTRA_EXTERNAL_TYPE, GTExternalProvider.GTExternalProviderType.FACEBOOK.name());
                startActivity(i);
            }
        };

        if (!fLinked) {
            getFacebookMe(false, new FacebookMeCallback() {

                @Override
                public void onProfileFetched(String firstName, String lastName, String userId, String email, String accessToken) {
                    TaskDialog.getInstance().showProcessingDialog(LinkedAccountsActivity.this);
                    GTSDK.getMeManager().linkExternalProvider(userId, accessToken, GTExternalProvider.GTExternalProviderType.FACEBOOK, new GTResponseHandler<GTUserResponse>() {

                        @Override
                        public void onSuccess(GTResponse<GTUserResponse> gtResponse) {
                            TaskDialog.getInstance().hideDialog();
                            loadLinkedAccounts();
                            openLinkedAccountRunnable.run();
                        }

                        @Override
                        public void onFailure(GTResponse<GTUserResponse> gtResponse) {
                            TaskDialog.getInstance().hideDialog();
                            DialogBuilder.buildAPIErrorDialog(LinkedAccountsActivity.this, getString(R.string.app_name), ApiUtils.localizedErrorReason(gtResponse), true, gtResponse.getResultCode());
                        }
                    });
                }

                @Override
                public void onCancel() {}

                @Override
                public void onError(FacebookException exception) {
                    DialogBuilder.buildOKToast(LinkedAccountsActivity.this, getString(R.string.other_facebook_error));
                }
            });
        }
        else
            openLinkedAccountRunnable.run();
    }

    // Loading

    private void loadLinkedAccounts() {
        GTUser user = GTSDK.getAccountManager().getLoggedInUser();
        boolean fLinked = user.hasLinkedAccount(GTExternalProvider.GTExternalProviderType.FACEBOOK);

        facebookImg.setImageDrawable(ImageUtils.tintIcon(this, R.drawable.facebook, getResources().getColor(fLinked ? R.color.colorPrimary : R.color.colorMetadata)));
        facebookLbl.setTextColor(fLinked ? Color.parseColor("#232728") : getResources().getColor(R.color.colorMetadata));
    }

    // Setup

    private void setupTopBar() {
        getSupportActionBar().setTitle(getString(R.string.settings_linked_accounts));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
