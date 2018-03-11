package com.zxzx74147.charttest;

import android.app.Activity;
import android.app.Application;

import com.zxzx74147.devlib.DevLib;
import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.data.MessageEvent;
import com.zxzx74147.devlib.data.SysInitData;
import com.zxzx74147.devlib.modules.account.AccountManager;
import com.zxzx74147.devlib.modules.busstation.LiveBusStation;
import com.zxzx74147.devlib.modules.busstation.MainBusStation;
import com.zxzx74147.devlib.modules.sys.SysInitManager;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.os.DeviceIDMananger;
import com.zxzx74147.devlib.os.PackageInfoMananger;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;
import com.zxzx74147.live.layout.LiveActivity;
import com.zxzx74147.profile.storage.SysStorage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;

import io.reactivex.Observable;

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
//            MainActivity.startMainActivity(event.context);

            MainFeedActivity.startMainActivity(event.context);
        }else if (event.id == MainBusStation.BUS_ID_MAIN_LOGOUT) {
            AccountManager.sharedInstance().logout();
            ZXActivityJumpHelper.startActivity(event.context,LauncherActivity.class);
            ((BaseActivity)event.context).finish();
        }else  if (event.id == LiveBusStation.BUS_ID_LIVE_VIEW){
            ZXActivityJumpHelper.startActivity(event.context, LiveActivity.class,new IntentData((Serializable) event.data));

        }else  if (event.id == MainBusStation.BUS_ID_PUSH_SUCC){
            updateToken((String) event.data);
        }else  if (event.id == MainBusStation.BUS_ID_MAIN_TO_LIVE){
            MainActivity.startMainActivity(event.context);
        }


    }


    private void updateToken(String token){
        SysStorage mStorage = RetrofitClient.getClient().create(SysStorage.class);
        Observable<SysInitData> initObser = mStorage.sysInit("main", DeviceIDMananger.sharedInstance().getDeviceID(), token, "", PackageInfoMananger.sharedInstance().getVersionInfo().getVersonName());
        NetworkApi.ApiSubscribe(initObser, sysInit -> {
            if (sysInit.hasError()) {
                ToastUtil.showToast(DevLib.getApp(), sysInit.error.usermsg);
                return;
            }
            SysInitManager.sharedInstance().setSysInitData(sysInit);
        });
    }


}
