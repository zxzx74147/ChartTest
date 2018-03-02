package com.zxzx74147.live.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.zxzx74147.live.data.LiveMsgListLiveData;

/**
 * Created by zhengxin on 2018/2/26.
 */

public class LiveMsgViewModel extends ViewModel {
    private LiveMsgListLiveData mLiveMsgListLiveData = new LiveMsgListLiveData();

    public LiveMsgListLiveData getLiveMsgListLiveData(){
        return mLiveMsgListLiveData;
    }
}
