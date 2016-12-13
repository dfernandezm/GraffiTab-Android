package com.graffitab.ui.views.autocomplete;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.MultiAutoCompleteTextView;

import com.graffitab.ui.views.autocomplete.components.AutoCompleteAdapter;
import com.graffitab.ui.views.autocomplete.components.UserTagTokenizer;

import butterknife.ButterKnife;

/**
 * Created by georgichristov on 13/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class UserTagMultiAutoCompleteTextView extends MultiAutoCompleteTextView {

    private AutoCompleteAdapter adapter;

    public UserTagMultiAutoCompleteTextView(Context context) {
        super(context);
        baseInit(context);
    }

    public UserTagMultiAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        baseInit(context);
    }

    public UserTagMultiAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
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
        adapter = new AutoCompleteAdapter(getContext());

        setTokenizer(new UserTagTokenizer());
        setThreshold(1);
        setAdapter(adapter);
        addTextChangedListener(new UserTagWatcher());
    }

    private class UserTagWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
            if (start >= charSequence.length())
                start = start - 1;

            String token = null;
            while (start >= 0) {
                String character = charSequence.toString().charAt(start) + "";
                if (character.startsWith("@") || character.startsWith("#")) { // We have a token.
                    token = character;
                    break;
                }
                start--;
            }
            if (token != null) // Found a token, so update the adapter.
                adapter.setSearchTerm(token);
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    }
}
