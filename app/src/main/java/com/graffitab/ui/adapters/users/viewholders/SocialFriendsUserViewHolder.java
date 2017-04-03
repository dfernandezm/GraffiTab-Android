package com.graffitab.ui.adapters.users.viewholders;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.graffitab.R;
import com.graffitab.ui.adapters.followersactivity.RowListItemsAdapter;
import com.graffitab.ui.views.recyclerview.AdvancedRecyclerViewItemDecoration;
import com.graffitabsdk.model.GTStreamable;
import com.graffitabsdk.model.GTUser;
import com.graffitabsdk.network.common.GTRequestPerformed;
import com.graffitabsdk.network.common.params.GTQueryParameters;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.network.service.streamable.response.GTListStreamablesResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.graffitabsdk.sdk.GTSDK.getUserManager;

/**
 * Created by david on 05/04/2017.
 */

public class SocialFriendsUserViewHolder extends ListUserViewHolder {

    private GTRequestPerformed previousRequest;

    @BindView(R.id.socialFriendsStreamablesRecyclerView)
    protected RecyclerView socialFriendsStreamablesRecyclerView;

    private RowListItemsAdapter<GTStreamable> rowListItemsAdapter;

    @BindView(R.id.noPostsYet)
    public TextView noPostsYetText;

    // To test, replace the findFacebookFriends call with getAllUsers and get their posts to force
    // a full list

    private Map<Integer, GTListStreamablesResponse> userIdToResponse = new HashMap<>();

    public SocialFriendsUserViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        bindSocialFriendsDetailsRecyclerView();
    }

    // This is called from onBind, which will be done every time a row is reused during scroll
    @Override
    public void setItem(final GTUser user) {
        super.setItem(user);

        // Getting called every time a row is going to be displayed
        GTQueryParameters params = getParameters();
        OnFindPosts<GTListStreamablesResponse> onFindPosts;

        final GTListStreamablesResponse streamablesResponse = userIdToResponse.get(user.id);

        if (streamablesResponse != null) {
            refreshItems(streamablesResponse.items);
        } else {

            if (previousRequest == null) {
                onFindPosts = buildOnFindPostsCallbacks(user, null);
                previousRequest = executeFindPostsCall(user.id, params, onFindPosts);

            } else {

                String getPostsUrl = getUserManager().getPostsUrl(user.id, params);

                if (!previousRequest.getRequestUrl().equals(getPostsUrl)) {
                    previousRequest.cancel();
                    onFindPosts = buildOnFindPostsCallbacks(user, previousRequest.getRequestUrl());
                    previousRequest = executeFindPostsCall(user.id, params, onFindPosts);
                } // else, if they are the same URL, we are loading the same request again, don't do anything
            }
        }
    }

    private OnFindPosts<GTListStreamablesResponse> buildOnFindPostsCallbacks(final GTUser user, final String previousRequestUrl) {
        return new OnFindPosts<GTListStreamablesResponse>() {

            @Override
            public void executeOnSuccess(GTResponse<GTListStreamablesResponse> gtResponse) {

                if (previousRequestUrl == null) {
                    refreshItemsAndCache(user.id, gtResponse.getObject());
                } else {
                    clearItems();
                    if (gtResponse.getApiEndpointUrl().equals(previousRequestUrl)) {
                        refreshItemsAndCache(user.id, gtResponse.getObject());
                    }
                }
            }

            @Override
            public void executeOnFailure(GTResponse<GTListStreamablesResponse> gtResponse) {
                toggleNoPostsLabel(true);
            }
        };
    }

    private void refreshItemsAndCache(Integer userId, GTListStreamablesResponse streamablesResponse) {
        refreshItems(streamablesResponse.items);
        userIdToResponse.put(userId, streamablesResponse);
    }

    private GTRequestPerformed executeFindPostsCall(Integer userId, GTQueryParameters params, final OnFindPosts<GTListStreamablesResponse> onFindPosts) {
        return getUserManager().getPosts(userId, true, params,
                new GTResponseHandler<GTListStreamablesResponse>() {
                    @Override
                    public void onSuccess(GTResponse<GTListStreamablesResponse> gtResponse) {
                        onFindPosts.executeOnSuccess(gtResponse);
                    }

                    @Override
                    public void onFailure(GTResponse<GTListStreamablesResponse> gtResponse) {
                        onFindPosts.executeOnFailure(gtResponse);
                    }
                });
    }


    private void bindSocialFriendsDetailsRecyclerView() {

        socialFriendsStreamablesRecyclerView.addItemDecoration(new AdvancedRecyclerViewItemDecoration(1, 0));

        LinearLayoutManager activityDetailLinearLayoutManager = new LinearLayoutManager(itemView.getContext());
        activityDetailLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        socialFriendsStreamablesRecyclerView.setLayoutManager(activityDetailLinearLayoutManager);

        AdvancedRecyclerViewItemDecoration decoration = new AdvancedRecyclerViewItemDecoration(1, 3);
        socialFriendsStreamablesRecyclerView.addItemDecoration(decoration);

        rowListItemsAdapter = new RowListItemsAdapter<>(R.layout.row_social_friends_item_image);
        socialFriendsStreamablesRecyclerView.setAdapter(rowListItemsAdapter);
    }

    private void toggleNoPostsLabel(boolean show) {
        noPostsYetText.setVisibility((show) ? View.VISIBLE : View.GONE);
        socialFriendsStreamablesRecyclerView.setVisibility((show) ? View.GONE : View.VISIBLE);
    }

    private void refreshItems(List<GTStreamable> streamables) {
        if (streamables.size() > 0) {
            toggleNoPostsLabel(false);
            rowListItemsAdapter.setItems(streamables);
            rowListItemsAdapter.setImageLoader(new RowListItemsAdapter.ImageLoader<GTStreamable>() {
                @Override
                public void loadImage(ImageView imageView, GTStreamable activityDetailItem) {
                    Picasso.with(imageView.getContext()).load(activityDetailItem.asset.thumbnail).into(imageView);
                }
            });
            rowListItemsAdapter.notifyDataSetChanged();
        } else {
            toggleNoPostsLabel(true);
        }
    }

    private void clearItems() {
        rowListItemsAdapter.setItems(new ArrayList<GTStreamable>());
        rowListItemsAdapter.notifyDataSetChanged();
    }

    private GTQueryParameters getParameters() {
        GTQueryParameters params = new GTQueryParameters();
        params.addParameter(GTQueryParameters.GTParameterType.offset, 0);
        params.addParameter(GTQueryParameters.GTParameterType.limit, 6);
        return params;
    }

    private interface OnFindPosts<T> {
        void executeOnSuccess(GTResponse<T> gtResponse);
        void executeOnFailure(GTResponse<T> gtResponse);
    }
}
