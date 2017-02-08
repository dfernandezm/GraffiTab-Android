package com.graffitab.ui.views.autocomplete;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.MultiAutoCompleteTextView;

/**
 * Created by georgichristov on 09/12/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
class UserHashtagTokenizer implements MultiAutoCompleteTextView.Tokenizer {

    @Override
    public CharSequence terminateToken(CharSequence text) {
        int i = text.length();

        while (i > 0 && text.charAt(i - 1) == ' ') {
            i--;
        }

        if (i > 0 && text.charAt(i - 1) == ' ') {
            return text;
        } else {
            if (text instanceof Spanned) {
                SpannableString sp = new SpannableString(text + " ");
                TextUtils.copySpansFrom((Spanned) text, 0, text.length(), Object.class, sp, 0);
                return sp;
            } else {
                return text + " ";
            }
        }
    }

    @Override
    public int findTokenStart(CharSequence text, int cursor) {
        int i = cursor;

        // Go back until we find one of the special symbols.
        while (i > 0 && text.charAt(i - 1) != '#' && text.charAt(i - 1) != '@') {
            i--;
        }

        // Check if token really started with a special symbol, else we don't have a valid token.
        if (i < 1 || (text.charAt(i - 1) != '#' && text.charAt(i - 1) != '@')) {
            return cursor;
        }

        return i;
    }

    @Override
    public int findTokenEnd(CharSequence text, int cursor) {
        int i = cursor;
        int len = text.length();

        while (i < len) {
            if (text.charAt(i) == ' ') {
                return i;
            } else {
                i++;
            }
        }

        return len;
    }
}
