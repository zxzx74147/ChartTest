package com.zxzx74147.devlib.base;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by zhengxin on 2018/2/6.
 */

public class BaseActivity extends SupportActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    protected CommonCallback mCallback = null;
    MyObserver myObserver = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myObserver =new MyObserver(this,getLifecycle());
        mCallback = ZXActivityJumpHelper.getCallBack();
    }

    public class MyObserver implements LifecycleObserver {
        private Lifecycle mLifecycle;
        public MyObserver(Context context, Lifecycle lifecycle) {
            mLifecycle = lifecycle;
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void connectListener() {
            Log.i(TAG,"ON_RESUME");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void disconnectListener() {
            Log.i(TAG,"ON_PAUSE");
        }
    }

}
