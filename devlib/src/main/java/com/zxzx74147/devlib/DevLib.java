package com.zxzx74147.devlib;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.commonsdk.UMConfigure;
import com.zxzx74147.devlib.image.ImageLoader;
import com.zxzx74147.devlib.kvstore.KVStore;
import com.zxzx74147.devlib.modules.busstation.MainBusStation;
import com.zxzx74147.devlib.utils.DisplayUtil;
import com.zxzx74147.devlib.utils.SharedPreferenceHelper;
import com.zxzx74147.devlib.wxapi.WxApiHandler;

import me.yokeyword.fragmentation.Fragmentation;

/**
 * Created by zhengxin on 2018/2/6.
 */

public class DevLib {

    @SuppressLint("StaticFieldLeak")
    private static Application mApplication = null;

    private DevLib() {

    }

    public static void onCreate(Application application) {
        mApplication = application;
        initLeakCanary();
        initFragmentation();
        initStetho();
        initSharedPreference();
        initOthers();
        initUmeng();
        initPush();
        initWXAPI();
    }

    public static Application getApp() {
        return mApplication;
    }

    private static void initFragmentation() {
        Fragmentation.builder()
                .stackViewMode(Fragmentation.NONE)
                .debug(BuildConfig.DEBUG)
                .handleException(e -> e.printStackTrace())
                .install();
    }

    private static void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(mApplication)) {
            return;
        }
        LeakCanary.install(mApplication);
    }

    private static void initStetho(){
        Stetho.initializeWithDefaults(mApplication);
    }

    private static void initSharedPreference(){
        SharedPreferenceHelper.init(mApplication);
    }

    private static void initOthers(){
        DisplayUtil.init(mApplication);
        KVStore.init(mApplication);
        ImageLoader.init(mApplication);
    }

    private static void initUmeng(){
        UMConfigure.init(mApplication, UMConfigure.DEVICE_TYPE_PHONE, mApplication.getResources().getString(R.string.umeng));
    }

    private static void initWXAPI(){
        WxApiHandler.createApi(mApplication);
    }

    private static void initPush(){
        PushServiceFactory.init(mApplication);
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(mApplication, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d("initPush","id"+pushService.getDeviceId());
                KVStore.put("push_id",pushService.getDeviceId());
                MainBusStation.onPushSucc(DevLib.getApp(),pushService.getDeviceId());
            }
            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.d("initPush", "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });

    }

}
