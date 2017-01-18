package com.graffitab.utils.date;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by georgichristov on 18/01/2017
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class DateUtils {

    public static String oneLetterTimePassedSinceDate(Date date) {
        SimpleDateFormat formatLetterDay = new SimpleDateFormat("EEEEE", Locale.getDefault());
        return formatLetterDay.format(date);
    }
}
