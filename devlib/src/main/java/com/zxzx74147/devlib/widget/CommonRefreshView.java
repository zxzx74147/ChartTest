package com.zxzx74147.devlib.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dinuscxj.refresh.IRefreshStatus;
import com.zxzx74147.devlib.R;
import com.zxzx74147.devlib.utils.ViewUtil;

/**
 * Created by zhengxin on 2018/2/12.
 */

public class CommonRefreshView extends FrameLayout implements IRefreshStatus {

    private static final int ANIMATION_DURATION = 150;
    private static final Interpolator ANIMATION_INTERPOLATOR = new DecelerateInterpolator();
    private ImageView mImageView1 =  null;
    private ImageView mImageView2 = null;

    private Animation mRotateAnimation;
    private Animation operatingAnim;
    private Animation mResetRotateAnimation;

    public CommonRefreshView(Context context) {
        super(context);
        initView();
        initAnimation();
    }


    public CommonRefreshView(Context context,AttributeSet attrs) {
        super(context, attrs);
        initView();
        initAnimation();
    }

    private void initView() {
        mImageView1 = new ImageView(getContext());
        mImageView2 = new ImageView(getContext());
        FrameLayout.LayoutParams lp1 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mImageView1,lp1);
        addView(mImageView2,lp2);
        mImageView1.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mImageView2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mImageView1.setImageResource(R.drawable.loading_1);
        mImageView2.setImageResource(R.drawable.loading_2);
    }

    private void initAnimation() {
        mRotateAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
        mRotateAnimation.setDuration(ANIMATION_DURATION);
        mRotateAnimation.setFillAfter(true);
//
        mResetRotateAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mResetRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
        mResetRotateAnimation.setDuration(ANIMATION_DURATION);
        mResetRotateAnimation.setFillAfter(true);

        operatingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
    }

    @Override
    public void reset() {
        mImageView1.clearAnimation();

    }

    @Override
    public void refreshing() {
        mImageView1.clearAnimation();
//        AnimationDrawable drawable = (AnimationDrawable) getResources().getDrawable(R.drawable.spinner);
//        this.setImageDrawable(drawable);
//        drawable.start();
        if (mImageView1.getAnimation() == null || mImageView1.getAnimation() == mRotateAnimation) {
            mImageView1.startAnimation(operatingAnim);
        }
    }

    @Override
    public void refreshComplete() {
//        this.setImageResource(R.drawable.loading_none);
    }

    @Override
    public void pullToRefresh() {
        mImageView1.clearAnimation();
//        this.setImageResource(R.drawable.loading_rotate);

        if (mImageView1.getAnimation() == null|| mImageView1.getAnimation() == operatingAnim) {
            mImageView1.startAnimation(mRotateAnimation);
        }
    }


    @Override
    public void releaseToRefresh() {
        mImageView1.clearAnimation();

    }

    @Override
    public void pullProgress(float pullDistance, float pullProgress) {

    }




}
