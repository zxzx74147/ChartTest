package com.zxzx74147.stock.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhengxin on 2018/2/21.
 */

public class PositionList implements Serializable {
    public int num;
    public float amount          ;      //持仓手数(gethis接口不返回)
    public float openCost;      //持仓总资产（元）(gethis接口不返回)
    public List<Position> position;
}
