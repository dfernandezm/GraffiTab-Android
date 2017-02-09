package com.graffitab.ui.views.autocomplete;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitab.ui.adapters.BaseItemAdapter;
import com.graffitabsdk.sdk.GTSDK;
import com.graffitabsdk.constants.GTConstants;
import com.graffitabsdk.model.GTUser;
import com.graffitabsdk.network.common.params.GTQueryParameters;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.network.service.streamable.response.GTListHashtagsResponse;
import com.graffitabsdk.network.service.user.response.GTListUsersResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by georgichristov on 13/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
class AutoCompleteAdapter extends BaseItemAdapter<Object> implements Filterable {

    private List<GTUser> usersCache = new ArrayList();
    private List<String> tagsCache = new ArrayList();
    private List<Object> suggestions = new ArrayList();
    private Filter filter = new CustomFilter();
    private String searchTerm;

    public AutoCompleteAdapter(Context context) {
        super(context, null);
    }

    public void setSearchTerm(String term) {
        this.searchTerm = term;
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public Object getItem(int pos) {
        return suggestions.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object item = getItem(position);
        if (item instanceof GTUser) { // @
            View v = mInflater.inflate(R.layout.row_suggestion_user, null);
            ImageView avatar = (ImageView) v.findViewById(R.id.avatar);
            TextView nameField = (TextView) v.findViewById(R.id.nameField);
            TextView usernameField = (TextView) v.findViewById(R.id.usernameField);

            GTUser user = (GTUser) item;
            nameField.setText(user.firstName + " " + user.lastName);
            usernameField.setText(user.mentionUsername());

            loadAvatar(avatar, user);

            return v;
        }
        else { // #
            View v = mInflater.inflate(R.layout.row_suggestion_tag, null);
            TextView tagField = (TextView) v.findViewById(R.id.tagField);

            String tag = (String) item;
            tagField.setText(tag);

            return v;
        }
    }

    private void loadAvatar(ImageView avatar, GTUser user) {
        int p = R.drawable.default_avatar;
        if (user.hasAvatar())
            Picasso.with(avatar.getContext()).load(user.avatar.thumbnail).placeholder(p).error(p).into(avatar);
        else
            Picasso.with(avatar.getContext()).load(p).placeholder(p).into(avatar);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private class CustomFilter extends Filter {

        private String previousSearchTerm;

        @Override
        protected FilterResults performFiltering(final CharSequence constraint) {
            suggestions.clear();

            if (searchTerm != null && constraint != null) {
                String query = constraint.toString().toLowerCase();

                if (searchTerm.equals("@")) {
                    Log.d(AutoCompleteAdapter.this.getClass().getSimpleName(), "Search term: " + constraint + ", total items in @ cache: " + usersCache.size());
                    for (GTUser user : usersCache) {
                        if ((user.firstName + " " + user.lastName).toLowerCase().contains(query)
                                || user.username.toLowerCase().contains(query)) {
                            suggestions.add(user);
                        }
                    }

                    if (suggestions.size() <= 0 && (previousSearchTerm == null || !previousSearchTerm.equals(query))) { // No local suggestions, so fetch from server.
                        previousSearchTerm = query;
                        GTQueryParameters parameters = new GTQueryParameters();
                        parameters.addParameter(GTQueryParameters.GTParameterType.OFFSET, 0);
                        parameters.addParameter(GTQueryParameters.GTParameterType.LIMIT, GTConstants.MAX_ITEMS);
                        parameters.addParameter(GTQueryParameters.GTParameterType.QUERY, query);
                        GTSDK.getUserManager().search(parameters, new GTResponseHandler<GTListUsersResponse>() {

                            @Override
                            public void onSuccess(GTResponse<GTListUsersResponse> gtResponse) {
                                if (gtResponse.getObject().items.size() > 0) {
                                    for (GTUser user : gtResponse.getObject().items) {
                                        if (!usersCache.contains(user))
                                            usersCache.add(user);
                                    }
                                    performFiltering(constraint);
                                }
                            }

                            @Override
                            public void onFailure(GTResponse<GTListUsersResponse> gtResponse) {}
                        });
                    }
                }
                else if (searchTerm.equals("#")) {
                    Log.d(AutoCompleteAdapter.this.getClass().getSimpleName(), "Search term: " + constraint + ", total items in # cache: " + tagsCache.size());
                    for (String tag : tagsCache) {
                        if (tag.toLowerCase().contains(query)) {
                            suggestions.add(tag);
                        }
                    }

                    if (suggestions.size() <= 0 && (previousSearchTerm == null || !previousSearchTerm.equals(query))) { // No local suggestions, so fetch from server.
                        previousSearchTerm = query;
                        GTQueryParameters parameters = new GTQueryParameters();
                        parameters.addParameter(GTQueryParameters.GTParameterType.OFFSET, 0);
                        parameters.addParameter(GTQueryParameters.GTParameterType.LIMIT, GTConstants.MAX_ITEMS);
                        parameters.addParameter(GTQueryParameters.GTParameterType.QUERY, query);
                        GTSDK.getStreamableManager().searchHashtags(parameters, new GTResponseHandler<GTListHashtagsResponse>() {

                            @Override
                            public void onSuccess(GTResponse<GTListHashtagsResponse> gtResponse) {
                                if (gtResponse.getObject().items.size() > 0) {
                                    for (String hashtag : gtResponse.getObject().items) {
                                        if (!tagsCache.contains(hashtag))
                                            tagsCache.add(hashtag);
                                    }
                                    performFiltering(constraint);
                                }
                            }

                            @Override
                            public void onFailure(GTResponse<GTListHashtagsResponse> gtResponse) {}
                        });
                    }
                }

                Log.d(AutoCompleteAdapter.this.getClass().getSimpleName(), "Found " + suggestions.size());
            }
            FilterResults results = new FilterResults();
            results.values = suggestions;
            results.count = suggestions.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0)
                notifyDataSetChanged();
            else
                notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            if (resultValue instanceof GTUser)
                return ((GTUser) resultValue).username;
            return resultValue.toString();
        }
    }
}
