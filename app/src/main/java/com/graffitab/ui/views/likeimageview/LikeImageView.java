package com.graffitab.ui.views.likeimageview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.graffitab.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 16/02/2017
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class LikeImageView extends RelativeLayout {

    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();

    @BindView(R.id.circleBg) View circleBackground;
    @BindView(R.id.heart) ImageView heartImageView;
    @BindView(R.id.imageView) ImageView imageView;

    private GestureDetector detector;
    private float likeImageScale = 1;
    private OnLikeListener likeListener;

    // Constructors

    public LikeImageView(Context context) {
        super(context);
        baseInit(context);
    }

    public LikeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        baseInit(context);
    }

    public LikeImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        baseInit(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        detector = new GestureDetector(getContext(), new GestureListener());
    }

    public void loadImage(String url) {
        Picasso.with(getContext()).load(url).into(imageView);
    }

    public Drawable getDrawable() {
        return imageView.getDrawable();
    }

    public void setLikeImageScale(float likeImageScale) {
        this.likeImageScale = likeImageScale;
    }

    public void setLikeListener(OnLikeListener likeListener) {
        this.likeListener = likeListener;
    }

    public View getImageView() {
        return imageView;
    }

    // Init

    private void baseInit(Context context) {
        View.inflate(context, R.layout.view_like_image_view, this);
    }

    // Touch

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    // Animations

    public void animateLike() {
        circleBackground.setVisibility(View.VISIBLE);
        heartImageView.setVisibility(View.VISIBLE);

        circleBackground.setScaleY(0.1f);
        circleBackground.setScaleX(0.1f);
        circleBackground.setAlpha(1f);
        heartImageView.setScaleY(0.1f);
        heartImageView.setScaleX(0.1f);

        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator bgScaleYAnim = ObjectAnimator.ofFloat(circleBackground, "scaleY", 0.1f, 1f);
        bgScaleYAnim.setDuration(200);
        bgScaleYAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
        ObjectAnimator bgScaleXAnim = ObjectAnimator.ofFloat(circleBackground, "scaleX", 0.1f, 1f);
        bgScaleXAnim.setDuration(200);
        bgScaleXAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
        ObjectAnimator bgAlphaAnim = ObjectAnimator.ofFloat(circleBackground, "alpha", 1f, 0f);
        bgAlphaAnim.setDuration(200);
        bgAlphaAnim.setStartDelay(150);
        bgAlphaAnim.setInterpolator(DECCELERATE_INTERPOLATOR);

        ObjectAnimator imgScaleUpYAnim = ObjectAnimator.ofFloat(heartImageView, "scaleY", 0.1f, likeImageScale);
        imgScaleUpYAnim.setDuration(300);
        imgScaleUpYAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
        ObjectAnimator imgScaleUpXAnim = ObjectAnimator.ofFloat(heartImageView, "scaleX", 0.1f, likeImageScale);
        imgScaleUpXAnim.setDuration(300);
        imgScaleUpXAnim.setInterpolator(DECCELERATE_INTERPOLATOR);

        ObjectAnimator imgScaleDownYAnim = ObjectAnimator.ofFloat(heartImageView, "scaleY", likeImageScale, 0f);
        imgScaleDownYAnim.setDuration(300);
        imgScaleDownYAnim.setInterpolator(ACCELERATE_INTERPOLATOR);
        ObjectAnimator imgScaleDownXAnim = ObjectAnimator.ofFloat(heartImageView, "scaleX", likeImageScale, 0f);
        imgScaleDownXAnim.setDuration(300);
        imgScaleDownXAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

        animatorSet.playTogether(bgScaleYAnim, bgScaleXAnim, bgAlphaAnim, imgScaleUpYAnim, imgScaleUpXAnim);
        animatorSet.play(imgScaleDownYAnim).with(imgScaleDownXAnim).after(imgScaleUpYAnim);

        animatorSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                reset();
            }
        });
        animatorSet.start();
    }

    public void reset() {
        circleBackground.setVisibility(View.GONE);
        heartImageView.setVisibility(View.GONE);
    }

    public interface OnLikeListener {
        void onLiked();
        void onTapped();
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (likeListener != null)
                likeListener.onTapped();
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            animateLike();
            if (likeListener != null)
                likeListener.onLiked();
            return true;
        }
    }
}
