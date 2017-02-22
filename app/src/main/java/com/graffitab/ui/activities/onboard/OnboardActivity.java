package com.graffitab.ui.activities.onboard;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.graffitab.R;
import com.graffitab.ui.adapters.viewpagers.ViewPagerTabAdapter;
import com.graffitab.ui.fragments.onboard.OnboardSlideFragment;
import com.graffitab.utils.activity.ActivityUtils;
import com.graffitab.utils.animation.BounceInterpolator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by georgichristov on 17/02/2017
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class OnboardActivity extends AppCompatActivity {

    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.indicator) CircleIndicator circleIndicator;
    @BindView(R.id.goBtn) Button goBtn;

    private ViewPagerTabAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUtils.enableFullScreen(this);
        ActivityUtils.hideActionBar(this);
        ActivityUtils.setOrientation(this);

        setContentView(R.layout.activity_onboard);
        ButterKnife.bind(this);

        setupBackgroundImage();
        setupViewPager();
        setupGoButton();
    }

    @Override
    public void onBackPressed() {
        // No-op because we would like to close this form the Go button instead.
    }

    @OnClick(R.id.goBtn)
    public void onClickGo(View view) {
        setResult(RESULT_OK);
        finish();
    }

    private void startAnimatingGoButton() {
        goBtn.setVisibility(View.VISIBLE);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        BounceInterpolator interpolator = new BounceInterpolator(0.1, 20);
        myAnim.setInterpolator(interpolator);
        goBtn.startAnimation(myAnim);

        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(goBtn,
                PropertyValuesHolder.ofFloat("scaleX", 1.1f),
                PropertyValuesHolder.ofFloat("scaleY", 1.1f));
        scaleDown.setDuration(1000);
        scaleDown.setInterpolator(new FastOutSlowInInterpolator());
        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        scaleDown.setStartDelay(2000);
        scaleDown.start();
    }

    // Setup

    private void setupBackgroundImage() {
        ActivityUtils.setupBackgroundImage(this, R.drawable.login_full, R.id.background);
    }

    private void setupViewPager() {
        adapter = new ViewPagerTabAdapter(getSupportFragmentManager());
        adapter.addFragment(setupSlide(getString(R.string.onboard_screen_1_title), getString(R.string.onboard_screen_1_description), R.drawable.onboard_1), "");
        adapter.addFragment(setupSlide(getString(R.string.onboard_screen_2_title), getString(R.string.onboard_screen_2_description), R.drawable.onboard_2), "");
        adapter.addFragment(setupSlide(getString(R.string.onboard_screen_3_title), getString(R.string.onboard_screen_3_description), R.drawable.onboard_3), "");
        adapter.addFragment(setupSlide(getString(R.string.onboard_screen_4_title), getString(R.string.onboard_screen_4_description), R.drawable.onboard_4), "");
        adapter.addFragment(setupSlide(getString(R.string.onboard_screen_5_title), getString(R.string.onboard_screen_5_description), R.drawable.onboard_5), "");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());
        viewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageScrollStateChanged(int state) {}

            @Override
            public void onPageSelected(int position) {
                if (position >= adapter.getCount() - 1 && goBtn.getVisibility() != View.VISIBLE)
                    startAnimatingGoButton();
            }
        });

        circleIndicator.setViewPager(viewPager);
    }

    private void setupGoButton() {
        goBtn.setVisibility(View.INVISIBLE);
    }

    private OnboardSlideFragment setupSlide(String title, String subtitle, int imageResId) {
        OnboardSlideFragment fragment = new OnboardSlideFragment();
        fragment.setImageResId(imageResId);
        fragment.setSubtitle(subtitle);
        fragment.setTitle(title);
        return fragment;
    }
}
