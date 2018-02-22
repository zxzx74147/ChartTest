package com.zxzx74147.devlib.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zhengxin on 2018/2/20.
 */

public class ToastUtil {
    public static void showToast(Context context,String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }
}
