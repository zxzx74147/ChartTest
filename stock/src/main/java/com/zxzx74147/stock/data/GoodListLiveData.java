package com.zxzx74147.stock.data;

import android.arch.lifecycle.LiveData;

import com.zxzx74147.devlib.data.ErrorData;
import com.zxzx74147.devlib.modules.account.AccountManager;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.stock.storage.GoodStorage;

import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by zhengxin on 2018/2/8.
 */

public class GoodListLiveData extends LiveData<GoodListData> {

    private static final int PERIOD = 1000 * 5;
    private Timer mTimer = null;
    private String mType = null;

    public GoodListLiveData(String type) {
        mType = type;
    }

    public void setType(String type) {
        mType = type;
        doRefresh();
    }

    private GoodStorage mStockStorage = RetrofitClient.getClient().create(GoodStorage.class);

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

    public void doRefresh() {
        if (!AccountManager.sharedInstance().isLogin()) {
            return;
        }
        Observable<GoodListData> kLineCall = mStockStorage.getGoods(mType);
        NetworkApi.ApiSubscribe(kLineCall, new Observer<GoodListData>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(GoodListData kline) {
                setValue(kline);
            }

            @Override
            public void onError(Throwable e) {
                ErrorData error = ErrorData.createError(e);
                GoodListData uniData = new GoodListData();
                uniData.error = error;
                setValue(uniData);
            }

            @Override
            public void onComplete() {

            }
        });
    }


    @Override
    protected void onInactive() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
}
