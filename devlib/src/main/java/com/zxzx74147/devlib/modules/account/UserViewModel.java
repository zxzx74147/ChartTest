package com.zxzx74147.devlib.modules.account;

import com.zxzx74147.devlib.base.BaseViewModel;
import com.zxzx74147.profile.data.UserUniLiveData;

/**
 * Created by zhengxin on 2018/2/8.
 */

public class UserViewModel extends BaseViewModel {

    private UserUniLiveData mUserUniLiveData = new UserUniLiveData();


    public UserUniLiveData getUserUniLiveData() {
        return mUserUniLiveData;
    }


}
