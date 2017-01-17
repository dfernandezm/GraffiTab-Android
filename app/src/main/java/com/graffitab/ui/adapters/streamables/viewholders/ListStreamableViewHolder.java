package com.graffitab.ui.adapters.streamables.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitab.application.MyApplication;
import com.graffitab.utils.image.ImageUtils;
import com.graffitabsdk.model.GTStreamable;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 17/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class ListStreamableViewHolder extends StreamableViewHolder {

    @BindView(R.id.avatar) public ImageView avatar;
    @BindView(R.id.nameField) public TextView nameField;
    @BindView(R.id.usernameField) public TextView usernameField;
    @BindView(R.id.dateField) public TextView dateField;
    @BindView(R.id.likesField) public TextView likesField;
    @BindView(R.id.commentsField) public TextView commentsField;
    @BindView(R.id.likeStatusImage) public ImageView likeStatusImage;
    @BindView(R.id.likeStatus) public TextView likeStatus;
    @BindView(R.id.likeButton) public View likeButton;
    @BindView(R.id.commentButton) public View commentButton;
    @BindView(R.id.shareButton) public View shareButton;

    public ListStreamableViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setItem(GTStreamable streamable) {
        super.setItem(streamable);

        nameField.setText(streamable.user.fullName());
        usernameField.setText(streamable.user.mentionUsername());

        int color = !streamable.likedByCurrentUser ? MyApplication.getInstance().getResources().getColor(R.color.colorMetadata) : MyApplication.getInstance().getResources().getColor(R.color.colorPrimary);
        likeStatusImage.setImageDrawable(ImageUtils.tintIcon(MyApplication.getInstance(), R.drawable.ic_thumb_up_black_24dp, color));
        likeStatus.setTextColor(color);
        likeStatus.setText(streamable.likedByCurrentUser ? MyApplication.getInstance().getString(R.string.likes_liked) : MyApplication.getInstance().getString(R.string.likes_like));

        boolean plural = streamable.likersCount != 1;
        likesField.setText(MyApplication.getInstance().getString(plural ? R.string.likes_count_plural : R.string.likes_count, streamable.likersCount));
        plural = streamable.commentsCount != 1;
        commentsField.setText(MyApplication.getInstance().getString(plural ? R.string.comments_count_plural : R.string.comments_count, streamable.commentsCount));

        loadAvatar();
    }

    @Override
    public void loadStreamableImage() {
        Picasso.with(streamableView.getContext()).load(item.asset.link).into(streamableView);
    }

    public void loadAvatar() {
        if (item.user.hasAvatar())
            Picasso.with(avatar.getContext()).load(item.user.avatar.thumbnail).into(avatar);
        else
            Picasso.with(avatar.getContext()).load(R.drawable.default_avatar).into(avatar);
    }

    @Override
    protected void setupViews() {
        super.setupViews();

        View.OnClickListener profileListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onClickUserProfile();
            }
        };

        avatar.setClickable(true);
        avatar.setOnClickListener(profileListener);
        usernameField.setClickable(true);
        usernameField.setOnClickListener(profileListener);
        nameField.setClickable(true);
        nameField.setOnClickListener(profileListener);

        likesField.setClickable(true);
        likesField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onClickLikes();
            }
        });

        View.OnClickListener commentListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onClickComments();
            }
        };
        commentsField.setClickable(true);
        commentsField.setOnClickListener(commentListener);
        commentButton.setClickable(true);
        commentButton.setOnClickListener(commentListener);

        likeButton.setClickable(true);
        likeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onClickToggleLike();
            }
        });

        shareButton.setClickable(true);
        shareButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onClickShare();
            }
        });
    }
}
