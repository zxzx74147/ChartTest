package com.zxzx74147.devlib.utils;

import android.content.Context;
import android.widget.Toast;

import com.zxzx74147.devlib.DevLib;
import com.zxzx74147.devlib.R;

import es.dmoral.toasty.Toasty;

/**
 * Created by zhengxin on 2018/2/20.
 */

public class ToastUtil {
    static {
        Toasty.Config.getInstance()
                .setInfoColor(0xB4000000) // optional
                .setTextColor(ColorUtil.getColor(R.color.white))
                .apply(); // required
    }

    public static void showToast(Context context, String content) {
        Toasty.info(context, content, Toast.LENGTH_LONG).show();
    }

    public static void showToast(Context context, int content) {
        Toasty.info(context, DevLib.getApp().getResources().getString(content),Toast.LENGTH_LONG).show();
    }
}
