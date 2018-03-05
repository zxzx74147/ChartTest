package com.zxzx74147.profile.data;

import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.data.UserData;
import com.zxzx74147.stock.data.GoodsList;
import com.zxzx74147.stock.data.MachPosition;
import com.zxzx74147.stock.data.MachPositionList;
import com.zxzx74147.stock.data.PositionList;

import java.io.Serializable;

/**
 * Created by zhengxin on 2018/2/20.
 */

public class UserUniData extends UniApiData implements Serializable {
    public String uId;
    public UserData user;
    public PositionList positionList;
    public GoodsList goodsTypeList;
    public MachPositionList machPositionList;


//    public
}
