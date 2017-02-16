package com.graffitab.ui.fragments.comments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
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
import com.graffitab.model.GTCommentExtension;
import com.graffitab.ui.activities.home.SearchActivity;
import com.graffitab.ui.activities.home.users.ProfileActivity;
import com.graffitab.ui.adapters.comments.GenericCommentsRecyclerViewAdapter;
import com.graffitab.ui.adapters.comments.OnCommentClickListener;
import com.graffitab.ui.dialog.DialogBuilder;
import com.graffitab.ui.dialog.OnYesNoHandler;
import com.graffitab.ui.dialog.TaskDialog;
import com.graffitab.ui.fragments.GenericItemListFragment;
import com.graffitab.ui.views.autocomplete.UserHashtagMultiAutoCompleteTextView;
import com.graffitab.ui.views.recyclerview.AdvancedEndlessRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.AdvancedRecyclerViewItemDecoration;
import com.graffitab.ui.views.recyclerview.AdvancedRecyclerViewLayoutConfiguration;
import com.graffitab.utils.Utils;
import com.graffitab.utils.api.ApiUtils;
import com.graffitab.utils.input.KeyboardUtils;
import com.graffitabsdk.constants.GTConstants;
import com.graffitabsdk.model.GTComment;
import com.graffitabsdk.model.GTStreamable;
import com.graffitabsdk.model.GTUser;
import com.graffitabsdk.network.common.params.GTQueryParameters;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.network.common.result.GTActionCompleteResult;
import com.graffitabsdk.network.service.streamable.response.GTCommentResponse;
import com.graffitabsdk.network.service.user.response.GTUserResponse;
import com.graffitabsdk.sdk.GTSDK;
import com.graffitabsdk.sdk.events.users.GTUserAvatarUpdatedEvent;
import com.graffitabsdk.sdk.events.users.GTUserProfileUpdatedEvent;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by georgichristov on 09/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class GenericCommentsFragment extends GenericItemListFragment<GTComment> implements OnCommentClickListener {

    public enum ViewType {LIST_FULL}

    @BindView(R.id.messageField) UserHashtagMultiAutoCompleteTextView commentField;

    private GTStreamable streamable;
    private ViewType viewType;
    private GTComment toEdit;
    private int toEditPosition;
    private Object eventListener;

    @Override
    public void basicInit() {
        super.basicInit();

        eventListener = new Object() {

            @Subscribe
            public void userProfileUpdatedEvent(GTUserProfileUpdatedEvent event) {
                if (getRecyclerView() == null || getRecyclerView().getRecyclerView() == null) return;
                handleUserChanged(event.getUser());
            }

            @Subscribe
            public void userAvatarUpdatedEvent(GTUserAvatarUpdatedEvent event) {
                if (getRecyclerView() == null || getRecyclerView().getRecyclerView() == null) return;
                handleUserChanged(GTSDK.getAccountManager().getLoggedInUser());
            }

            private void handleUserChanged(GTUser user) {
                List<Integer> positions = new ArrayList<>();
                for (int position = 0; position < items.size(); position++) {
                    GTComment comment = items.get(position);
                    if (comment.user.equals(user)) {
                        comment.user = user;
                        positions.add(position);
                    }
                }
                adapter.setItems(items, positions, getRecyclerView().getRecyclerView());
            }
        };
        GTSDK.registerEventListener(eventListener);
    }

    @Override
    public void onDestroyView() {
        if (eventListener != null)
            GTSDK.unregisterEventListener(eventListener);
        super.onDestroyView();
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
                // Edit local comment.
                toEdit.text = text;
                toEdit.updatedOn = new Date();
                items.set(toEditPosition, toEdit);

                // Send comment to server.
                editComment(toEdit);
            }
            else {
                Log.i(getClass().getSimpleName(), "Posting comment");
                // Add local comment.
                GTCommentExtension comment = new GTCommentExtension();
                comment.text = text;
                comment.createdOn = new Date();
                comment.id = new Random().nextInt();
                comment.user = GTSDK.getAccountManager().getLoggedInUser();
                comment.setState(GTCommentExtension.State.SENDING);
                items.add(0, comment);

                // Send comment to server.
                postComment(comment);
            }
            toEdit = null;
            toEditPosition = -1;
            adapter.replaceItems(items, getRecyclerView().getRecyclerView());
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

    public void setStreamable(GTStreamable streamable) {
        this.streamable = streamable;
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
        ProfileActivity.show(getActivity(), commenter);
    }

    @Override
    public void onOpenHashtag(GTComment comment, String hashtag, int adapterPosition) {
        SearchActivity.openSearch(getActivity(), hashtag);
    }

    @Override
    public void onOpenLink(GTComment comment, String url, int adapterPosition) {
        Utils.openUrl(getActivity(), url);
    }

    @Override
    public void onOpenMention(GTComment comment, String mention, int adapterPosition) {
        // Check if the user profile exists.
        TaskDialog.getInstance().showProcessingDialog(getActivity());
        GTSDK.getUserManager().getFullUserProfileForUsername(mention.replace("@", ""), false, new GTResponseHandler<GTUserResponse>() {

            @Override
            public void onSuccess(GTResponse<GTUserResponse> gtResponse) {
                if (getActivity() == null) return;
                TaskDialog.getInstance().hideDialog();
                ProfileActivity.show(getActivity(), gtResponse.getObject().user);
            }

            @Override
            public void onFailure(GTResponse<GTUserResponse> gtResponse) {
                if (getActivity() == null) return;
                TaskDialog.getInstance().hideDialog();
                DialogBuilder.buildAPIErrorDialog(getActivity(), getString(R.string.app_name), ApiUtils.localizedErrorReason(gtResponse), true, gtResponse.getResultCode());
            }
        });
    }

    @Override
    public void onErrorSelected(final GTComment comment, final int adapterPosition) {
        KeyboardUtils.hideKeyboard(getActivity());
        BottomSheet.Builder builder = new BottomSheet.Builder(getActivity(), R.style.BottomSheet_StyleDialog)
                .title(R.string.comments_error_title)
                .sheet(R.menu.menu_comments_error);
        builder = builder.listener(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == R.id.action_retry)
                    postComment((GTCommentExtension) comment);
                else if (which == R.id.action_remove)
                    deleteComment(comment, adapterPosition, false);
            }
        });
        builder.show();
    }

    private void showOptionsMenu(final GTComment comment, final int adapterPosition) {
        BottomSheet.Builder builder = new BottomSheet.Builder(getActivity(), R.style.BottomSheet_StyleDialog)
                .title(R.string.comments_menu_title)
                .sheet(comment.user.isMe() ? R.menu.menu_comments_mine : R.menu.menu_comments);

        builder = builder.listener(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == R.id.action_edit) {
                    toEdit = comment;
                    toEditPosition = adapterPosition;

                    commentField.setText(comment.text);
                    commentField.setSelection(commentField.getText().length());
                    KeyboardUtils.toggleKeyboard(getActivity(), commentField);
                }
                else if (which == R.id.action_copy) {
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Text", comment.text);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getActivity(), getString(R.string.other_copied), Toast.LENGTH_SHORT).show();
                }
                else if (which == R.id.action_remove) {
                    DialogBuilder.buildYesNoDialog(getContext(), getString(R.string.app_name), getString(R.string.other_confirm_delete), getString(R.string.other_delete), getString(R.string.other_cancel), new OnYesNoHandler() {

                        @Override
                        public void onClickYes() {
                            deleteComment(comment, adapterPosition, true);
                        }

                        @Override
                        public void onClickNo() {}
                    });
                }
            }
        });
        builder.show();
    }

    // Comments

    private void deleteComment(final GTComment comment, final int adapterPosition, boolean shouldDeleteRemotely) {
        removeItemAtIndex(adapterPosition);
        if (shouldDeleteRemotely) {
            // TODO: Handle more intelligently.
            GTSDK.getStreamableManager().deleteComment(streamable.id, comment.id, new GTResponseHandler<GTActionCompleteResult>() {

                @Override
                public void onSuccess(GTResponse<GTActionCompleteResult> gtResponse) {
                    Log.i(getClass().getSimpleName(), "Comment deleted");
                }

                @Override
                public void onFailure(GTResponse<GTActionCompleteResult> gtResponse) {
                    Log.i(getClass().getSimpleName(), "Could not delete comment");
                }
            });
        }
    }

    private void editComment(final GTComment comment) {
        // TODO: Handle more intelligently.
        GTSDK.getStreamableManager().editComment(streamable.id, comment.id, comment.text, new GTResponseHandler<GTCommentResponse>() {

            @Override
            public void onSuccess(GTResponse<GTCommentResponse> gtResponse) {
                Log.i(getClass().getSimpleName(), "Comment edited");
            }

            @Override
            public void onFailure(GTResponse<GTCommentResponse> gtResponse) {
                Log.i(getClass().getSimpleName(), "Could not edit comment");
            }
        });
    }

    private void postComment(final GTCommentExtension comment) {
        // Reset adapter state.
        comment.setState(GTCommentExtension.State.SENDING);
        adapter.notifyDataSetChanged();

        // TODO: Handle more intelligently.
        GTSDK.getStreamableManager().postComment(streamable.id, comment.text, new GTResponseHandler<GTCommentResponse>() {

            @Override
            public void onSuccess(GTResponse<GTCommentResponse> gtResponse) {
                Log.i(getClass().getSimpleName(), "Comment posted");
                if (getActivity() == null) return;

                comment.setState(GTCommentExtension.State.SENT);
                comment.id = gtResponse.getObject().comment.id;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(GTResponse<GTCommentResponse> gtResponse) {
                Log.i(getClass().getSimpleName(), "Could not post comment");
                if (getActivity() == null) return;

                comment.setState(GTCommentExtension.State.FAILED);
                adapter.notifyDataSetChanged();
            }
        });
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
    public void loadItems(boolean isFirstLoad, int offset, GTResponseHandler handler) {
        GTQueryParameters parameters = new GTQueryParameters();
        parameters.addParameter(GTQueryParameters.GTParameterType.OFFSET, offset);
        parameters.addParameter(GTQueryParameters.GTParameterType.LIMIT, GTConstants.MAX_ITEMS);
        GTSDK.getStreamableManager().getComments(streamable.id, isFirstLoad, parameters, handler);
    }

    // Setup

    @Override
    public void setupCustomViews() {
        super.setupCustomViews();

        getRecyclerView().setBackgroundColor(Color.WHITE);
        getRecyclerView().getRefreshLayout().setDirection(SwipyRefreshLayoutDirection.BOTTOM);
    }
}
