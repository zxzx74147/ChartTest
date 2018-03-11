package com.zxzx74147.profile.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

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

public class UserUniLiveData extends MutableLiveData<UserUniData> {




    @Override
    protected void onActive() {
        AccountManager.sharedInstance().registerConsumer(mConsumer);
    }

    @Override
    protected void onInactive() {
        AccountManager.sharedInstance().unregisterConsumer(mConsumer);
    }

    private Consumer  mConsumer = new Consumer<UserUniData>() {

        @Override
        public void accept(UserUniData userUniData) throws Exception {
            setValue(userUniData);
        }
    };


}
