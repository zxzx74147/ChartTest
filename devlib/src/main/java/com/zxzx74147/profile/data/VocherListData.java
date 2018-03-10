
package com.zxzx74147.profile.data;

import com.zxzx74147.devlib.data.BaseListData;
import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.interfaces.IBaseListDataHolder;

import java.io.Serializable;

public class VocherListData extends UniApiData implements Serializable,IBaseListDataHolder<Voucher>
{

    public VoucherList userVoucherList;
    private final static long serialVersionUID = -1146491776787044507L;

    @Override
    public BaseListData<Voucher> getListData() {
        return userVoucherList;
    }
}
