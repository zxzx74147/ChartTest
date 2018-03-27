package com.zxzx74147.devlib.umeng;

import com.umeng.analytics.MobclickAgent;
import com.zxzx74147.devlib.DevLib;

public class UmengAgent {


    public static void onEvent(String action){
        MobclickAgent.onEvent(DevLib.getApp(),action);
    }


}
