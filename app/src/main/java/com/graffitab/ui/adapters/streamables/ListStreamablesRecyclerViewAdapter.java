package com.graffitab.ui.adapters.streamables;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.IntRange;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.graffitabsdk.model.GTStreamable;
import com.graffitab.ui.views.recyclerview.components.CustomRecyclerViewAdapter;
import com.graffitab.ui.adapters.streamables.viewholders.ListStreamableViewHolder;
import com.graffitab.utils.display.DisplayUtils;

import java.util.List;

/**
 * Created by georgichristov on 15/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class ListStreamablesRecyclerViewAdapter extends CustomRecyclerViewAdapter<GTStreamable> {

    public ListStreamablesRecyclerViewAdapter(Context context, List<GTStreamable> items) {
        super(context, items);
    }

    @Override
    public RecyclerView.ViewHolder onCreateCustomViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_streamable_list, parent, false);
        ListStreamableViewHolder rcv = new ListStreamableViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindCustomViewHolder(RecyclerView.ViewHolder holder, int position) {
        final GTStreamable item = getItem(position);

        ((ListStreamableViewHolder) holder).streamableView.setBackgroundColor(Color.parseColor(Constants.PALLETE[position % Constants.PALLETE.length]));
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
