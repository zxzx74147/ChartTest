package com.zxzx74147.balance.data;

import com.zxzx74147.devlib.data.BaseListData;
import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.interfaces.IBaseListDataHolder;

import java.io.Serializable;

/**
 * Created by zhengxin on 2018/3/1.
 */

public class WithdrawListData extends UniApiData implements Serializable,IBaseListDataHolder<Withdraw> {
    public WithdrawList withdrawList;

    @Override
    public BaseListData<Withdraw> getListData() {
        return withdrawList;
    }
}
