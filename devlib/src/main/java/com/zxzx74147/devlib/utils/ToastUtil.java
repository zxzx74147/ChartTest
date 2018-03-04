package com.zxzx74147.devlib.utils;

import android.content.Context;
import android.widget.Toast;

import com.zxzx74147.devlib.DevLib;

/**
 * Created by zhengxin on 2018/2/20.
 */

public class ToastUtil {
    public static void showToast(Context context,String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context,int content){
        Toast.makeText(context, DevLib.getApp().getResources().getString(content),Toast.LENGTH_SHORT).show();
    }
}
