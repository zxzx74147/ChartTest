package com.zxzx74147.stock.data;

import android.arch.lifecycle.LiveData;

import com.zxzx74147.devlib.data.ErrorData;
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

//    private GoodItem mGood = null;
//    private static final int PERIOD = 1000 * 5;
//    private Timer mTimer = null;
//
//    public KLineLiveData(GoodItem good) {
//        mGood = good;
//    }
//
//    public void setGood(GoodItem good) {
//        mGood = good;
//
//        mTimerTask.run();
//    }
//
//    private StockStorage mStockStorage = RetrofitClient.getStockClient().create(StockStorage.class);
//
//    @Override
//    protected void onActive() {
//        mTimer = new Timer();
//        mTimer.schedule(mTimerTask, 0, PERIOD);
//    }
//
//    private TimerTask mTimerTask = new TimerTask() {
//        @Override
//        public void run() {
//            if (mGood == null) {
//                return;
//            }
//            Observable<KLineData> kLineCall = mStockStorage.getKLine(mGood.TypeCode, 1);
//            NetworkApi.ApiSubscribe(kLineCall, new Observer<KLineData>() {
//                @Override
//                public void onSubscribe(Disposable d) {
//
//                }
//
//                @Override
//                public void onNext(KLineData kline) {
//                    setValue(kline);
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    ErrorData error = new ErrorData();
//                    KLineData uniData = new KLineData();
//                    uniData.error = error;
//                    setValue(uniData);
//                }
//
//                @Override
//                public void onComplete() {
//
//                }
//            });
//        }
//    };
//
//    @Override
//    protected void onInactive() {
//        mTimer.cancel();
//    }
}
