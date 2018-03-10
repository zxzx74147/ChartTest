package com.zxzx74147.live.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.zxzx74147.live.data.Live;
import com.zxzx74147.live.data.LiveMsgListLiveData;

import java.io.Serializable;

/**
 * Created by zhengxin on 2018/2/26.
 */

public class LiveMsgViewModel extends ViewModel {

    private Live mLive = null;
    private LiveMsgListLiveData mLiveMsgListLiveData = new LiveMsgListLiveData();

    public void setLive(Live live){
        mLive = live;
    }
    public Live getLive(){
        return mLive;
    }
    public LiveMsgListLiveData getLiveMsgListLiveData(){
        return mLiveMsgListLiveData;
    }
}
