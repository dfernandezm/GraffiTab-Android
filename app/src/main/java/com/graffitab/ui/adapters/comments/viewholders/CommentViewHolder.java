package com.graffitab.ui.adapters.comments.viewholders;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitab.application.MyApplication;
import com.graffitab.model.GTCommentExtension;
import com.graffitab.ui.adapters.comments.OnCommentClickListener;
import com.graffitabsdk.model.GTComment;
import com.luseen.autolinklibrary.AutoLinkMode;
import com.luseen.autolinklibrary.AutoLinkOnClickListener;
import com.luseen.autolinklibrary.AutoLinkTextView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.format.DateUtils.FORMAT_ABBREV_ALL;

/**
 * Created by georgichristov on 09/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class CommentViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.avatar) public ImageView avatar;
    @BindView(R.id.nameField) public TextView nameField;
    @BindView(R.id.usernameField) public TextView usernameField;
    @BindView(R.id.textField) public AutoLinkTextView autoLinkTextView;
    @BindView(R.id.dateField) public TextView dateField;
    @BindView(R.id.errorBtn) public ImageButton errorBtn;

    protected GTComment item;
    protected OnCommentClickListener clickListener;

    public CommentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        setupViews();
    }

    public void setItem(GTComment comment) {
        this.item = comment;

        nameField.setText(comment.user.fullName());
        usernameField.setText(comment.user.mentionUsername());

        int flags = FORMAT_ABBREV_ALL;
        dateField.setText(DateUtils.getRelativeTimeSpanString(item.createdOn.getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, flags));

        autoLinkTextView.setAutoLinkText(comment.text);
        if (comment.updatedOn != null) {
            String edited = " " + MyApplication.getInstance().getString(R.string.comments_edited);
            SpannableString span = new SpannableString(edited);
            span.setSpan(new ForegroundColorSpan(Color.parseColor("#d0d0d0")), 0, edited.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            span.setSpan(new AbsoluteSizeSpan((int) autoLinkTextView.getTextSize() - 15), 0, edited.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            autoLinkTextView.append(span);
        }

        // Comment state.
        if (comment instanceof GTCommentExtension) {
            GTCommentExtension commentExtension = (GTCommentExtension) comment;
            errorBtn.setVisibility(commentExtension.getState() == GTCommentExtension.State.FAILED ? View.VISIBLE : View.GONE);
            autoLinkTextView.setTextColor(commentExtension.getState() == GTCommentExtension.State.SENT ? Color.parseColor("#555555") : Color.parseColor("#d0d0d0"));
        }
        else {
            errorBtn.setVisibility(View.GONE);
            autoLinkTextView.setTextColor(Color.parseColor("#555555"));
        }

        loadAvatar();
    }

    public void setClickListener(OnCommentClickListener listener) {
        this.clickListener = listener;
    }

    public GTComment getItem() {
        return item;
    }

    public void loadAvatar() {
        int p = R.drawable.default_avatar;
        if (item.user.hasAvatar())
            Picasso.with(avatar.getContext()).load(item.user.avatar.thumbnail).placeholder(p).error(p).into(avatar);
        else
            Picasso.with(avatar.getContext()).load(p).placeholder(p).into(avatar);
    }

    // Setup

    protected void setupViews() {
        View.OnClickListener profileListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (clickListener != null)
                    clickListener.onOpenCommenterProfile(item, item.user, getAdapterPosition());
            }
        };
        avatar.setClickable(true);
        avatar.setOnClickListener(profileListener);
        nameField.setClickable(true);
        nameField.setOnClickListener(profileListener);
        usernameField.setClickable(true);
        usernameField.setOnClickListener(profileListener);

        View.OnClickListener errorListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (clickListener != null)
                    clickListener.onErrorSelected(item, getAdapterPosition());
            }
        };
        errorBtn.setOnClickListener(errorListener);

        autoLinkTextView.setHashtagModeColor(ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary));
        autoLinkTextView.setMentionModeColor(ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary));
        autoLinkTextView.setUrlModeColor(ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary));
        autoLinkTextView.addAutoLinkMode(AutoLinkMode.MODE_URL, AutoLinkMode.MODE_HASHTAG, AutoLinkMode.MODE_MENTION);
        autoLinkTextView.setAutoLinkOnClickListener(new AutoLinkOnClickListener() {

            @Override
            public void onAutoLinkTextClick(AutoLinkMode autoLinkMode, String matchedText) {
                if (clickListener == null)
                    return;

                if (autoLinkMode == AutoLinkMode.MODE_URL)
                    clickListener.onOpenLink(item, matchedText.trim(), getAdapterPosition());
                else if (autoLinkMode == AutoLinkMode.MODE_HASHTAG) {
                    String text = matchedText.substring(1);
                    clickListener.onOpenHashtag(item, text, getAdapterPosition());
                }
                else if (autoLinkMode == AutoLinkMode.MODE_MENTION) {
                    String text = matchedText.substring(1);
                    clickListener.onOpenMention(item, text, getAdapterPosition());
                }
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (clickListener != null)
                    clickListener.onRowSelected(item, getAdapterPosition());
            }
        });

        View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (clickListener != null)
                    clickListener.onRowLongSelected(item, getAdapterPosition());
                return true;
            }
        };
        itemView.setOnLongClickListener(longClickListener);
        autoLinkTextView.setOnLongClickListener(longClickListener);
    }
}
