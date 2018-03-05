
package com.zxzx74147.balance.data;

import com.zxzx74147.devlib.data.BaseListData;
import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.interfaces.IBaseListDataHolder;

import java.io.Serializable;

public class DepositListData extends UniApiData implements Serializable,IBaseListDataHolder<Deposit>
{

    public DepositList depositList;
    private final static long serialVersionUID = -1997781447951606696L;

    @Override
    public BaseListData<Deposit> getListData() {
        return depositList;
    }
}
