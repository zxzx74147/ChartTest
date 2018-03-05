package com.zxzx74147.stock.data;

import com.zxzx74147.devlib.data.BaseListData;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhengxin on 2018/3/5.
 */

public class MachPositionList extends BaseListData<MachPosition> implements Serializable {

    public List<MachPosition> machPosition;
    @Override
    public List<MachPosition> getListItems() {
        return machPosition;
    }
}
