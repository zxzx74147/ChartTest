package com.zxzx74147.stock.data;

import com.zxzx74147.devlib.data.BaseListData;
import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.data.UserData;
import com.zxzx74147.devlib.interfaces.IBaseListDataHolder;
import com.zxzx74147.profile.data.UserUniData;

import java.io.Serializable;

/**
 * Created by zhengxin on 2018/2/21.
 */

public class PositionListData extends UniApiData implements Serializable ,IBaseListDataHolder{
    public PositionList positionList;

    public UserData user;

    @Override
    public BaseListData<Position> getListData() {
        return positionList;
    }
}
