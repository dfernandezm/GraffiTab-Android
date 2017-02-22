package com.graffitab.ui.activities.onboard;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;
import com.graffitab.R;
import com.graffitab.managers.UserAssetManager;
import com.graffitab.ui.activities.custom.CameraUtilsActivity;
import com.graffitab.utils.activity.ActivityUtils;
import com.graffitabsdk.model.GTExternalProvider;
import com.graffitabsdk.model.GTUser;
import com.graffitabsdk.sdk.GTSDK;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by georgichristov on 21/02/2017
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class AvatarPromptActivity extends CameraUtilsActivity {

    @BindView(R.id.nameField) TextView nameField;
    @BindView(R.id.avatar) ImageView avatar;

    private GTUser me = GTSDK.getAccountManager().getLoggedInUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUtils.hideActionBar(this);
        ActivityUtils.setOrientation(this);

        setContentView(R.layout.activity_avatar_prompt);
        ButterKnife.bind(this);

        loadUserData();
    }

    @OnClick(R.id.goBtn)
    public void onClickGo(View view) {
        promptForAvatar();
    }

    @OnClick(R.id.avatar)
    public void onClickAvatar(View view) {
        promptForAvatar();
    }

    @OnClick(R.id.laterBtn)
    public void onClickLater(View view) {
        finish();
    }

    // Loading

    private void loadUserData() {
        nameField.setText(me.fullName());
    }

    private void promptForAvatar() {
        showImagePicker(avatar);
    }

    // Image picking

    @Override
    public BottomSheet.Builder buildImagePickerSheet() {
        return new BottomSheet.Builder(this, R.style.BottomSheet_StyleDialog)
                .title(R.string.other_select_image)
                .sheet(me.hasLinkedAccount(GTExternalProvider.GTExternalProviderType.FACEBOOK) ? R.menu.menu_camera_facebook : R.menu.menu_camera_normal);
    }

    @Override
    public void finishPickingImage(Bitmap bitmap, int actionId) {
        super.finishPickingImage(bitmap, actionId);

        UserAssetManager.OnAssetUpdatedListener listener = new UserAssetManager.OnAssetUpdatedListener() {

            @Override
            public void onAssetUpdated() {
                finish();
            }
        };

        if (actionId == R.id.action_facebook_import)
            UserAssetManager.importAvatar(this, GTExternalProvider.GTExternalProviderType.FACEBOOK, listener);
        else if (bitmap != null)
            UserAssetManager.editAvatar(this, bitmap, listener);
        else
            UserAssetManager.deleteAvatar(this);
    }
}
