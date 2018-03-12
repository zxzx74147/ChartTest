package com.zxzx74147.devlib.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.zxzx74147.devlib.data.UniApiData;

/**
 * Created by zhengxin on 2018/2/20.
 */

public class RspDealUtil {
    public static boolean deanRsp(Context context, UniApiData uniApiData){
        if(uniApiData.hasError()){
            ToastUtil.showToast(context,uniApiData.error.usermsg);
            return true;
        }
        return false;
    }

    public static boolean deanRsp(Context context,UniApiData uniApiData,boolean showToast){
        if(uniApiData.hasError()){
            if(showToast) {
                ToastUtil.showToast(context, uniApiData.error.usermsg);
            }
            return true;
        }
        return false;
    }
}
