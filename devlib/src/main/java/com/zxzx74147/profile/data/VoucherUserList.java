
package com.zxzx74147.profile.data;

import com.zxzx74147.devlib.data.BaseListData;
import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.interfaces.IBaseListDataHolder;

import java.io.Serializable;
import java.util.List;

public class VoucherUserList extends BaseListData<Voucher> implements Serializable
{

    public List<Voucher> userVoucher;

    @Override
    public List<Voucher> getListItems() {
        return userVoucher;
    }
}
