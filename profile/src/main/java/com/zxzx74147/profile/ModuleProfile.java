package com.zxzx74147.profile;

import android.app.Application;

import com.zxzx74147.devlib.data.MessageEvent;
import com.zxzx74147.devlib.modules.busstation.ProfileBusStation;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;
import com.zxzx74147.profile.activity.LoginPhoneActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by zhengxin on 2018/2/8.
 */

public class ModuleProfile {

    private static Application mApp = null;
    private static ModuleProfile mModule = null;

    public static void init(Application application) {
        mApp = application;
        mModule = new ModuleProfile();
    }

    private ModuleProfile() {
        EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.id == ProfileBusStation.BUS_ID_PROFILE_LOGIN) {
            ZXActivityJumpHelper.startActivity(event.context, LoginPhoneActivity.class);
        }
    }

    static {
    }

}
