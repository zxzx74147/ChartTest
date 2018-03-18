package com.zxzx74147.profile.data;

import java.io.Serializable;

/**
 * Created by zhengxin on 2018/3/12.
 */

public class Auth implements Serializable {
   public String name;
    public String  identityNo;
    public int   state;//0:未申请, 1:申请中; 2:认证成功; 3:认证失败)
    public String   stateName;
}
