package com.zxzx74147.live.layout;

import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.live.R;
import com.zxzx74147.live.data.Live;
import com.zxzx74147.live.databinding.ActivityLiveBinding;
import com.zxzx74147.live.stroage.LiveStorage;
import com.zxzx74147.live.viewmodel.LiveMsgViewModel;

import io.reactivex.functions.Consumer;

public class LiveActivity extends BaseActivity {
    private ActivityLiveBinding mBinding = null;
    private LiveMsgViewModel mLiveMsgViewModel = null;
    private LiveStorage mLiveStorage = RetrofitClient.getClient().create(LiveStorage.class);
    private Live mLive = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_live);
        mLiveMsgViewModel = ViewModelProviders.of(this).get(LiveMsgViewModel.class);
        mLive = (Live) mIntentData.data;
        mLiveMsgViewModel.setLive(mLive);
        mLiveMsgViewModel.getLiveMsgListLiveData().setLive(mLive);
        initData();
        initView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void initView() {
        switch (this.getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                RxView.clicks(mBinding.ijkVideoView2).subscribe(v -> {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                });
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                RxView.clicks(mBinding.ijkVideoView1).subscribe(v -> {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                });
                break;

        }
    }

    private void initData() {

        NetworkApi.ApiSubscribe(mLiveStorage.roomJoin(mLive.liveId), new Consumer<UniApiData>() {
            @Override
            public void accept(UniApiData uniApiData) throws Exception {
                if(uniApiData.hasError()){
                    ToastUtil.showToast(LiveActivity.this,uniApiData.error.usermsg);
                    return;
                }
            }
        });


//        mBinding.ijkVideoView1.setVideoPath(mLive.rtmpList.rtmp.get(0).url.trim());
//        mBinding.ijkVideoView1.start();
//        if (mLive.rtmpList.rtmp.size() > 1) {
//            mBinding.ijkVideoView2.setVideoPath(mLive.rtmpList.rtmp.get(1).url.trim());
//            mBinding.ijkVideoView2.start();
//        }
    }


}
