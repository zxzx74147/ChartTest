
package com.zxzx74147.live.data;

import android.arch.lifecycle.LiveData;

import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.live.stroage.LiveStorage;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LiveMsgListLiveData extends LiveData<LiveMsgListData> implements Serializable {

    private static final int MAX_MSG_NUM = 300;
    private Live mLive = null;
    private static final int PERIOD = 1000 * 2;
    private Timer mTimer = null;
    private Disposable mDisposable = null;
    private LinkedList<Msg> mMsgData = new LinkedList<>();
    private LinkedList<Msg> mBullsetMsgData = new LinkedList<>();

    public LiveMsgListLiveData() {
    }

    public void LiveMsgListLiveData(Live live) {
        mLive = live;
        doRefresh();
    }

    public void setLive(Live live){
        mLive = live;
    }

    private LiveStorage mStockStorage = RetrofitClient.getClient().create(LiveStorage.class);

    @Override
    protected void onActive() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                doRefresh();
            }
        }, 0, PERIOD);

    }

    @Override
    protected void onInactive() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        mTimer.cancel();
    }

    private void doRefresh() {
        if (mLive == null) {
            return;
        }
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        LiveMsgListData data = getValue();

        Observable<LiveMsgListData> kLineCall = mStockStorage.msgList(mLive.liveId, data == null ? 0 : data.lastMId);

        NetworkApi.ApiSubscribe(kLineCall, new Observer<LiveMsgListData>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(LiveMsgListData kline) {
                updateMsg(kline.msgList.msg);
                kline.msgList.msg = mMsgData;
                setValue(kline);
                mDisposable = null;
            }

            @Override
            public void onError(Throwable e) {
                setValue(UniApiData.createError(e, LiveMsgListData.class));
                mDisposable = null;
            }

            @Override
            public void onComplete() {
                mDisposable = null;
            }
        });

    }

    public void updateMsg(List<Msg> msgs){
        if(msgs==null){
            return;
        }
        for(Msg msg:msgs){
            switch (msg.type){
                case Msg.TYPE_BULLET:
                    mBullsetMsgData.addLast(msg);
                    break;
                case Msg.TYPE_TEXT:
                case Msg.TYPE_PROFIT:
                case Msg.TYPE_SYSTEM:
                    mMsgData.addLast(msg);
                    break;
                    default:

            }

        }
        while(mMsgData.size()>MAX_MSG_NUM){
            mMsgData.removeFirst();
        }
        while(mBullsetMsgData.size()>MAX_MSG_NUM){
            mBullsetMsgData.removeFirst();
        }

    }

    public Msg popBullet(){
        if(mBullsetMsgData.size()>0){
            return mBullsetMsgData.removeFirst();
        }
        return null;
    }

}
