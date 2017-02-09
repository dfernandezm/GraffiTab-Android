package com.graffitab.ui.activities.home.streamables;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.ui.activities.home.streamables.explorer.ExplorerActivity;
import com.graffitab.ui.activities.home.users.ProfileActivity;
import com.graffitab.ui.dialog.DialogBuilder;
import com.graffitab.ui.dialog.OnYesNoHandler;
import com.graffitab.ui.dialog.TaskDialog;
import com.graffitab.utils.Utils;
import com.graffitab.utils.activity.ActivityUtils;
import com.graffitab.utils.api.ApiUtils;
import com.graffitab.utils.image.ImageUtils;
import com.graffitabsdk.model.GTStreamable;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.network.common.result.GTActionCompleteResult;
import com.graffitabsdk.network.service.streamable.response.GTStreamableResponse;
import com.graffitabsdk.sdk.GTSDK;
import com.graffitabsdk.sdk.events.comments.GTCommentDeletedEvent;
import com.graffitabsdk.sdk.events.comments.GTCommentPostedEvent;
import com.graffitabsdk.sdk.events.streamables.GTStreamableLikedEvent;
import com.graffitabsdk.sdk.events.streamables.GTStreamableUnlikedEvent;
import com.graffitabsdk.sdk.events.users.GTUserProfileUpdatedEvent;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by georgichristov on 03/01/2017
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class StreamableDetailsActivity extends AppCompatActivity {

    @BindView(R.id.avatar) ImageView avatar;
    @BindView(R.id.nameField) public TextView nameField;
    @BindView(R.id.usernameField) public TextView usernameField;
    @BindView(R.id.streamableView) ImageView streamableView;
    @BindView(R.id.likesField) public TextView likesField;
    @BindView(R.id.commentsField) public TextView commentsField;
    @BindView(R.id.likeBtn) public View likeBtn;
    @BindView(R.id.likeIcon) public ImageView likeIcon;
    @BindView(R.id.commentIcon) public ImageView commentIcon;
    @BindView(R.id.topDisplay) public View topDisplay;
    @BindView(R.id.bottomDisplay) public View bottomDisplay;

    private GTStreamable streamable;
    private PhotoViewAttacher mAttacher;

    public static void openStreamableDetails(Context context, GTStreamable streamable) {
        Intent intent = new Intent(context, StreamableDetailsActivity.class);
        intent.putExtra(Constants.EXTRA_STREAMABLE, streamable);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getSerializable(Constants.EXTRA_STREAMABLE) != null) {
            streamable = (GTStreamable) extras.getSerializable(Constants.EXTRA_STREAMABLE);
        }
        else {
            finish();
            return;
        }

        ActivityUtils.hideActionBar(this);
        setContentView(R.layout.activity_streamable_details);
        ButterKnife.bind(this);

        setupImageView();
        setupImageViews();
        setupEventListeners();
        setupDisplays();

        loadUserAndStreamableData();
        refreshStreamable();

        Utils.runWithDelay(new Runnable() {

            @Override
            public void run() {
                toggleDisplay();
            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        GTSDK.unregisterEventListener(this);
        super.onDestroy();
    }

    @OnClick(R.id.avatar)
    public void onClickAvatar(View view) {
        onClickProfile();
    }

    @OnClick(R.id.nameField)
    public void onClickName(View view) {
        onClickProfile();
    }

    @OnClick(R.id.usernameField)
    public void onClickUsername(View view) {
        onClickProfile();
    }

    @OnClick(R.id.close)
    public void onClickClose(View view) {
        finish();
    }

    @OnClick(R.id.optionsBtn)
    public void onClickOptions(View view) {
        BottomSheet.Builder builder = new BottomSheet.Builder(this, R.style.BottomSheet_StyleDialog)
                .title(R.string.streamable_details_menu_title)
                .sheet(streamable.isMine() ? streamable.isPrivate ? R.menu.menu_streamable_details_mine_public : R.menu.menu_streamable_details_mine_private : R.menu.menu_streamable_details);

        builder = builder.listener(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == R.id.action_edit) {

                }
                else if (which == R.id.action_toggle_privacy) {
                    togglePrivacy();
                }
                else if (which == R.id.action_save) {
                    save();
                }
                else if (which == R.id.action_flag) {
                    flag();
                }
                else if (which == R.id.action_explore) {
                    ExplorerActivity.openForLocation(StreamableDetailsActivity.this, streamable.latitude, streamable.longitude);
                }
                else if (which == R.id.action_remove) {
                    delete();
                }
            }
        });
        builder.show();
    }

    @OnClick(R.id.shareBtn)
    public void onClickShare(View view) {
        Utils.shareImage(this, ((BitmapDrawable) streamableView.getDrawable()).getBitmap());
    }

    @OnClick(R.id.likeBtn)
    public void onClickToggleLike(View view) {
        streamable.likedByCurrentUser = !streamable.likedByCurrentUser;
        if (streamable.likedByCurrentUser) {
            streamable.addToLikersCount();
            GTSDK.getStreamableManager().like(streamable.id, new GTResponseHandler<GTStreamableResponse>() {

                @Override
                public void onSuccess(GTResponse<GTStreamableResponse> gtResponse) {}

                @Override
                public void onFailure(GTResponse<GTStreamableResponse> gtResponse) {}
            });
        }
        else {
            streamable.removeFromLikersCount();
            GTSDK.getStreamableManager().unlike(streamable.id, new GTResponseHandler<GTStreamableResponse>() {

                @Override
                public void onSuccess(GTResponse<GTStreamableResponse> gtResponse) {}

                @Override
                public void onFailure(GTResponse<GTStreamableResponse> gtResponse) {}
            });
        }
        loadUserAndStreamableData();
    }

    @OnClick(R.id.commentBtn)
    public void onClickComment(View view) {
        Intent i = new Intent(this, CommentsActivity.class);
        i.putExtra(Constants.EXTRA_STREAMABLE, streamable);
        startActivity(i);
    }

    private void onClickProfile() {
        ProfileActivity.show(streamable.user, this);
    }

    // Events

    @Subscribe
    public void userProfileUpdatedEvent(GTUserProfileUpdatedEvent event) {
        if (streamable.user.equals(event.getUser())) {
            streamable.user = event.getUser();
            loadUserAndStreamableData();
        }
    }

    @Subscribe
    public void streamableLikedEvent(GTStreamableLikedEvent event) {
        refreshStreamableAfterLikeToggle(event.getStreamable());
    }

    @Subscribe
    public void streamableUnlikedEvent(GTStreamableUnlikedEvent event) {
        refreshStreamableAfterLikeToggle(event.getStreamable());
    }

    @Subscribe
    public void commentPostedEvent(GTCommentPostedEvent event) {
        if (streamable.equals(event.getComment().streamable)) {
            streamable = event.getComment().streamable;
            loadUserAndStreamableData();
        }
    }

    @Subscribe
    public void commentDeletedEvent(GTCommentDeletedEvent event) {
        if (streamable.id == event.getStreamableId()) {
            streamable.removeFromCommentsCount();
            loadUserAndStreamableData();
        }
    }

    private void refreshStreamableAfterLikeToggle(GTStreamable toggledStreamable) {
        if (streamable.equals(toggledStreamable)) {
            streamable = toggledStreamable;
            loadUserAndStreamableData();
        }
    }

    private void togglePrivacy() {
        streamable.isPrivate = !streamable.isPrivate;
        if (streamable.isPrivate)
            GTSDK.getStreamableManager().makePrivate(streamable.id, new GTResponseHandler<GTStreamableResponse>() {

                @Override
                public void onSuccess(GTResponse<GTStreamableResponse> gtResponse) {}

                @Override
                public void onFailure(GTResponse<GTStreamableResponse> gtResponse) {}
            });
        else
            GTSDK.getStreamableManager().makePublic(streamable.id, new GTResponseHandler<GTStreamableResponse>() {

                @Override
                public void onSuccess(GTResponse<GTStreamableResponse> gtResponse) {}

                @Override
                public void onFailure(GTResponse<GTStreamableResponse> gtResponse) {}
            });
    }

    private void flag() {
        DialogBuilder.buildYesNoDialog(this, getString(R.string.app_name), getString(R.string.streamable_details_flag_prompt), getString(R.string.streamable_details_flag), getString(R.string.other_cancel), new OnYesNoHandler() {

            @Override
            public void onClickYes() {
                GTSDK.getStreamableManager().flag(streamable.id, new GTResponseHandler<GTStreamableResponse>() {

                    @Override
                    public void onSuccess(GTResponse<GTStreamableResponse> gtResponse) {}

                    @Override
                    public void onFailure(GTResponse<GTStreamableResponse> gtResponse) {
                        DialogBuilder.buildAPIErrorDialog(StreamableDetailsActivity.this, getString(R.string.app_name), ApiUtils.localizedErrorReason(gtResponse), true, gtResponse.getResultCode());
                    }
                });
            }

            @Override
            public void onClickNo() {}
        });
    }

    private void delete() {
        DialogBuilder.buildYesNoDialog(this, getString(R.string.app_name), getString(R.string.other_confirm_delete), getString(R.string.other_delete), getString(R.string.other_cancel), new OnYesNoHandler() {

            @Override
            public void onClickYes() {
                TaskDialog.getInstance().showDialog(getString(R.string.other_processing), StreamableDetailsActivity.this, null);
                GTSDK.getStreamableManager().delete(streamable.id, new GTResponseHandler<GTActionCompleteResult>() {

                    @Override
                    public void onSuccess(GTResponse<GTActionCompleteResult> gtResponse) {
                        TaskDialog.getInstance().hideDialog();
                        finish();
                    }

                    @Override
                    public void onFailure(GTResponse<GTActionCompleteResult> gtResponse) {
                        TaskDialog.getInstance().hideDialog();
                        DialogBuilder.buildAPIErrorDialog(StreamableDetailsActivity.this, getString(R.string.app_name), ApiUtils.localizedErrorReason(gtResponse), true, gtResponse.getResultCode());
                    }
                });
            }

            @Override
            public void onClickNo() {}
        });
    }

    private void save() {
        new Thread() {

            @Override
            public void run() {
                BitmapDrawable drawable = (BitmapDrawable) streamableView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, streamable.user.fullName() + "_" + streamable.createdOn.toString(), "");

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(StreamableDetailsActivity.this, getString(R.string.streamable_details_save_success), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }.start();
    }

    private void toggleDisplay() {
        if (topDisplay.getAlpha() == 0) {
            topDisplay.animate().alpha(1.0f);
            bottomDisplay.animate().alpha(1.0f);
        }
        else {
            topDisplay.animate().alpha(0.0f);
            bottomDisplay.animate().alpha(0.0f);
        }
    }

    // Loading

    private void loadUserAndStreamableData() {
        nameField.setText(streamable.user.fullName());
        usernameField.setText(streamable.user.mentionUsername());

        int background = streamable.likedByCurrentUser ? R.drawable.rounded_corner_streamable_detail_liked : R.drawable.rounded_corner_streamable_detail;
        likeBtn.setBackgroundResource(background);

        likesField.setText(streamable.likersCount + "");
        commentsField.setText(streamable.commentsCount + "");

        loadAvatar();
    }

    private void loadAvatar() {
        int p = R.drawable.default_avatar;
        if (streamable.user.hasAvatar())
            Picasso.with(this).load(streamable.user.avatar.thumbnail).placeholder(p).error(p).into(avatar);
        else
            Picasso.with(this).load(p).placeholder(p).into(avatar);
    }

    private void refreshStreamable() {
        GTSDK.getStreamableManager().getStreamable(streamable.id, true, new GTResponseHandler<GTStreamableResponse>() {

            @Override
            public void onSuccess(GTResponse<GTStreamableResponse> gtResponse) {
                streamable = gtResponse.getObject().streamable;
                finishRefresh();
            }

            @Override
            public void onFailure(GTResponse<GTStreamableResponse> gtResponse) {
                finishRefresh();
            }

            @Override
            public void onCache(GTResponse<GTStreamableResponse> gtResponse) {
                super.onCache(gtResponse);
                streamable = gtResponse.getObject().streamable;
                finishRefresh();
            }
        });
    }

    private void finishRefresh() {
        if (avatar == null) return; // View is destroyed.

        loadUserAndStreamableData();
        Picasso.with(this).load(streamable.asset.link).into(streamableView, new Callback() {

            @Override
            public void onSuccess() {
                mAttacher.update();
            }

            @Override
            public void onError() {
                mAttacher.update();
            }
        });
    }

    // Setup

    private void setupImageView() {
        mAttacher = new PhotoViewAttacher(streamableView);
        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View view, float x, float y) {
                toggleDisplay();
            }

            @Override
            public void onOutsidePhotoTap() {}
        });
    }

    private void setupImageViews() {
        likeIcon.setImageDrawable(ImageUtils.tintIcon(this, R.drawable.ic_thumb_up_black_24dp, Color.WHITE));
        commentIcon.setImageDrawable(ImageUtils.tintIcon(this, R.drawable.ic_chat_bubble_black_24dp, Color.WHITE));
    }

    private void setupEventListeners() {
        GTSDK.registerEventListener(this);
    }

    private void setupDisplays() {
        topDisplay.setAlpha(0.0f);
        bottomDisplay.setAlpha(0.0f);
    }
}
