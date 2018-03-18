package com.zxzx74147.stock.data;

import com.zxzx74147.devlib.data.UniApiData;

import java.io.Serializable;

/**
 * Created by zhengxin on 2018/2/28.
 */

public class MachPositionData extends UniApiData implements Serializable{
    public MachPosition machPosition;
    public Fail failed;
}
