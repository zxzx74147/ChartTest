package com.zxzx74147.profile.data;

import android.arch.lifecycle.LiveData;

import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.modules.account.AccountManager;
import com.zxzx74147.devlib.modules.account.UserStorage;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;

import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by zhengxin on 2018/2/20.
 */

public class UserUniLiveData extends LiveData<UserUniData> {

    private UserStorage mUserStorage = RetrofitClient.getClient().create(UserStorage.class);
    private Disposable mDisposable = null;
    private static final int PERIOD = 1000 * 10;
    private Timer mTimer = null;

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
        mTimer = null;
    }

    public void doRefresh() {

        if (!AccountManager.sharedInstance().isLogin()) {
            return;
        }
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        Observable<UserUniData> userCall = mUserStorage.accountGet();
        NetworkApi.ApiSubscribe(userCall, new Consumer<UserUniData>() {
            @Override
            public void accept(UserUniData userUniData) throws Exception {
                setValue(userUniData);
                AccountManager.sharedInstance().saveUser(userUniData.user);
            }
        });
//        NetworkApi.ApiSubscribe(userCall, new Observer<UserUniData>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                mDisposable = d;
//            }
//
//            @Override
//            public void onNext(UserUniData user) {
//                setValue(user);
//                AccountManager.sharedInstance().saveUser(user.user);
//                mDisposable = null;
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                setValue(UniApiData.createError(e, UserUniData.class));
//                mDisposable = null;
//            }
//
//            @Override
//            public void onComplete() {
//                mDisposable = null;
//            }
//        });

    }
}
