package com.zxzx74147.devlib.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v4.app.FragmentActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;

/**
 * Created by zhengxin on 2018/2/20.
 */

public class ViewUtil {
    public static FragmentActivity  getFragmentActivity(View v){
        FragmentActivity temp = null;
        if(v.getContext() instanceof FragmentActivity){
            temp = (FragmentActivity) v.getContext();
        }else if(v.getContext() instanceof ContextThemeWrapper){
            temp = (FragmentActivity) ((ContextThemeWrapper) v.getContext()).getBaseContext();
        }else if(v.getContext() instanceof android.view.ContextThemeWrapper){
            temp = (FragmentActivity) ((android.view.ContextThemeWrapper) v.getContext()).getBaseContext();
        }
        return temp;
    }


}
