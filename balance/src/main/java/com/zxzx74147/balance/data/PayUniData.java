package com.zxzx74147.balance.data;

import com.zxzx74147.devlib.data.UniApiData;

import java.io.Serializable;

/**
 * Created by zhengxin on 2018/3/1.
 */

public class PayUniData extends UniApiData implements Serializable {
    public String html;    //！！成功后返回，支付页面的html，客户端给一个回调地址，回调会携带参数depositId
}
