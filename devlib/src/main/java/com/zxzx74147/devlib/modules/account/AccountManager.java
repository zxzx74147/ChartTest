package com.zxzx74147.devlib.modules.account;

import com.zxzx74147.devlib.data.UserData;
import com.zxzx74147.devlib.kvstore.KVStore;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.SharedPreferenceHelper;
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
    private static final String KEY_MSG_ID = "KEY_MSG_ID2";


    private static final AccountManager mInstance = new AccountManager();
    private UserData mUser = null;
    private UserUniData mUserUniData = null;
    private int mLastReadMessageID = 0;

    private UserStorage mUserStorage = RetrofitClient.getClient().create(UserStorage.class);
    private static final int PERIOD = 1000 * 5;
    private Disposable mDisposable = null;
    private Timer mTimer = null;
    private boolean mIsActive = false;

    private List<Consumer> mConsumers = new LinkedList<>();

    private AccountManager() {
        mUser = KVStore.get(KEY, UserData.class);
        mLastReadMessageID = SharedPreferenceHelper.getInt(KEY_MSG_ID, 0);
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
        KVStore.put(KEY_MSG_ID, 0);
    }


    public UserData getUser() {
        return mUser;
    }

    public UserUniData getUserUni() {
        return mUserUniData;
    }


    public void registerConsumer(Consumer consumer) {
        mConsumers.add(consumer);
        checkStatus();
        try {
            if (mUserUniData != null) {
                consumer.accept(mUserUniData);
            }
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
                if (userUniData.hasError()) {
                    return;
                }
                if (userUniData.userComVoucherInfo != null && userUniData.userComVoucherInfo.voucherId == null) {
                    userUniData.userComVoucherInfo = null;
                }
                for (Consumer consumer : mConsumers) {
                    consumer.accept(userUniData);
                }
                mUserUniData = userUniData;
                saveUser(userUniData.user);
                mUser.unreadNum = mUserUniData.lastMsgCenterId - mLastReadMessageID;
                mUser.unreadNum = Math.max(mUser.unreadNum, 0);
            }
        });
    }

    public void markMessageRead() {
        SharedPreferenceHelper.saveInt(KEY_MSG_ID, mUserUniData.lastMsgCenterId);
        mLastReadMessageID = mUserUniData.lastMsgCenterId;
    }


}
