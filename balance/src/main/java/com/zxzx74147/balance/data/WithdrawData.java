package com.zxzx74147.balance.data;

import com.zxzx74147.devlib.data.UniApiData;

import java.io.Serializable;

/**
 * Created by zhengxin on 2018/3/1.
 */

public class WithdrawData extends UniApiData implements Serializable {
    public int needAuth;   //是否需要实名认证(1：需要, 0:不需要)
    public long withdrawID;   //提现流水号
}
