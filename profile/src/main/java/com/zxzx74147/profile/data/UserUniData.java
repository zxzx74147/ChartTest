package com.zxzx74147.profile.data;

import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.data.UserData;

import java.io.Serializable;

/**
 * Created by zhengxin on 2018/2/20.
 */

public class UserUniData extends UniApiData implements Serializable {
    public String uId;
    public UserData user;
}
