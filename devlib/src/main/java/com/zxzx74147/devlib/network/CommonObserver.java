package com.zxzx74147.devlib.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.zxzx74147.devlib.data.ErrorData;
import com.zxzx74147.devlib.data.UniApiData;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.zxzx74147.devlib.data.UniApiData.createError;

/**
 * Created by zhengxin on 2018/2/12.
 */

public class CommonObserver<T> implements Observer<T> {

    private MutableLiveData<T> mData;

    public CommonObserver(MutableLiveData<T> data){
        mData = data;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onNext(T kline) {
        mData.setValue(kline);
    }

    @Override
    public void onError(Throwable e) {
        ErrorData error = new ErrorData();
        T uniData = (T) UniApiData.createError(error);
        mData.setValue(uniData);
    }
}
