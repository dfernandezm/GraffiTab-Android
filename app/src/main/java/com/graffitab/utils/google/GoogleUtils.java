package com.graffitab.utils.google;

/**
 * Created by georgichristov on 01/02/2017
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class GoogleUtils {

    static final String StaticMap = "http://maps.googleapis.com/maps/api/staticmap?";
    static final String StreetView = "http://maps.googleapis.com/maps/api/streetview?";

    public static String getStaticMapUrl(double latitude, double longitude) {
        return String.format("%scenter=%f,%f&zoom=16&size=600x300&maptype=roadmap&markers=color:blue|%f,%f&sensor=false", StaticMap, latitude, longitude, latitude, longitude);
    }

    public static String getStreetViewUrl(double latitude, double longitude) {
        return String.format("%ssize=600x300&location=%f,%f&sensor=false", StreetView, latitude, longitude);
    }
}
