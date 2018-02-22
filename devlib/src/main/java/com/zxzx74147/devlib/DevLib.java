package com.zxzx74147.devlib;

import android.annotation.SuppressLint;
import android.app.Application;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.zxzx74147.devlib.image.ImageLoader;
import com.zxzx74147.devlib.kvstore.KVStore;
import com.zxzx74147.devlib.utils.DisplayUtil;
import com.zxzx74147.devlib.utils.SharedPreferenceHelper;

import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;

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
    }

    public static Application getApp() {
        return mApplication;
    }

    private static void initFragmentation() {
        Fragmentation.builder()
                .stackViewMode(Fragmentation.BUBBLE)
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
}
