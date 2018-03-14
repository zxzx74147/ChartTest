package com.zxzx74147.stock.data;

import java.io.Serializable;

/**
 * Created by zhengxin on 2018/3/5.
 */

public class MachPosition implements Serializable {
    public long machPositionId;   //挂单ID
    public int status;   //状态，0:等待建仓，1:建仓成功，2:建仓撤销，3:建仓失败
    public String goodsId;     //商品代码
    public String goodsType;     //商品类型代码
    public String name;     //商品名称
    public int buySell;   //买卖方向(1-买,2-卖)
    public float amount;      //持仓手数
    public float price;      //挂单价格
    public String amountPerLot;     //每手数量(字符串描述)
    public float openCost;      //建仓成本
    public float openCharge;      //建仓手续费
    public float limit;      //止盈(不设则为空)
    public float stop;      //止损(不设则为空)
    public float error;      //误差
    public float deferred;      //过夜费
    public String validTime;     //有效期(option)，仅（status==0时有效）
    public long positionId;   //仓位ID(option)，仅（status==1时有效）
    public String reason;     //建仓撤销/失败原因(option)（仅 status==2, 3时有效）

    public String getStatus(){
        switch (status){
            case 0:
                return "等待建仓";
            case 1:
                return "建仓成功";
            case 2:
                return "建仓撤销";
            case 3:
                return "建仓失败";

        }
        return null;
    }
}
