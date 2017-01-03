package com.graffitab.ui.activities.home.streamables;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.graffitab.R;
import com.graffitab.utils.activity.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by georgichristov on 03/01/2017
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class StreamableDetailsActivity extends AppCompatActivity {

    @BindView(R.id.streamableView) ImageView streamableView;

    private PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUtils.hideActionBar(this);
        setContentView(R.layout.activity_streamable_details);
        ButterKnife.bind(this);

        setupDummyContent();
    }

    @OnClick(R.id.close)
    public void onClickClose(View view) {
        finish();
    }

    // Setup

    private void setupDummyContent() {
        Drawable bitmap = getResources().getDrawable(R.drawable.login);
        streamableView.setImageDrawable(bitmap);

        mAttacher = new PhotoViewAttacher(streamableView);
    }
}
