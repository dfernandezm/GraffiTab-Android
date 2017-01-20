package com.graffitab.ui.activities.home.streamables;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;
import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.ui.activities.home.streamables.explorer.ExplorerActivity;
import com.graffitab.ui.activities.home.users.ProfileActivity;
import com.graffitab.utils.activity.ActivityUtils;
import com.graffitabsdk.config.GTSDK;
import com.graffitabsdk.model.GTStreamable;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.network.service.streamable.response.GTStreamableResponse;
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

    private GTStreamable streamable;
    private PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            streamable = (GTStreamable) extras.getSerializable(Constants.EXTRA_STREAMABLE);
        }
        else finish();

        ActivityUtils.hideActionBar(this);
        setContentView(R.layout.activity_streamable_details);
        ButterKnife.bind(this);

        setupImageView();

        loadData();
        refreshStreamable();
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
                .sheet(R.menu.menu_streamable_details);

        builder = builder.listener(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == R.id.action_edit) {

                }
                else if (which == R.id.action_toggle_privacy) {

                }
                else if (which == R.id.action_save) {

                }
                else if (which == R.id.action_flag) {

                }
                else if (which == R.id.action_explore) {
                    startActivity(new Intent(StreamableDetailsActivity.this, ExplorerActivity.class));
                }
                else if (which == R.id.action_remove) {
                    finish();
                }
            }
        });
        builder.show();
    }

    @OnClick(R.id.shareBtn)
    public void onClickShare(View view) {
        System.out.println("SHARE");
    }

    @OnClick(R.id.likeBtn)
    public void onClickToggleLike(View view) {
        System.out.println("LIKE");
    }

    @OnClick(R.id.commentBtn)
    public void onClickComment(View view) {
        startActivity(new Intent(this, CommentsActivity.class));
    }

    private void onClickProfile() {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    // Loading

    private void loadData() {
        nameField.setText(streamable.user.fullName());
        usernameField.setText(streamable.user.mentionUsername());

        int background = streamable.likedByCurrentUser ? R.drawable.rounded_corner_streamable_detail_liked : R.drawable.rounded_corner_streamable_detail;
        likeBtn.setBackgroundResource(background);

        likesField.setText(streamable.likersCount + "");
        commentsField.setText(streamable.commentsCount + "");

        loadAvatar();
    }

    private void loadAvatar() {
        if (streamable.user.hasAvatar())
            Picasso.with(this).load(streamable.user.avatar.thumbnail).error(R.drawable.default_avatar).into(avatar);
        else
            Picasso.with(this).load(R.drawable.default_avatar).into(avatar);
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
    }
}
