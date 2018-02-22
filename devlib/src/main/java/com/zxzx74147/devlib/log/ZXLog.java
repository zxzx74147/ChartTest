package com.zxzx74147.devlib.log;

import android.util.Log;

/**
 * Created by zhengxin on 16/8/21.
 */

public class ZXLog {

    public static void i(String tag, String message) {

        Log.i(tag, message);

    }

    public static void e(String tag, String message) {

        Log.e(tag, message);

    }
}
