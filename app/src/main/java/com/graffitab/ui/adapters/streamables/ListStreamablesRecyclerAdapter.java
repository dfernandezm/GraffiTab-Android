package com.graffitab.ui.adapters.streamables;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.IntRange;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.ui.adapters.BaseItemRecyclerAdapter;
import com.graffitab.ui.fragments.streamable.GenericStreamablesFragment;
import com.graffitab.utils.display.DisplayUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 15/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class ListStreamablesRecyclerAdapter extends BaseItemRecyclerAdapter<GenericStreamablesFragment.GTStreamble> {

    public ListStreamablesRecyclerAdapter(Context context, List<GenericStreamablesFragment.GTStreamble> items) {
        super(context, items);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_streamable_list, null);
        ListViewHolder rcv = new ListViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final GenericStreamablesFragment.GTStreamble item = getItem(position);

        ((ListViewHolder) holder).streamableView.setBackgroundColor(Color.parseColor(Constants.PALLETE[position % Constants.PALLETE.length]));
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.avatar) ImageView avatar;
        @BindView(R.id.nameField) TextView nameField;
        @BindView(R.id.usernameField) TextView usernameField;
        @BindView(R.id.dateField) TextView dateField;
        @BindView(R.id.streamableView) ImageView streamableView;
        @BindView(R.id.likesField) TextView likesField;
        @BindView(R.id.commentsField) TextView commentsField;
        @BindView(R.id.likeStatusImage) ImageView likeStatusImage;
        @BindView(R.id.likeButton) View likeButton;
        @BindView(R.id.commentButton) View commentButton;
        @BindView(R.id.shareButton) View shareButton;

        public ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class RecyclerViewMargin extends RecyclerView.ItemDecoration {

        private final int columns;

        public RecyclerViewMargin(@IntRange(from = 0) int columns) {
            this.columns = columns;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int itemCount = state.getItemCount();
            int position = parent.getChildLayoutPosition(view);

            outRect.right = 0;
            outRect.left = 0;
            outRect.top = DisplayUtils.pxToDip(parent.getContext(), 10);

            if (position == itemCount - 1)
                outRect.bottom = DisplayUtils.pxToDip(parent.getContext(), 10);
        }
    }
}
