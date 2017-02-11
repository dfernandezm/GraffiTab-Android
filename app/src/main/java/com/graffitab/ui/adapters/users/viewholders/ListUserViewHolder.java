package com.graffitab.ui.adapters.users.viewholders;

import android.view.View;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitab.application.MyApplication;
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
    @BindView(R.id.followButton) public View followBtn;
    @BindView(R.id.followButtonText) public TextView followText;

    public ListUserViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setItem(GTUser user) {
        super.setItem(user);

        nameField.setText(item.fullName());
        usernameField.setText(item.mentionUsername());

        followText.setVisibility(user.isMe() ? View.GONE : View.VISIBLE);
        followText.setBackgroundResource(user.followedByCurrentUser ? R.drawable.rounded_corner_user_list_unfollow : R.drawable.rounded_corner_user_list_follow);
        followText.setTextColor(MyApplication.getInstance().getResources().getColor(user.followedByCurrentUser ? R.color.colorPrimary : R.color.colorWhite));
        followText.setText(user.followedByCurrentUser ? R.string.profile_following : R.string.profile_follow);
    }

    @Override
    protected void setupViews() {
        super.setupViews();

        followBtn.setClickable(true);
        followBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onClickToggleFollow();
            }
        });
    }
}
