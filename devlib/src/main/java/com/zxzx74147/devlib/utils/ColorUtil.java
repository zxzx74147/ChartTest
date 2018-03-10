package com.zxzx74147.devlib.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.zxzx74147.devlib.DevLib;

/**
 * Created by zhengxin on 2018/2/20.
 */

public class ColorUtil {
    public static int  getColor(int id){
        return DevLib.getApp().getResources().getColor(id);
    }

}
