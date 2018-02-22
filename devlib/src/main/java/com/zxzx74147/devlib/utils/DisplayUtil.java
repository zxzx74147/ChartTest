package com.zxzx74147.devlib.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Size;

/**
 * Created by zhengxin on 2018/2/20.
 */

public class DisplayUtil {
    private static DisplayMetrics mDm = null;
    public static void init(Context context){
        mDm =context.getResources().getDisplayMetrics();
    }

    public static DisplayMetrics getDisplayMetrics(){
        return mDm;
    }
}
