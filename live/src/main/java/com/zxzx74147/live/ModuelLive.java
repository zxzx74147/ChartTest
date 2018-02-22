package com.zxzx74147.live;

import android.app.Application;

import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.live.data.Live;

/**
 * Created by zhengxin on 2018/2/7.
 */

public class ModuelLive {
    private static Application mApp = null;
    private static ModuelLive mModule = null;

    public static void init(Application application) {
        mApp = application;
        mModule = new ModuelLive();
    }

    static {
        CommonMultiTypeDelegate.registDefaultViewType(Live.class, R.layout.item_live);
    }
}
