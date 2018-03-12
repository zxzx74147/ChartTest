package com.zxzx74147.balance.data;

import com.zxzx74147.devlib.data.BaseListData;
import com.zxzx74147.devlib.data.UniApiData;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhengxin on 2018/3/1.
 */

public class WithdrawList extends BaseListData<Withdraw> implements Serializable {
    public List<Withdraw> withdraw;
    @Override
    public List<Withdraw> getListItems() {
        return withdraw;
    }
}
