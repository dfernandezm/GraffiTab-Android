package com.graffitab.ui.activities.home.users;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.github.clans.fab.FloatingActionButton;
import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.ui.activities.custom.CameraUtilsActivity;
import com.graffitab.ui.activities.home.me.edit.EditProfileActivity;
import com.graffitab.ui.fragments.streamables.GenericStreamablesFragment;
import com.graffitab.ui.fragments.users.profile.UserProfileFragment;
import com.graffitab.utils.image.ImageUtils;
import com.graffitabsdk.model.GTUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by georgichristov on 20/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class ProfileActivity extends CameraUtilsActivity {

    @BindView(R.id.fab) FloatingActionButton fab;

    private GTUser user;
    private UserProfileFragment content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = (GTUser) extras.getSerializable(Constants.EXTRA_USER);
        }
        else finish();

        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        setupTopBar();
        setupContent();
        setupButtons();
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

    @OnClick(R.id.fab)
    public void onClickFollow(View view) {

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
        showImagePicker((ImageView) view);
    }

    public void onClickCover(View view) {
        showImagePicker((ImageView) view);
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

    private void setupButtons() {
        fab.setImageDrawable(ImageUtils.tintIcon(this, R.drawable.ic_action_follow, getResources().getColor(R.color.colorPrimary)));
        fab.setVisibility(user.isMe() ? View.GONE : View.VISIBLE);
    }
}
