package com.zxzx74147.devlib.modules.account;

import com.zxzx74147.devlib.data.UserData;
import com.zxzx74147.devlib.kvstore.KVStore;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.profile.data.UserUniData;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by zhengxin on 2018/2/21.
 */

public class AccountManager {
    private static final String TAG = AccountManager.class.getSimpleName();
    private static final String KEY = "ACCOUNT_USER";
    private static final AccountManager mInstance = new AccountManager();
    private UserData mUser = null;
    private UserUniData mUserUniData = null;

    private UserStorage mUserStorage = RetrofitClient.getClient().create(UserStorage.class);
    private static final int PERIOD = 1000 * 10;
    private Disposable mDisposable = null;
    private Timer mTimer = null;
    private boolean mIsActive = false;

    private List<Consumer> mConsumers = new LinkedList<>();

    private AccountManager() {
        mUser = KVStore.get(KEY, UserData.class);
    }

    public static AccountManager sharedInstance() {
        return mInstance;
    }

    public void saveUser(UserData user) {
        if (user == null) {
            return;
        }
        mUser = user;
        KVStore.put(KEY, user);
        checkStatus();
    }


    public boolean isLogin() {
        if (mUser == null) {
            return false;
        }
        return true;
    }

    public String getUid() {
        if (mUser == null) {
            return null;
        }
        return mUser.uId;
    }

    public void logout() {
        mUser = null;
        KVStore.put(KEY, "");
    }


    public UserData getUser() {
        return mUser;
    }


    public void registerConsumer(Consumer consumer) {
        mConsumers.add(consumer);
        checkStatus();
        try {
            consumer.accept(mUserUniData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unregisterConsumer(Consumer consumer) {
        mConsumers.remove(consumer);
        checkStatus();
    }

    private void checkStatus() {
        if (mConsumers.size() > 0 && !mIsActive) {
            onActive();
        }
        if (mConsumers.size() == 0 && mIsActive) {
            onInactive();
        }
    }


    private void onActive() {
        mIsActive = true;
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


    private void onInactive() {
        mIsActive = false;
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
                for(Consumer consumer:mConsumers){
                    consumer.accept(userUniData);
                }
                mUserUniData = userUniData;
                saveUser(userUniData.user);
            }
        });

    }


}
