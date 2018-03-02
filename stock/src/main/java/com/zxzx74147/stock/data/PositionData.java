package com.zxzx74147.stock.data;

import com.zxzx74147.devlib.data.UniApiData;

import java.io.Serializable;

/**
 * Created by zhengxin on 2018/2/21.
 */

public class PositionData  extends UniApiData implements Serializable {
    public Position position;
    public Fail failed;
}
