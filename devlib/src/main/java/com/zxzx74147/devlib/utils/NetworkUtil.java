package com.zxzx74147.devlib.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by zhengxin on 2018/2/20.
 */

public class NetworkUtil {

    public static  MultipartBody.Part createPartFromBytes(byte[] data){
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", "file", RequestBody.create(MediaType.parse("image/jpeg"),data));
        return body;
    }

}
