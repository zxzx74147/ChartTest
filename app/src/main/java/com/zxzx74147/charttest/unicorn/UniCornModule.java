package com.zxzx74147.charttest.unicorn;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.ImageLoaderListener;
import com.qiyukf.unicorn.api.StatusBarNotificationConfig;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.UnicornImageLoader;
import com.qiyukf.unicorn.api.YSFOptions;
import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.image.ImageLoader;
import com.zxzx74147.stock.ModuleStock;

/**
 * Created by zhengxin on 2018/3/14.
 */

public class UniCornModule {

    private static Application mApp = null;
    private static UniCornModule mModule = null;

    private UniCornModule(){
        YSFOptions options = new YSFOptions();
        options.statusBarNotificationConfig = new StatusBarNotificationConfig();
        Unicorn.init(mApp, "aeeac4bfbb8953094b728aee3332fba1", options, new UnicornImageLoader() {
            @Nullable
            @Override
            public Bitmap loadImageSync(String uri, int width, int height) {
                return ImageLoader.loadImageSync(uri,width,height);
            }

            @Override
            public void loadImage(String uri, int width, int height, ImageLoaderListener listener) {
                ImageLoader.loadImageAync(uri, width, height, item -> {
                    if(item instanceof Bitmap){
                        listener.onLoadComplete((Bitmap) item);
                    }else{
                        listener.onLoadFailed(new Throwable("fail"));
                    }
                });
            }
        });
    }

    public static void init(Application application) {
        mApp = application;
        mModule = new UniCornModule();

    }

    public static void startUnicorn(Context context){
        String title = "在线客服";
        /**
         * 设置访客来源，标识访客是从哪个页面发起咨询的，用于客服了解用户是从什么页面进入。
         * 三个参数分别为：来源页面的url，来源页面标题，来源页面额外信息（保留字段，暂时无用）。
         * 设置来源后，在客服会话界面的"用户资料"栏的页面项，可以看到这里设置的值。
         */
        ConsultSource source = new ConsultSource("", "", "custom information string");
        /**
         * 请注意： 调用该接口前，应先检查Unicorn.isServiceAvailable()，
         * 如果返回为false，该接口不会有任何动作
         *
         * @param context 上下文
         * @param title   聊天窗口的标题
         * @param source  咨询的发起来源，包括发起咨询的url，title，描述信息等
         */
        Unicorn.openServiceActivity(context, title, source);
    }
}
