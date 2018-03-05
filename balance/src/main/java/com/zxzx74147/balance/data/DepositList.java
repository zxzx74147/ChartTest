
package com.zxzx74147.balance.data;

import com.zxzx74147.devlib.data.BaseListData;

import java.io.Serializable;
import java.util.List;

public class DepositList extends BaseListData<Deposit> implements Serializable
{

    public List<Deposit> deposit = null;
    private final static long serialVersionUID = -6248660443700361106L;

    @Override
    public List<Deposit> getListItems() {
        return deposit;
    }
}
