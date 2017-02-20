package com.graffitab.ui.adapters.streamables.viewholders;

import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitab.application.MyApplication;
import com.graffitab.ui.views.likeimageview.LikeImageView;
import com.graffitab.utils.image.ImageUtils;
import com.graffitabsdk.model.GTStreamable;
import com.like.LikeButton;
import com.like.OnLikeListener;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.format.DateUtils.FORMAT_ABBREV_ALL;

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
    @BindView(R.id.likeAnimationButton) public LikeButton likeAnimationButton;
    @BindView(R.id.likeStatus) public TextView likeStatus;
    @BindView(R.id.likeButton) public View likeButton;
    @BindView(R.id.commentButton) public View commentButton;
    @BindView(R.id.shareButton) public View shareButton;

    public ListStreamableViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
//        setupImageView();
    }

    @Override
    public void setItem(GTStreamable streamable) {
        super.setItem(streamable);

        nameField.setText(streamable.user.fullName());
        usernameField.setText(streamable.user.mentionUsername());

        int flags = FORMAT_ABBREV_ALL;
        dateField.setText(DateUtils.getRelativeTimeSpanString(item.createdOn.getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, flags));

        int color = !streamable.likedByCurrentUser ? MyApplication.getInstance().getResources().getColor(R.color.colorMetadata) : MyApplication.getInstance().getResources().getColor(R.color.colorPrimary);
        likeAnimationButton.setLiked(streamable.likedByCurrentUser);
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
        streamableView.loadImage(item.asset.link);
    }

    public void loadAvatar() {
        ImageUtils.setAvatar(avatar, item.user);
    }

    @Override
    protected void setupViews() {
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
                likeAnimationButton.onClick(null);
            }
        });
        likeAnimationButton.setOnLikeListener(new OnLikeListener() {

            @Override
            public void liked(LikeButton likeButton) {
                streamableView.animateLike();
                onClickToggleLike();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
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

        // Custom handler for Like animation.
        streamableView.setLikeImageScale(0.3f);
        streamableView.setLikeListener(new LikeImageView.OnLikeListener() {

            @Override
            public void onLiked() {
                if (!item.likedByCurrentUser)
                    likeAnimationButton.onClick(null);
            }

            @Override
            public void onTapped() {
                onClickDetails();
            }
        });
    }
}
