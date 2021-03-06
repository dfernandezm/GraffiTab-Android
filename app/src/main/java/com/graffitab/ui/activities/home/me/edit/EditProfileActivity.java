package com.graffitab.ui.activities.home.me.edit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;
import com.graffitab.R;
import com.graffitab.managers.UserAssetManager;
import com.graffitab.ui.activities.custom.CameraUtilsActivity;
import com.graffitab.ui.activities.home.me.PrivateStreamablesActivity;
import com.graffitab.ui.dialog.DialogBuilder;
import com.graffitab.ui.dialog.TaskDialog;
import com.graffitab.utils.activity.ActivityUtils;
import com.graffitab.utils.api.ApiUtils;
import com.graffitab.utils.image.ImageUtils;
import com.graffitab.utils.input.InputValidator;
import com.graffitab.utils.input.KeyboardUtils;
import com.graffitabsdk.model.GTExternalProvider;
import com.graffitabsdk.model.GTUser;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.network.service.user.response.GTUserResponse;
import com.graffitabsdk.sdk.GTSDK;
import com.graffitabsdk.sdk.events.users.GTUserAvatarUpdatedEvent;
import com.graffitabsdk.sdk.events.users.GTUserCoverUpdatedEvent;
import com.graffitabsdk.sdk.events.users.GTUserProfileUpdatedEvent;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by georgichristov on 01/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class EditProfileActivity extends CameraUtilsActivity {

    @BindView(R.id.avatar) ImageView avatar;
    @BindView(R.id.cover) ImageView cover;
    @BindView(R.id.firstName) TextView firstName;
    @BindView(R.id.lastName) TextView lastName;
    @BindView(R.id.email) TextView email;
    @BindView(R.id.about) TextView about;
    @BindView(R.id.website) TextView website;

    private GTUser me = GTSDK.getAccountManager().getLoggedInUser();
    private boolean pickingCoverImage = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_edit);
        ButterKnife.bind(this);

        setupTopBar();

        loadUserData();
    }

    @Override
    public void onPause() {
        super.onPause();
        GTSDK.registerEventListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        GTSDK.unregisterEventListener(this);
    }

    @Override
    protected void onDestroy() {
        GTSDK.unregisterEventListener(this);
        super.onDestroy();
    }

    @OnClick(R.id.avatarLayout)
    public void onClickAvatar(View view) {
        pickingCoverImage = false;
        showImagePicker(avatar);
    }

    @OnClick(R.id.coverLayout)
    public void onClickCover(View view) {
        pickingCoverImage = true;
        showImagePicker(cover);
    }

    @OnClick(R.id.changePasswordLayout)
    public void onClickChangePassword(View view) {
        startActivity(new Intent(this, EditPasswordActivity.class));
    }

    @OnClick(R.id.privateLayout)
    public void onClickPrivate(View view) {
        startActivity(new Intent(this, PrivateStreamablesActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        ActivityUtils.colorMenu(this, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        else if (item.getItemId() == R.id.action_save) {
            saveProfile();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Image picking.

    @Override
    public BottomSheet.Builder buildImagePickerSheet() {
        return new BottomSheet.Builder(this, R.style.BottomSheet_StyleDialog)
                .title(R.string.other_select_image)
                .sheet(me.hasLinkedAccount(GTExternalProvider.GTExternalProviderType.FACEBOOK) && !pickingCoverImage ? R.menu.menu_camera_facebook : R.menu.menu_camera_normal);
    }

    @Override
    public Pair<Integer, Integer> calculateAspectRatio(View targetView) {
        if (targetView == avatar)
            return super.calculateAspectRatio(targetView);

        int ratioW = 5;
        int ratioH = 3;
        return new Pair<>(ratioW, ratioH);
    }

    @Override
    public void finishPickingImage(Bitmap bitmap, int actionId) {
        super.finishPickingImage(bitmap, actionId);

        if (pickingCoverImage) { // Cover
            if (bitmap != null)
                UserAssetManager.editCover(this, bitmap, new UserAssetManager.OnAssetUpdatedListener() {

                    @Override
                    public void onAssetUpdated() {
                        refreshUserAfterProfileChange();
                    }
                });
            else
                UserAssetManager.deleteCover(this, new UserAssetManager.OnAssetUpdatedListener() {

                    @Override
                    public void onAssetUpdated() {
                        refreshUserAfterProfileChange();
                    }
                });
        }
        else { // Avatar
            if (actionId == R.id.action_facebook_import)
                UserAssetManager.importAvatar(this, GTExternalProvider.GTExternalProviderType.FACEBOOK, new UserAssetManager.OnAssetUpdatedListener() {

                    @Override
                    public void onAssetUpdated() {
                        refreshUserAfterProfileChange();
                    }
                });
            else if (bitmap != null)
                UserAssetManager.editAvatar(this, bitmap, new UserAssetManager.OnAssetUpdatedListener() {

                    @Override
                    public void onAssetUpdated() {
                        refreshUserAfterProfileChange();
                    }
                });
            else
                UserAssetManager.deleteAvatar(this, new UserAssetManager.OnAssetUpdatedListener() {

                    @Override
                    public void onAssetUpdated() {
                        refreshUserAfterProfileChange();
                    }
                });
        }
    }

    @Subscribe
    public void userProfileUpdatedEvent(GTUserProfileUpdatedEvent event) {
        if (avatar == null) return;
        refreshUserAfterProfileChange();
    }

    @Subscribe
    public void userAvatarChangedEvent(GTUserAvatarUpdatedEvent event) {
        if (avatar == null) return;
        refreshUserAfterProfileChange();
    }

    @Subscribe
    public void userCoverChangedEvent(GTUserCoverUpdatedEvent event) {
        if (avatar == null) return;
        refreshUserAfterProfileChange();
    }

    private void refreshUserAfterProfileChange() {
        me = GTSDK.getAccountManager().getLoggedInUser();
        loadUserData();
    }

    private void saveProfile() {
        Log.i(getClass().getSimpleName(), "Saving user");
        String fn = firstName.getText().toString().trim();
        String ln = lastName.getText().toString().trim();
        String em = email.getText().toString().trim();
        String a = about.getText().toString().trim();
        String w = website.getText().toString().trim();

        if (InputValidator.validateEditProfile(this, fn, ln, em)) {
            KeyboardUtils.hideKeyboard(this);
            TaskDialog.getInstance().showProcessingDialog(this);

            GTSDK.getMeManager().edit(fn, ln, em, a.length() > 0 ? a : null, w.length() > 0 ? w : null, new GTResponseHandler<GTUserResponse>() {

                @Override
                public void onSuccess(GTResponse<GTUserResponse> responseObject) {
                    if (avatar == null) return; // View is destroyed.

                    Log.i(getClass().getSimpleName(), "Successfully saved profile");
                    TaskDialog.getInstance().hideDialog();

                    me = GTSDK.getAccountManager().getLoggedInUser();
                    loadUserData();

                    DialogBuilder.buildOKToast(EditProfileActivity.this, getString(R.string.edit_profile_success));
                }

                @Override
                public void onFailure(GTResponse<GTUserResponse> responseObject) {
                    Log.e(getClass().getSimpleName(), "Failed to save profile");
                    TaskDialog.getInstance().hideDialog();

                    DialogBuilder.buildAPIErrorDialog(EditProfileActivity.this, getString(R.string.app_name), ApiUtils.localizedErrorReason(responseObject), true, responseObject.getResultCode());
                }
            });
        }
    }

    // Loading

    private void loadUserData() {
        firstName.setText(me.firstName);
        lastName.setText(me.lastName);
        email.setText(me.email);
        if (me.about != null) about.setText(me.about);
        if (me.website != null) website.setText(me.website);

        loadUserAssets();
    }

    private void loadUserAssets() {
        loadAvatar();
        loadCover();
    }

    private void loadAvatar() {
        ImageUtils.setAvatar(avatar, me);
    }

    private void loadCover() {
        ImageUtils.setCover(cover, me);
    }

    // Setup

    private void setupTopBar() {
        getSupportActionBar().setTitle(getString(R.string.edit_profile));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
