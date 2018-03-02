package com.zxzx74147.balance.data;

import android.arch.lifecycle.LiveData;

import com.zxzx74147.balance.storage.PayStorage;
import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;

import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by zhengxin on 2018/3/1.
 */

public class PayResultLiveData extends LiveData<PayResultData> {


    private long depositId = 0;
    private Disposable mDisposable = null;
    private Timer mTimer = null;
    private static final int PERIOD = 1000 * 5;
    
    public void setDepositId(long good) {
        depositId = good;
        doRefresh();
    }


    private PayStorage mStockStorage = RetrofitClient.getStockClient().create(PayStorage.class);

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

        if (depositId == 0) {
            return;
        }
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        Observable<PayResultData> kLineCall = mStockStorage.payVerify(depositId);

        NetworkApi.ApiSubscribe(kLineCall, new Observer<PayResultData>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(PayResultData kline) {
                setValue(kline);
                mDisposable = null;
            }

            @Override
            public void onError(Throwable e) {
                setValue(UniApiData.createError(e, PayResultData.class));
                mDisposable = null;
            }

            @Override
            public void onComplete() {
                mDisposable = null;
            }
        });

    }


}
