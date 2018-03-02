package com.zxzx74147.balance.data;

import com.zxzx74147.devlib.data.UniApiData;

import java.io.Serializable;

/**
 * Created by zhengxin on 2018/3/1.
 */

public class PayResultData extends UniApiData implements Serializable {
    public static final int RESULT_NONE = 0;

    public static final int RESULT_DOING = 1;
    public static final int RESULT_SUCC = 2;
    public static final int RESULT_FAIL = 3;

    public int depositStatus;   //订单状态（0 未支付，1 支付中，2 支付成功， 3 支付失败，其他待定）
}
