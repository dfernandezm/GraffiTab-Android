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
import com.graffitab.ui.activities.custom.CameraUtilsActivity;
import com.graffitab.ui.fragments.streamable.GenericStreamablesFragment;
import com.graffitab.ui.fragments.user.profile.UserProfileFragment;
import com.graffitab.utils.ImageUtils;

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

    private UserProfileFragment content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

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

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    public void onClickFollow(View view) {

    }

    public void onClickPosts(View view) {
        startActivity(new Intent(this, UserPostsActivity.class));
    }

    public void onClickFollowers(View view) {
        startActivity(new Intent(this, UserFollowersActivity.class));
    }

    public void onClickFollowing(View view) {
        startActivity(new Intent(this, UserFollowingActivity.class));
    }

    public void onClickGrid(View view) {
        content.switchViewType(GenericStreamablesFragment.ViewType.GRID);
    }

    public void onClickList(View view) {
        content.switchViewType(GenericStreamablesFragment.ViewType.LIST_FULL);
    }

    public void onClickLikes(View view) {
        startActivity(new Intent(this, UserLikesActivity.class));
    }

    public void onClickMentions(View view) {
        startActivity(new Intent(this, UserMentionsActivity.class));
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
        content.hasOptionsMenu = true;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, content);
        transaction.commit();
    }

    private void setupButtons() {
        fab.setImageDrawable(ImageUtils.tintIcon(this, R.drawable.ic_action_follow, getResources().getColor(R.color.colorPrimary)));
    }
}
