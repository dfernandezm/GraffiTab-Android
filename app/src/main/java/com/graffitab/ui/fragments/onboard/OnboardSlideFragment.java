package com.graffitab.ui.fragments.onboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.graffitab.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 17/02/2017
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class OnboardSlideFragment extends Fragment {

    @BindView(R.id.image) ImageView imageView;
    @BindView(R.id.title) TextView titleView;
    @BindView(R.id.subtitle) TextView subtitleView;

    private String title;
    private String subtitle;
    private int imageResId;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboard_slide, container, false);
        ButterKnife.bind(this, view);

        loadSlideData();

        return view;
    }

    // Loading

    private void loadSlideData() {
        imageView.setImageResource(imageResId);
        titleView.setText(title);
        subtitleView.setText(subtitle);
    }
}
