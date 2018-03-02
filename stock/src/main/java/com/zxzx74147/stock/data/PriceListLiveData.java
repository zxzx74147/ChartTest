package com.zxzx74147.stock.data;

import android.arch.lifecycle.LiveData;
import android.text.TextUtils;

import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.stock.storage.GoodStorage;
import com.zxzx74147.stock.storage.StockStorage;

import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by zhengxin on 2018/2/8.
 */

public class PriceListLiveData extends LiveData<PriceListData> {

    private GoodType mGood = null;
    private static final int PERIOD = 1000 * 5;
    private Timer mTimer = null;
    private Disposable mDisposable = null;

    public PriceListLiveData() {
    }

    public void setGood(GoodType good) {
        mGood = good;
        doRefresh();
    }


    private GoodStorage mStockStorage = RetrofitClient.getStockClient().create(GoodStorage.class);

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

        if (mGood == null ) {
            return;
        }
        if(mDisposable!=null){
            mDisposable.dispose();
        }
        Observable<PriceListData> kLineCall = mStockStorage.getRealTime(mGood.goodsType);

        NetworkApi.ApiSubscribe(kLineCall, new Observer<PriceListData>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(PriceListData kline) {
                setValue(kline);
                mDisposable = null;
            }

            @Override
            public void onError(Throwable e) {
                setValue(UniApiData.createError(e, PriceListData.class));
                mDisposable = null;
            }

            @Override
            public void onComplete() {
                mDisposable = null;
            }
        });

    }




}
