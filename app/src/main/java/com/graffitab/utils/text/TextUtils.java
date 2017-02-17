package com.graffitab.utils.text;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitabsdk.model.GTStreamable;
import com.graffitabsdk.model.GTUser;
import com.graffitabsdk.sdk.GTSDK;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by georgichristov on 06/07/16.
 */
public class TextUtils {

    public static String buildLikersString(Context context, List<GTUser> users, GTStreamable streamable) {
        GTUser first = users.get(0);
        String text;
        if (users.size() > 1) {
            if (first.equals(GTSDK.getAccountManager().getLoggedInUser())) {
                if (streamable.likersCount - 1 == 1)
                    text = context.getString(R.string.streamable_details_likers_me_single);
                else
                    text = context.getString(R.string.streamable_details_likers_me, streamable.likersCount - 1);
            }
            else {
                if (streamable.likersCount - 1 == 1)
                    text = context.getString(R.string.streamable_details_likers_single, first.fullName());
                else
                    text = context.getString(R.string.streamable_details_likers, first.fullName(), streamable.likersCount - 1);
            }
        }
        else {
            if (first.equals(GTSDK.getAccountManager().getLoggedInUser()))
                text = context.getString(R.string.streamable_details_liker_me);
            else
                text = context.getString(R.string.streamable_details_liker, first.fullName());
        }
        return text;
    }

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
