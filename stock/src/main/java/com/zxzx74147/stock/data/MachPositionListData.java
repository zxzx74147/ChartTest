package com.zxzx74147.stock.data;

import com.zxzx74147.devlib.data.BaseListData;
import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.interfaces.IBaseListDataHolder;

import java.io.Serializable;

/**
 * Created by zhengxin on 2018/2/28.
 */

public class MachPositionListData extends UniApiData implements Serializable,IBaseListDataHolder<MachPosition> {
    public MachPositionList machPositionList;

    @Override
    public BaseListData<MachPosition> getListData() {
        return machPositionList;
    }
}
