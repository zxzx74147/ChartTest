
package com.zxzx74147.profile.data;

import com.zxzx74147.devlib.data.BaseListData;

import java.io.Serializable;
import java.util.List;

public class VoucherList  extends BaseListData<Voucher> implements Serializable

{
    public List<Voucher> voucher = null;
    private final static long serialVersionUID = -8001284931850519223L;

    @Override
    public List<Voucher> getListItems() {
        return voucher;
    }
}
