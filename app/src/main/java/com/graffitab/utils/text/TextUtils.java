package com.graffitab.utils.text;

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
        if (textView == null) return;

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

    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}
