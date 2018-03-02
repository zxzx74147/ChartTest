package com.zxzx74147.balance.data;

import com.zxzx74147.devlib.data.UniApiData;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhengxin on 2018/3/1.
 */

public class BankList  implements Serializable {
    public int num;     //银行名称
    public List<Bank> bank;     //银行图片 url
}
