package com.graffitab.ui.fragments.comments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
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
import com.graffitab.utils.input.KeyboardUtils;
import com.graffitabsdk.model.GTComment;
import com.graffitabsdk.model.GTUser;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

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
    private GTComment toEdit;
    private int toEditPosition;

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
            if (toEdit != null) {
                Log.i(getClass().getSimpleName(), "Editing comment");
                toEdit.text = text;
                items.set(toEditPosition, toEdit);
            }
            else {
                Log.i(getClass().getSimpleName(), "Posting comment");
                GTComment comment = new GTComment();
                comment.text = text;
                items.add(0, comment);
            }
            toEdit = null;
            toEditPosition = -1;
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
    public void onRowSelected(GTComment comment, int adapterPosition) {
        showOptionsMenu(comment, adapterPosition);
    }

    @Override
    public void onRowLongSelected(GTComment comment, int adapterPosition) {
        showOptionsMenu(comment, adapterPosition);
    }

    @Override
    public void onOpenCommenterProfile(GTComment comment, GTUser commenter, int adapterPosition) {
        ProfileActivity.show(commenter, getActivity());
    }

    @Override
    public void onOpenHashtag(GTComment comment, String hashtag, int adapterPosition) {
        startActivity(new Intent(getActivity(), SearchActivity.class));
    }

    @Override
    public void onOpenLink(GTComment comment, String url, int adapterPosition) {
        Utils.openUrl(getActivity(), url);
    }

    @Override
    public void onOpenMention(GTComment comment, String mention, int adapterPosition) {
//        startActivity(new Intent(getActivity(), ProfileActivity.class));
    }

    private void showOptionsMenu(final GTComment comment, final int adapterPosition) {
        BottomSheet.Builder builder = new BottomSheet.Builder(getActivity(), R.style.BottomSheet_StyleDialog)
                .title(R.string.comments_menu_title)
                .sheet(R.menu.menu_comments);

        builder = builder.listener(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == R.id.action_edit) {
                    toEdit = comment;
                    toEditPosition = adapterPosition;

                    commentField.setText(comment.text);
                    KeyboardUtils.showKeyboard(getActivity(), commentField);
                }
                else if (which == R.id.action_copy) {
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Text", comment.text);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getActivity(), getString(R.string.other_copied), Toast.LENGTH_SHORT).show();
                }
                else if (which == R.id.action_remove) {
                    adapter.removeItem(adapterPosition, getRecyclerView().getRecyclerView());
                }
            }
        });
        builder.show();
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

    // Setup

    @Override
    public void setupCustomViews() {
        super.setupCustomViews();

        getRecyclerView().setBackgroundColor(Color.WHITE);
        getRecyclerView().getRefreshLayout().setDirection(SwipyRefreshLayoutDirection.BOTTOM);
    }
}
