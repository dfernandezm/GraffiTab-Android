package com.graffitab.ui.fragments.comments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.graffitab.R;
import com.graffitab.application.MyApplication;
import com.graffitab.ui.activities.home.SearchActivity;
import com.graffitab.ui.activities.home.users.ProfileActivity;
import com.graffitab.ui.adapters.comments.GenericCommentsRecyclerViewAdapter;
import com.graffitab.ui.adapters.comments.OnCommentClickListener;
import com.graffitab.ui.fragments.GenericItemListFragment;
import com.graffitab.ui.views.autocomplete.UserTagMultiAutoCompleteTextView;
import com.graffitab.ui.views.recyclerview.components.AdvancedEndlessRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecyclerViewItemDecoration;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecyclerViewLayoutConfiguration;
import com.graffitab.utils.Utils;
import com.graffitabsdk.model.GTComment;
import com.graffitabsdk.model.GTUser;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by georgichristov on 09/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public abstract class GenericCommentsFragment extends GenericItemListFragment<GTComment> implements OnCommentClickListener {

    public enum ViewType {LIST_FULL}

    @BindView(R.id.messageField) UserTagMultiAutoCompleteTextView commentField;

    private ViewType viewType;

    public void basicInit() {
        setViewType(ViewType.LIST_FULL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_refreshable_recyclerview_comments, container, false);
        ButterKnife.bind(this, view);

        setupRecyclerView();

        return view;
    }

    @OnClick(R.id.sendBtn)
    public void onClickSend(View view) {
        String text = commentField.getText().toString().trim();
        if (text.length() > 0) {
            Log.i(getClass().getSimpleName(), "Posting comment");
            GTComment comment = new GTComment();
            comment.text = text;
            items.add(0, comment);
            adapter.setItems(items, getRecyclerView().getRecyclerView());
            commentField.setText("");
        }
    }

    @Override
    public int emptyViewImageResource() {
        return -1;
    }

    @Override
    public String emptyViewTitle() {
        return getString(R.string.other_empty_no_comments);
    }

    @Override
    public String emptyViewSubtitle() {
        return getString(R.string.other_empty_no_comments_description);
    }

    public void setViewType(ViewType type) {
        this.viewType = type;
    }

    public void switchViewType(ViewType type) {
        setViewType(type);

        ((GenericCommentsRecyclerViewAdapter) adapter).setViewType(viewType);
        configureLayout();
    }

    @Override
    public void onRowSelected(GTComment comment) {
        System.out.println("SELECTED " + comment);
    }

    @Override
    public void onOpenCommenterProfile(GTComment comment, GTUser commenter) {
        startActivity(new Intent(getActivity(), ProfileActivity.class));
    }

    @Override
    public void onOpenHashtag(GTComment comment, String hashtag) {
        startActivity(new Intent(getActivity(), SearchActivity.class));
    }

    @Override
    public void onOpenLink(GTComment comment, String url) {
        Utils.openUrl(getActivity(), url);
    }

    @Override
    public void onOpenMention(GTComment comment, String mention) {
        startActivity(new Intent(getActivity(), ProfileActivity.class));
    }

    // Configuration

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        if (viewType == ViewType.LIST_FULL)
            return new AdvancedRecyclerViewItemDecoration(1, 0);
        return null;
    }

    @Override
    public AdvancedEndlessRecyclerViewAdapter getAdapterForViewType() {
        GenericCommentsRecyclerViewAdapter customAdapter = new GenericCommentsRecyclerViewAdapter(MyApplication.getInstance(), items, getRecyclerView().getRecyclerView());
        customAdapter.setViewType(viewType);
        customAdapter.setClickListener(this);
        return customAdapter;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManagerForViewType() {
        if (viewType == ViewType.LIST_FULL)
            return new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        return null;
    }

    @Override
    public AdvancedRecyclerViewLayoutConfiguration getLayoutConfiguration() {
        return null;
    }

    // Loading

    @Override
    public List<GTComment> generateDummyData() {
        List<GTComment> loaded = new ArrayList();
        for (int i = 0; i < 25; i++) {
            GTComment comment = new GTComment();
            comment.text = "This is some #random #text to @georgi. Check out this link - https://www.graffitab.com.";
            loaded.add(comment);
        }
        return loaded;
    }

    // Setup

    @Override
    public void setupCustomViews() {
        super.setupCustomViews();

        getRecyclerView().setBackgroundColor(Color.WHITE);
        getRecyclerView().getRefreshLayout().setDirection(SwipyRefreshLayoutDirection.BOTTOM);
    }
}
