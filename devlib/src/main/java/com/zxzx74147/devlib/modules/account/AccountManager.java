package com.zxzx74147.devlib.modules.account;

import com.zxzx74147.devlib.data.UserData;
import com.zxzx74147.devlib.kvstore.KVStore;
import com.zxzx74147.profile.data.UserUniData;

/**
 * Created by zhengxin on 2018/2/21.
 */

public class AccountManager {
    private static final String TAG = AccountManager.class.getSimpleName();
    private static final String KEY = "ACCOUNT_USER";
    private static final AccountManager mInstance = new AccountManager();
    private UserData mUser = null;

    private AccountManager() {
        mUser = KVStore.get(KEY, UserData.class);
    }

    public static AccountManager sharedInstance() {
        return mInstance;
    }

    public void saveUser(UserData user) {
        if(user==null){
            return;
        }
        mUser = user;
        KVStore.put(KEY, user);
    }


    public boolean isLogin(){
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

    public void logout(){
        mUser = null;
        KVStore.put(KEY,"");
    }


    public UserData getUser() {
        return mUser;
    }




}
