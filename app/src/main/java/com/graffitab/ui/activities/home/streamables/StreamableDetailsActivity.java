package com.graffitab.ui.activities.home.streamables;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.cocosw.bottomsheet.BottomSheet;
import com.graffitab.R;
import com.graffitab.ui.activities.home.streamables.explorer.ExplorerActivity;
import com.graffitab.ui.activities.home.users.ProfileActivity;
import com.graffitab.utils.activity.ActivityUtils;

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

    @BindView(R.id.streamableView) ImageView streamableView;

    private PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUtils.hideActionBar(this);
        setContentView(R.layout.activity_streamable_details);
        ButterKnife.bind(this);

        setupDummyContent();
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

    // Setup

    private void setupDummyContent() {
        Drawable bitmap = getResources().getDrawable(R.drawable.login);
        streamableView.setImageDrawable(bitmap);

        mAttacher = new PhotoViewAttacher(streamableView);
    }
}
