package com.graffitab.ui.views.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitab.ui.views.recyclerview.components.AdvancedEndlessRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecycleView;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 18/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class AdvancedRecyclerView extends RelativeLayout {

    @BindView(R.id.refreshLayout) SwipeRefreshLayout refreshView;
    @BindView(R.id.recyclerView)
    AdvancedRecycleView recyclerView;

    @BindView(R.id.emptyView) View emptyView;
    @BindView(R.id.emptyImage) ImageView emptyImage;
    @BindView(R.id.emptyTitle) TextView emptyTitle;
    @BindView(R.id.emptyDescription) TextView emptyDescription;

    private AdvancedRecyclerViewAdapter adapter;
    private OnEmptyViewListener emptyListener;

    // Constructors

    public AdvancedRecyclerView(Context context) {
        super(context);
        baseInit(context);
    }

    public AdvancedRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        baseInit(context);
    }

    public AdvancedRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        baseInit(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    // Init

    private void baseInit(Context context) {
        View.inflate(context, R.layout.view_advanced_recycler, this);
    }

    // Methods

    public void setAdapter(AdvancedRecyclerViewAdapter adapter) {
        this.adapter = adapter;
        recyclerView.setAdapter(adapter);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void beginRefreshing() {
        refreshView.setRefreshing(true);
    }

    public void endRefreshing() {
        refreshView.setRefreshing(false);
    }

    public void setRefreshColorScheme(@NonNull int... colors) {
        refreshView.setColorSchemeResources(colors);
    }

    public void addOnLoadMoreListener(OnLoadMoreListener listener) {
        if (adapter instanceof AdvancedEndlessRecyclerViewAdapter)
            ((AdvancedEndlessRecyclerViewAdapter) adapter).addOnLoadMoreListener(listener, recyclerView);
        else
            throw new RuntimeException("Adapter does not support endless listeners.");
    }

    public void addOnEmptyViewListener(OnEmptyViewListener listener) {
        this.emptyListener = listener;

        if (listener == null) {
            recyclerView.setEmptyView(null, null);
            emptyView.setVisibility(GONE);
        }
        else {
            recyclerView.setEmptyView(emptyView, new AdvancedRecycleView.OnEmptyViewRecyclerListener() {

                @Override
                public void willShowEmptyView() {
                    int imageResId = emptyListener.emptyViewImageResource();
                    String title = emptyListener.emptyViewTitle();
                    String description = emptyListener.emptyViewSubtitle();

                    emptyImage.setVisibility(imageResId >= 0 ? View.VISIBLE : View.GONE);
                    emptyTitle.setVisibility(title.length() > 0 ? View.VISIBLE : View.GONE);
                    emptyDescription.setVisibility(description.length() > 0 ? View.VISIBLE : View.GONE);

                    if (imageResId >= 0)
                        emptyImage.setImageResource(imageResId);
                    if (title.length() > 0)
                        emptyTitle.setText(title);
                    if (description.length() > 0)
                        emptyDescription.setText(description);
                }
            });
        }
    }

    public void addOnRefreshListener(final OnRefreshListener listener) {
        refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                listener.onRefresh();
            }
        });
    }

    // Interfaces

    public interface OnRefreshListener {
        void onRefresh();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface OnEmptyViewListener {
        int emptyViewImageResource();
        String emptyViewTitle();
        String emptyViewSubtitle();
    }
}
