package com.graffitab.ui.adapters.comments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.graffitab.R;
import com.graffitab.ui.adapters.comments.viewholders.ListCommentViewHolder;
import com.graffitab.ui.fragments.comments.GenericCommentsFragment;
import com.graffitab.ui.views.recyclerview.components.AdvancedEndlessRecyclerViewAdapter;
import com.graffitabsdk.model.GTComment;

import java.util.List;

/**
 * Created by georgichristov on 09/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class GenericCommentsRecyclerViewAdapter extends AdvancedEndlessRecyclerViewAdapter<GTComment>  {

    private final int VIEW_TYPE_LIST_FULL = 0;

    private GenericCommentsFragment.ViewType itemViewType;
    private OnCommentClickListener clickListener;

    public GenericCommentsRecyclerViewAdapter(Context context, List<GTComment> items, RecyclerView recyclerView) {
        super(context, items, recyclerView);
    }

    public void setViewType(GenericCommentsFragment.ViewType type) {
        this.itemViewType = type;
    }

    public void setClickListener(OnCommentClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public int getViewType(int position) {
        switch (itemViewType) {
            case LIST_FULL: return VIEW_TYPE_LIST_FULL;
        }

        return VIEW_TYPE_LIST_FULL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LIST_FULL) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comment_list, parent, false);
            final ListCommentViewHolder rcv = new ListCommentViewHolder(layoutView);
            rcv.setClickListener(clickListener);
            return rcv;
        }
        else {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comment_list, parent, false);
            final ListCommentViewHolder rcv = new ListCommentViewHolder(layoutView);
            rcv.setClickListener(clickListener);
            return rcv;
        }
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final GTComment item = getItem(position);

        if (holder instanceof ListCommentViewHolder) {
            ListCommentViewHolder customHolder = (ListCommentViewHolder) holder;
            customHolder.setItem(item);
        }
    }
}
