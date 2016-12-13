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
            String character;
            if (start < charSequence.length())
                character = charSequence.toString().charAt(start) + "";
            else if (start > 0) {
                character = charSequence.toString().charAt(start - 1) + "";
            }
            else {
                character = "";
            }
            if (character.startsWith("@") || character.startsWith("#"))
                adapter.setSearchTerm(character);
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    }
}
