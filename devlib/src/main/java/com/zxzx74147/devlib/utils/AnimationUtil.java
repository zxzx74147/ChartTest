package com.zxzx74147.devlib.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by zhengxin on 2018/2/20.
 */

public class AnimationUtil {
    public static void  showViewAlpha(View v){
        ObjectAnimator mAnimator = ObjectAnimator.ofFloat(v,"alpha",0,1);
        mAnimator.setDuration(1000);
        v.setVisibility(View.VISIBLE);
        mAnimator.start();
    }

    public static void  hideViewAlpha(final View v){
        ObjectAnimator mAnimator = ObjectAnimator.ofFloat(v,"alpha",1,0);
        mAnimator.setDuration(1000);
        v.setVisibility(View.VISIBLE);
        mAnimator.start();
        mAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                v.setVisibility(View.GONE);
            }
        });
    }

    public static void  translationView(final View v,float x,float y){
        ObjectAnimator mAnimatorX = ObjectAnimator.ofFloat(v, "translationX", 0.0f, x);
        ObjectAnimator mAnimatorY = ObjectAnimator.ofFloat(v, "translationY", 0.0f, y);
        AnimatorSet set = new AnimatorSet();
        set.play(mAnimatorX).with(mAnimatorY);
        set.setDuration(1000);
        set.start();

    }
}
