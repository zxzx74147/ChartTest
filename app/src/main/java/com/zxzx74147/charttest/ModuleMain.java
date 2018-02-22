package com.zxzx74147.charttest;

import android.app.Application;

import com.zxzx74147.devlib.data.MessageEvent;
import com.zxzx74147.devlib.modules.busstation.MainBusStation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by zhengxin on 2018/2/8.
 */

public class ModuleMain {

    private static Application mApp = null;
    private static ModuleMain mModule = null;

    public static void init(Application application) {
        mApp = application;
        mModule = new ModuleMain();
    }

    private ModuleMain() {
        EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.id == MainBusStation.BUS_ID_MAIN_START_MAIN) {
            MainActivity.startMainActivity(event.context);
        }
    }

}
