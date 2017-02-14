package com.graffitab.ui.activities.home.users;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.cocosw.bottomsheet.BottomSheet;
import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.managers.UserAssetManager;
import com.graffitab.ui.activities.custom.CameraUtilsActivity;
import com.graffitab.ui.activities.home.me.edit.EditProfileActivity;
import com.graffitab.ui.dialog.DialogBuilder;
import com.graffitab.ui.dialog.OnYesNoHandler;
import com.graffitab.ui.fragments.streamables.GenericStreamablesFragment;
import com.graffitab.ui.fragments.users.UserProfileFragment;
import com.graffitab.utils.Utils;
import com.graffitab.utils.api.ApiUtils;
import com.graffitabsdk.model.GTExternalProvider;
import com.graffitabsdk.model.GTUser;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.network.service.user.response.GTUserResponse;
import com.graffitabsdk.sdk.GTSDK;
import com.graffitabsdk.sdk.events.users.GTUserAvatarUpdatedEvent;
import com.graffitabsdk.sdk.events.users.GTUserCoverUpdatedEvent;
import com.graffitabsdk.sdk.events.users.GTUserFollowedEvent;
import com.graffitabsdk.sdk.events.users.GTUserProfileUpdatedEvent;
import com.graffitabsdk.sdk.events.users.GTUserUnfollowedEvent;
import com.squareup.otto.Subscribe;
import com.stfalcon.frescoimageviewer.ImageViewer;

import butterknife.ButterKnife;

/**
 * Created by georgichristov on 20/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class ProfileActivity extends CameraUtilsActivity {

    private GTUser user;
    private UserProfileFragment content;
    private boolean profileRefreshedOnce = false;
    private boolean pickingCoverImage = false;

    public static void show(GTUser user, Context context) {
        Intent i = new Intent(context, ProfileActivity.class);
        i.putExtra(Constants.EXTRA_USER, user);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getSerializable(Constants.EXTRA_USER) != null) {
            user = (GTUser) extras.getSerializable(Constants.EXTRA_USER);
        }
        else {
            finish();
            return;
        }

        setContentView(R.layout.activity_fragment_holder);
        ButterKnife.bind(this);

        setupTopBar();
        setupContent();
        setupEventListeners();

        Utils.runWithDelay(new Runnable() {

            @Override
            public void run() {
                reloadUserProfile();
            }
        }, 300);
    }

    @Override
    protected void onDestroy() {
        GTSDK.unregisterEventListener(this);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        else if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, EditProfileActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickFollow(View view) {
        if (user.followedByCurrentUser) {
            DialogBuilder.buildUnfollowDialog(this, user, new OnYesNoHandler() {

                @Override
                public void onClickYes() {
                    user.followedByCurrentUser = content.toggleFollow();
                }

                @Override
                public void onClickNo() {}
            });
        }
        else
            user.followedByCurrentUser = content.toggleFollow();
    }

    public void onClickPosts(View view) {
        Intent i = new Intent(this, UserPostsActivity.class);
        i.putExtra(Constants.EXTRA_USER, user);
        startActivity(i);
    }

    public void onClickFollowers(View view) {
        Intent i = new Intent(this, UserFollowersActivity.class);
        i.putExtra(Constants.EXTRA_USER, user);
        startActivity(i);
    }

    public void onClickFollowing(View view) {
        Intent i = new Intent(this, UserFollowingActivity.class);
        i.putExtra(Constants.EXTRA_USER, user);
        startActivity(i);
    }

    public void onClickGrid(View view) {
        content.switchViewType(GenericStreamablesFragment.ViewType.GRID);
    }

    public void onClickList(View view) {
        content.switchViewType(GenericStreamablesFragment.ViewType.LIST_FULL);
    }

    public void onClickLikes(View view) {
        Intent i = new Intent(this, UserLikesActivity.class);
        i.putExtra(Constants.EXTRA_USER, user);
        startActivity(i);
    }

    public void onClickMentions(View view) {
        Intent i = new Intent(this, UserMentionsActivity.class);
        i.putExtra(Constants.EXTRA_USER, user);
        startActivity(i);
    }

    public void onClickAvatar(View view) {
        if (user.isMe()) {
            pickingCoverImage = false;
            showImagePicker((ImageView) view);
        }
        else if (user.hasAvatar()) {
            new ImageViewer.Builder(this, new String[]{user.avatar.link})
                    .setStartPosition(0)
                    .show();
        }
    }

    public void onClickCover(View view) {
        if (user.isMe()) {
            pickingCoverImage = true;
            showImagePicker((ImageView) view);
        }
        else if (user.hasCover()) {
            new ImageViewer.Builder(this, new String[]{user.cover.link})
                    .setStartPosition(0)
                    .show();
        }
    }

    public void onClickAbout(View view) {
        if (user.website != null)
            Utils.openUrl(this, user.website);
    }

    // Events

    @Subscribe
    public void userFollowedEvent(GTUserFollowedEvent event) {
        if (this.isDestroyed()) return;
        refreshUserAfterFollowStateChange(event.getUser());
    }

    @Subscribe
    public void userUnfollowedEvent(GTUserUnfollowedEvent event) {
        if (this.isDestroyed()) return;
        refreshUserAfterFollowStateChange(event.getUser());
    }

    @Subscribe
    public void userProfileUpdatedEvent(GTUserProfileUpdatedEvent event) {
        if (this.isDestroyed()) return;
        refreshUserAfterFollowStateChange(event.getUser());
        content.loadUserNamesAndHeaderData(); // Refresh profile assets.
        content.loadUserAssets();
    }

    @Subscribe
    public void userAvatarChangedEvent(GTUserAvatarUpdatedEvent event) {
        if (this.isDestroyed()) return;
        refreshUserAfterAssetsChange();
    }

    @Subscribe
    public void userCoverChangedEvent(GTUserCoverUpdatedEvent event) {
        if (this.isDestroyed()) return;
        refreshUserAfterAssetsChange();
    }

    private void refreshUserAfterAssetsChange() {
        if (user.isMe()) {
            user = GTSDK.getAccountManager().getLoggedInUser();
            content.setUser(user);
            content.loadUserAssets();
        }
    }

    private void refreshUserAfterFollowStateChange(GTUser newUser) {
        // The current logged in user cannot be followed/unfollowed, so we only care about other users.
        if (user.equals(newUser)) // This user has been followed/unfollowed.
            user = newUser;
        else if (user.isMe()) // We are viewing the logged in user.
            user = GTSDK.getAccountManager().getLoggedInUser();

        content.setUser(user);
        content.loadUserCountData();
        content.loadFollowButton();
    }

    // Image picking.

    @Override
    public BottomSheet.Builder buildImagePickerSheet() {
        return new BottomSheet.Builder(this, R.style.BottomSheet_StyleDialog)
                .title(R.string.other_select_image)
                .sheet(user.hasLinkedAccount(GTExternalProvider.GTExternalProviderType.FACEBOOK) && !pickingCoverImage ? R.menu.menu_camera_facebook : R.menu.menu_camera_normal);
    }

    @Override
    public void finishPickingImage(Bitmap bitmap, int actionId) {
        super.finishPickingImage(bitmap, actionId);

        if (pickingCoverImage) { // Cover
            if (bitmap != null)
                UserAssetManager.editCover(this, bitmap);
            else
                UserAssetManager.deleteCover(this);
        }
        else { // Avatar
            if (actionId == R.id.action_facebook_import)
                UserAssetManager.importAvatar(this, GTExternalProvider.GTExternalProviderType.FACEBOOK);
            else if (bitmap != null)
                UserAssetManager.editAvatar(this, bitmap);
            else
                UserAssetManager.deleteAvatar(this);
        }
    }

    // Loading

    public void reloadUserProfile() {
        GTSDK.getUserManager().getFullUserProfile(user.id, !profileRefreshedOnce, new GTResponseHandler<GTUserResponse>() {

            @Override
            public void onSuccess(GTResponse<GTUserResponse> gtResponse) {
                user = gtResponse.getObject().user;
                finishLoadingUserProfile();
            }

            @Override
            public void onFailure(GTResponse<GTUserResponse> gtResponse) {
                DialogBuilder.buildAPIErrorDialog(ProfileActivity.this, getString(R.string.app_name), ApiUtils.localizedErrorReason(gtResponse), gtResponse.getResultCode());
            }

            @Override
            public void onCache(GTResponse<GTUserResponse> gtResponse) {
                super.onCache(gtResponse);
                user = gtResponse.getObject().user;
                finishLoadingUserProfile();
            }
        });
        profileRefreshedOnce = true;
    }

    private void finishLoadingUserProfile() {
        if (isDestroyed()) return; // View is destroyed.

        content.setUser(user);
        content.loadFollowButton();
        content.loadUserCountData();
        content.loadUserNamesAndHeaderData();
        content.loadUserAssets();
    }

    // Setup

    private void setupTopBar() {
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupContent() {
        content = new UserProfileFragment();
        content.setUser(user);
        content.hasOptionsMenu = true;
        Bundle args = new Bundle();
        args.putInt(Constants.EXTRA_USER, user.id);
        content.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, content);
        transaction.commit();
    }

    private void setupEventListeners() {
        GTSDK.registerEventListener(this);
    }
}
