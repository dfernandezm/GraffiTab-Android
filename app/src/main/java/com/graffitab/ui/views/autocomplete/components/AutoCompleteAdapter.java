package com.graffitab.ui.views.autocomplete.components;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitab.ui.adapters.BaseItemAdapter;
import com.graffitabsdk.model.GTUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by georgichristov on 13/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class AutoCompleteAdapter extends BaseItemAdapter<Object> implements Filterable {

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
            TextView nameField = (TextView) v.findViewById(R.id.nameField);
            TextView usernameField = (TextView) v.findViewById(R.id.usernameField);

            GTUser user = (GTUser) item;
            nameField.setText(user.firstName + " " + user.lastName);
            usernameField.setText(user.username);

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

    @Override
    public Filter getFilter() {
        return filter;
    }

    private class CustomFilter extends Filter {

        private List<GTUser> generateUserList() {
            if (!usersCache.isEmpty())
                return new ArrayList<>();
            List<GTUser> suggestions = new ArrayList();
            GTUser user = new GTUser();
            user.firstName = "Georgi";
            user.lastName = "Christov";
            user.username = "georgi";
            suggestions.add(user);
            user = new GTUser();
            user.firstName = "Lussi";
            user.lastName = "Hristova";
            user.username = "lussi";
            suggestions.add(user);
            user = new GTUser();
            user.firstName = "Teba";
            user.lastName = "Rios";
            user.username = "teba";
            suggestions.add(user);
            user = new GTUser();
            user.firstName = "Test";
            user.lastName = "Test";
            user.username = "test";
            suggestions.add(user);
            user = new GTUser();
            user.firstName = "Test 2";
            user.lastName = "Test 2";
            user.username = "test2";
            suggestions.add(user);
            return suggestions;
        }

        private List<String> generateTagList() {
            if (!tagsCache.isEmpty())
                return new ArrayList<>();
            List<String> suggestions = new ArrayList();
            suggestions.add("hashtag1");
            suggestions.add("hashtag2");
            suggestions.add("hashtag23");
            suggestions.add("hashtag23423");
            suggestions.add("tree");
            suggestions.add("test");
            suggestions.add("nature");
            suggestions.add("truth");
            return suggestions;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            suggestions.clear();

            if (searchTerm != null && constraint != null) {
                String query = constraint.toString().toLowerCase();

                if (searchTerm.equals("@")) {
                    Log.d(AutoCompleteAdapter.this.getClass().getSimpleName(), "Search term: " + constraint + ", total items in @ cache: " + usersCache.size());
                    for (GTUser user : usersCache) {
                        if ((user.firstName + " " + user.lastName).toLowerCase().startsWith(query)
                                || user.username.toLowerCase().startsWith(query)) {
                            suggestions.add(user);
                        }
                    }

                    if (suggestions.size() <= 0) {
                        // TODO: Fetch suggestions from API and refresh adapter.
                        List<GTUser> loadedItems = generateUserList();
                        if (loadedItems.size() > 0) {
                            usersCache.addAll(loadedItems);
                            performFiltering(constraint);
                        }
                    }
                }
                else if (searchTerm.equals("#")) {
                    Log.d(AutoCompleteAdapter.this.getClass().getSimpleName(), "Search term: " + constraint + ", total items in # cache: " + tagsCache.size());
                    for (String tag : tagsCache) {
                        if (tag.toLowerCase().startsWith(query)) {
                            suggestions.add(tag);
                        }
                    }

                    if (suggestions.size() <= 0) {
                        // TODO: Fetch suggestions from API and refresh adapter.
                        List<String> loadedItems = generateTagList();
                        if (loadedItems.size() > 0) {
                            tagsCache.addAll(loadedItems);
                            performFiltering(constraint);
                        }
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
