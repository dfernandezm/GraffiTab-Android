package com.graffitab.ui.adapters.users.viewholders;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitab.application.MyApplication;
import com.graffitab.utils.image.ImageUtils;
import com.graffitabsdk.model.GTUser;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 17/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class ListUserViewHolder extends UserViewHolder {

    @BindView(R.id.nameField) public TextView nameField;
    @BindView(R.id.usernameField) public TextView usernameField;
    @BindView(R.id.followButton) public ImageView followBtn;

    public ListUserViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setItem(GTUser user) {
        super.setItem(user);

        followBtn.setImageResource(user.followedByCurrentUser ? R.drawable.ic_action_unfollow : R.drawable.ic_action_follow);
        followBtn.setBackgroundResource(user.followedByCurrentUser ? R.drawable.rounded_corner_unfollow : R.drawable.rounded_corner_follow);
        followBtn.setImageDrawable(ImageUtils.tintIcon(MyApplication.getInstance(),
                user.followedByCurrentUser ? R.drawable.ic_action_unfollow : R.drawable.ic_action_follow,
                user.followedByCurrentUser ? Color.WHITE : MyApplication.getInstance().getResources().getColor(R.color.colorPrimary)));
    }
}
