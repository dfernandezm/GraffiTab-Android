package com.graffitab.ui.adapters.followersactivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.graffitab.R;
import com.graffitab.ui.adapters.followersactivity.viewholders.ActivityContainerViewHolder;
import com.graffitab.ui.adapters.followersactivity.viewholders.CommentActivityViewHolder;
import com.graffitab.ui.adapters.followersactivity.viewholders.CreateStreamableActivityViewHolder;
import com.graffitab.ui.adapters.followersactivity.viewholders.FollowActivityViewHolder;
import com.graffitab.ui.adapters.followersactivity.viewholders.LikeFollowersActivityViewHolder;
import com.graffitab.ui.views.recyclerview.AdvancedEndlessRecyclerViewAdapter;
import com.graffitabsdk.model.activity.GTActivity;
import com.graffitabsdk.model.activity.GTActivityContainer;

import java.util.List;

/**
 * Created by georgichristov on 15/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class GenericFollowersActivityRecyclerViewAdapter extends AdvancedEndlessRecyclerViewAdapter<GTActivityContainer> {

    // By convention 0 is the header view
    private final int VIEW_TYPE_LIKE_SINGLE = 1;
    private final int VIEW_TYPE_LIKE_MULTIPLE = 2;
    private final int VIEW_TYPE_FOLLOW_SINGLE = 3;
    private final int VIEW_TYPE_FOLLOW_MULTIPLE = 4;
    private final int VIEW_TYPE_CREATE_STREAMABLE_SINGLE = 5;
    private final int VIEW_TYPE_CREATE_STREAMABLE_MULTIPLE = 6;
    private final int VIEW_TYPE_COMMENT_SINGLE = 7;
    private final int VIEW_TYPE_COMMENT_MULTIPLE = 8;

    private OnFollowerActivityClickListener clickListener;

    public GenericFollowersActivityRecyclerViewAdapter(Context context, List<GTActivityContainer> items, RecyclerView recyclerView) {
        super(context, items, recyclerView);
    }

    public void setClickListener(OnFollowerActivityClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public int getViewType(int position) {
        GTActivityContainer item = getItem(position);
        GTActivity.GTActivityType type = item.type;
        boolean isSingle = item.isSingle();
        switch (type) {
            case LIKE: return isSingle ? VIEW_TYPE_LIKE_SINGLE : VIEW_TYPE_LIKE_MULTIPLE;
            case FOLLOW: return isSingle ? VIEW_TYPE_FOLLOW_SINGLE : VIEW_TYPE_FOLLOW_MULTIPLE;
            case CREATE_STREAMABLE: return isSingle ? VIEW_TYPE_CREATE_STREAMABLE_SINGLE : VIEW_TYPE_CREATE_STREAMABLE_MULTIPLE;
            case COMMENT: return isSingle ? VIEW_TYPE_COMMENT_SINGLE : VIEW_TYPE_COMMENT_MULTIPLE;
            default:
                throw new RuntimeException("The view type " + type + " is not supported");
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_LIKE_SINGLE:
            case VIEW_TYPE_LIKE_MULTIPLE: {
                int layoutId = viewType == VIEW_TYPE_LIKE_SINGLE ? R.layout.row_activity_like_single : R.layout.row_activity_like_multiple;
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
                final LikeFollowersActivityViewHolder rcv = new LikeFollowersActivityViewHolder(layoutView);
                rcv.setClickListener(clickListener);
                return rcv;
            }
            case VIEW_TYPE_FOLLOW_SINGLE:
            case VIEW_TYPE_FOLLOW_MULTIPLE: {
                int layoutId = viewType == VIEW_TYPE_FOLLOW_SINGLE ? R.layout.row_activity_follow_single : R.layout.row_activity_follow_multiple;
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
                final FollowActivityViewHolder rcv = new FollowActivityViewHolder(layoutView);
                rcv.setClickListener(clickListener);
                return rcv;
            }
            case VIEW_TYPE_CREATE_STREAMABLE_SINGLE:
            case VIEW_TYPE_CREATE_STREAMABLE_MULTIPLE:{
                int layoutId = viewType == VIEW_TYPE_CREATE_STREAMABLE_SINGLE ? R.layout.row_activity_create_streamable_single : R.layout.row_activity_create_streamable_multiple;
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
                final CreateStreamableActivityViewHolder rcv = new CreateStreamableActivityViewHolder(layoutView);
                rcv.setClickListener(clickListener);
                return rcv;
            }
            case VIEW_TYPE_COMMENT_SINGLE:
            case VIEW_TYPE_COMMENT_MULTIPLE: {
                int layoutId = viewType == VIEW_TYPE_COMMENT_SINGLE ? R.layout.row_activity_comment_single : R.layout.row_activity_comment_multiple;
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
                final CommentActivityViewHolder rcv = new CommentActivityViewHolder(layoutView);
                rcv.setClickListener(clickListener);
                return rcv;
            }
        }
        return null;
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ActivityContainerViewHolder viewHolder = (ActivityContainerViewHolder) holder;
        final GTActivityContainer item = getItem(position);
        viewHolder.setItem(item);
        viewHolder.topTimelineSeparator.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
        viewHolder.bottomTimelineSeparator.setVisibility(position == getItemCount() - 2 /* Account for the infinite scroll view here. */ ? View.INVISIBLE : View.VISIBLE);
    }
}
