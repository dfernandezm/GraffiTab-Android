package com.graffitab.utils;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by georgichristov on 06/07/16.
 */
public class TextUtils {

    public static void colorTextViewSubstring(TextView textView, String substring, int color) {
        String notes = textView.getText().toString();
        SpannableStringBuilder sb = new SpannableStringBuilder(notes);
        Pattern p = Pattern.compile(substring, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(notes);
        while (m.find()){
            //String word = m.group();
            //String word1 = notes.substring(m.start(), m.end());

            sb.setSpan(new ForegroundColorSpan(color), m.start(), m.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        textView.setText(sb);
    }
}
