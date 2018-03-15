package com.zxzx74147.devlib.base;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.devlib.R;
import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;
import com.zxzx74147.devlib.widget.CommonLoading;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by zhengxin on 2018/2/6.
 */

public class BaseActivity extends SupportActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    protected CommonCallback mCallback = null;
    protected IntentData mIntentData = null;
    MyObserver myObserver = null;
    public CommonLoading mLoading = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mLoading = new CommonLoading(this);
        myObserver = new MyObserver(this, getLifecycle());
        mCallback = ZXActivityJumpHelper.getCallBack();
        mIntentData = (IntentData) getIntent().getSerializableExtra(ZXActivityJumpHelper.INTENT_DATA);
    }

    @Override
    protected void onResume() {
        super.onResume();
        View v = findViewById(R.id.close);
        if(v!=null){
            RxView.clicks(v).subscribe(a->{finish();});
        }

    }

    public class MyObserver implements LifecycleObserver {
        private Lifecycle mLifecycle;

        public MyObserver(Context context, Lifecycle lifecycle) {
            mLifecycle = lifecycle;
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void connectListener() {
            Log.i(TAG, "ON_RESUME");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void disconnectListener() {
            Log.i(TAG, "ON_PAUSE");
        }
    }

}
