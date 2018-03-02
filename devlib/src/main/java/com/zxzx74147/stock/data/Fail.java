package com.zxzx74147.stock.data;

import java.io.Serializable;

/**
 * Created by zhengxin on 2018/2/28.
 */

public class Fail implements Serializable {
    public static final int FAIL_PASS_NOT_SET = 1;
    public static final int FAIL_PASS_OUT_OF_TIME = 2;
    public static final int FAIL_PASS_MONEY_NOT_ENOUGTH = 3;
    public static final int FAIL_PASS_TICKET_NOT_ENOUGTH = 4;
    public static final int FAIL_PASS_RIST = 5;
    public static final int FAIL_PASS_NOT_IN_TIME = 6;
    public int errno;   //1:交易密码未设置，2:交易密码过期，3:余额不足 4:代金券不足，5:账户存在风险，6:休市中
    public String title;     //
    public String advise;     //
}
