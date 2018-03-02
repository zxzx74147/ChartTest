package com.zxzx74147.balance.data;

import java.io.Serializable;

/**
 * Created by zhengxin on 2018/3/1.
 */

public class PayRequest implements Serializable{
    public static final int TYPE_WECHAT = 0;
    public static final int TYPE_ALIPAY = 1;
    public static final int TYPE_UNIPAY = 2;
    public int amount;
    public int type=TYPE_WECHAT;
}
