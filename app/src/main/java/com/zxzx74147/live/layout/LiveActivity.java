package com.zxzx74147.live.layout;

import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.charttest.R;
import com.zxzx74147.charttest.databinding.ActivityLiveBinding;
import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.modules.account.UserViewModel;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.DisplayUtil;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.live.data.Live;
import com.zxzx74147.live.media.IjkVideoViewHolder;
import com.zxzx74147.live.stroage.LiveStorage;
import com.zxzx74147.live.viewmodel.LiveMsgViewModel;

import io.reactivex.functions.Consumer;

public class LiveActivity extends BaseActivity {


    private ActivityLiveBinding mBinding = null;
    private LiveMsgViewModel mLiveMsgViewModel = null;
    private UserViewModel mUserViewModel = null;
    private LiveStorage mLiveStorage = RetrofitClient.getClient().create(LiveStorage.class);
    private Live mLive = null;
    private boolean mIsRequestRotate = false;


    private IjkVideoViewHolder mVideoHolder1, mVideoHolder2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_live);
        mLiveMsgViewModel = ViewModelProviders.of(this).get(LiveMsgViewModel.class);
        mLive = (Live) mIntentData.data;
        mLiveMsgViewModel.setLive(mLive);
        mLiveMsgViewModel.getLiveMsgListLiveData().setLive(mLive);
        mBinding.setLive(mLive);
        initData();
        initView();
        initVideo();
        mIsRequestRotate = false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void initView() {
//        Matrix matrix = new Matrix();
//        matrix.reset();
//        matrix.setScale(-1,1, DisplayUtil.getDisplayMetrics().widthPixels/2,0);
//        mBinding.video2.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                Matrix matrix = new Matrix();
//                matrix.reset();
//                matrix.setScale(-1,1, (right-left)/2,0);
//                mBinding.video2.setTransform(matrix);
//            }
//        });
//        mBinding.video1.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                Matrix matrix = new Matrix();
//                matrix.reset();
//                matrix.setScale(-1,1, (right-left)/2,0);
//                mBinding.video1.setTransform(matrix);
//            }
//        });
        switch (this.getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                RxView.clicks(mBinding.video2).subscribe(v -> {
                    prepareToRotate();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                });
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                RxView.clicks(mBinding.video2).subscribe(v -> {
                    prepareToRotate();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                });
                break;

        }
    }

    public void prepareToRotate() {
        mIsRequestRotate = true;
//        mVideoHolder1.clearRender();
//        mVideoHolder2.clearRender();
    }

    private static final String RTMP_HKS = "rtmp://live.hkstv.hk.lxdns.com/live/hks";

    private void initVideo() {
        if (mIsRequestRotate) {
            mVideoHolder1.setTextureRender(mBinding.video2);
            mVideoHolder2.setTextureRender(mBinding.video1);
            mIsRequestRotate = false;
            return;
        }
        mVideoHolder1 = new IjkVideoViewHolder(this);
        mVideoHolder2 = new IjkVideoViewHolder(this);

        mVideoHolder1.setTextureRender(mBinding.video1);
        mVideoHolder2.setTextureRender(mBinding.video2);

        if (mLive.rtmpList.rtmp.size() > 0) {
//            mVideoHolder1.setVideoPath(RTMP_HKS);
            mVideoHolder1.setVideoPath(mLive.rtmpList.rtmp.get(0).url);
            mVideoHolder1.start();
        }

        if (mLive.rtmpList.rtmp.size() > 1) {
            mVideoHolder2.setVideoPath(mLive.rtmpList.rtmp.get(1).url);
//            mVideoHolder2.setVideoPath(RTMP_HKS);
            mVideoHolder2.start();
        }else{
            mBinding.video2.setVisibility(View.GONE);
        }
    }

    private void initData() {

        mUserViewModel = ViewModelProviders.of(LiveActivity.this).get(UserViewModel.class);
//        mBinding.setUser(mUserViewModel.getUserUniLiveData().getValue().user);
        mUserViewModel.getUserUniLiveData().observe(LiveActivity.this, userUniData -> {
            if (userUniData.hasError()) {
                return;
            }
            mBinding.setUser(userUniData.user);
        });


//        mLiveMsgViewModel.getLiveMsgListLiveData().observe(this, new Observer<LiveMsgListData>() {
//            @Override
//            public void onChanged(@Nullable LiveMsgListData liveMsgListData) {
//                if(liveMsgListData.hasError()){
//                    return;
//                }
//                mBinding.setLiveMsg(liveMsgListData);
//            }
//        });

        if (mIsRequestRotate) {
            return;
        }
        NetworkApi.ApiSubscribe(mLiveStorage.roomJoin(mLive.liveId), uniApiData -> {
            if (uniApiData.hasError()) {
                ToastUtil.showToast(LiveActivity.this, uniApiData.error.usermsg);
                return;
            }
        });


    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIsRequestRotate) {
            return;
        }

        mVideoHolder1.stopPlayback();
        mVideoHolder1.release(true);
        mVideoHolder2.stopPlayback();
        mVideoHolder2.release(true);

        NetworkApi.ApiSubscribe(mLiveStorage.roomQuite(mLive.liveId), new Consumer<UniApiData>() {
            @Override
            public void accept(UniApiData uniApiData) throws Exception {
                if (uniApiData.hasError()) {
                    return;
                }
            }
        });

    }


}
