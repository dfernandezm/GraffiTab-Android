package com.graffitab.ui.adapters.comments.viewholders;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitab.ui.adapters.comments.OnCommentClickListener;
import com.graffitabsdk.model.GTComment;
import com.luseen.autolinklibrary.AutoLinkMode;
import com.luseen.autolinklibrary.AutoLinkOnClickListener;
import com.luseen.autolinklibrary.AutoLinkTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.graffitab.R.id.textField;

/**
 * Created by georgichristov on 09/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class CommentViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.avatar) public ImageView avatar;
    @BindView(R.id.nameField) public TextView nameField;
    @BindView(R.id.usernameField) public TextView usernameField;
    @BindView(textField) public AutoLinkTextView autoLinkTextView;

    protected GTComment item;
    protected OnCommentClickListener clickListener;

    public CommentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        setupViews();
    }

    public void setItem(GTComment comment) {
        this.item = comment;

        autoLinkTextView.setAutoLinkText(comment.text);
    }

    public void setClickListener(OnCommentClickListener listener) {
        this.clickListener = listener;
    }

    public GTComment getItem() {
        return item;
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
