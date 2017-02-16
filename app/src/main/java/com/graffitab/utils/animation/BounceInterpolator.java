package com.graffitab.utils.animation;

import android.view.animation.Interpolator;

/**
 * Created by georgichristov on 16/02/2017
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class BounceInterpolator implements Interpolator {

    double mAmplitude = 1;
    double mFrequency = 10;

    public BounceInterpolator(double amplitude, double frequency) {
        mAmplitude = amplitude;
        mFrequency = frequency;
    }

    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                Math.cos(mFrequency * time) + 1);
    }
}
