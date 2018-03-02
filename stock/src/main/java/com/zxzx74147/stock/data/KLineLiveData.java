package com.zxzx74147.stock.data;

import android.arch.lifecycle.LiveData;
import android.text.TextUtils;

import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.stock.storage.StockStorage;

import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by zhengxin on 2018/2/8.
 */

public class KLineLiveData extends LiveData<KLineData> {

    private GoodType mGood = null;
    private String mType = "";
    private static final int PERIOD = 1000 * 5;
    private Timer mTimer = null;
    private Disposable mDisposable = null;

    public KLineLiveData() {
    }

    public void setGood(GoodType good) {
        mGood = good;
        doRefresh();
    }

    public void setKType(String type) {
        mType = type;
        doRefresh();
    }

    private StockStorage mStockStorage = RetrofitClient.getStockClient().create(StockStorage.class);

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
        if(mDisposable!=null){
            mDisposable.dispose();
        }
        mTimer.cancel();
    }

    private void doRefresh() {

        if (mGood == null || TextUtils.isEmpty(mType)) {
            return;
        }
        if(mDisposable!=null){
            mDisposable.dispose();
        }
        Observable<KLineData> kLineCall = mStockStorage.getKLine(mGood.goodsType, mType);

        NetworkApi.ApiSubscribe(kLineCall, new Observer<KLineData>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(KLineData kline) {
                setValue(kline);
                mDisposable = null;
            }

            @Override
            public void onError(Throwable e) {
                setValue(UniApiData.createError(e, KLineData.class));
                mDisposable = null;
            }

            @Override
            public void onComplete() {
                mDisposable = null;
            }
        });

    }




}
