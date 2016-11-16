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
import android.widget.RelativeLayout;

import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.ui.adapters.BaseItemRecyclerAdapter;
import com.graffitab.ui.fragments.streamable.GenericStreamablesFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class SwimlaneStreamablesRecyclerAdapter extends BaseItemRecyclerAdapter<GenericStreamablesFragment.GTStreamble> {

    public SwimlaneStreamablesRecyclerAdapter(Context context, List<GenericStreamablesFragment.GTStreamble> items) {
        super(context, items);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_swimlane, null);
        SwimlaneViewHolder rcv = new SwimlaneViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final GenericStreamablesFragment.GTStreamble item = getItem(position);

        SwimlaneViewHolder streamableHolder = (SwimlaneViewHolder) holder;
        streamableHolder.streamableView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, item.height));
        streamableHolder.streamableView.setBackgroundColor(Color.parseColor(Constants.PALLETE[position % Constants.PALLETE.length]));
    }

    static class SwimlaneViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.streamableView) ImageView streamableView;

        public SwimlaneViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class RecyclerViewMargin extends RecyclerView.ItemDecoration {

        private final int spanCount;
        private int spacing;

        public RecyclerViewMargin(@IntRange(from = 0) int columns) {
            this.spacing = 5;
            this.spanCount = columns;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (parent.getPaddingLeft() != spacing) {
                parent.setPadding(spacing, spacing, spacing, spacing);
                parent.setClipToPadding(false);
            }

            outRect.top = spacing;
            outRect.bottom = spacing;
            outRect.left = spacing;
            outRect.right = spacing;
        }
    }
}
