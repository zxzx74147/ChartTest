package com.zxzx74147.charttest;

import android.app.Application;

import com.zxzx74147.devlib.DevLib;
import com.zxzx74147.live.ModuelLive;
import com.zxzx74147.profile.ModuleProfile;
import com.zxzx74147.stock.ModuleStock;

/**
 * Created by zhengxin on 2018/2/7.
 */

public class ZXApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        DevLib.onCreate(this);
        ModuleStock.init(this);
        ModuleProfile.init(this);
        ModuelLive.init(this);
        ModuleMain.init(this);
    }
}
