package com.zxzx74147.stock.data;

import java.io.Serializable;

/**
 * Created by zhengxin on 2018/2/28.
 */

public class MachPosition implements Serializable{
    public long machPositionId; //uint64_t   //挂单ID
    public int status;//uint32_t   //状态，0:等待建仓，1:建仓成功，2:建仓撤销，3:建仓失败
    public String goodsId; //  string     //商品代码
    public String goodsType; //   string     //商品类型代码
    public String name;   //  string     //商品名称
    public int buySell;  // uint32_t   //买卖方向(1-买,2-卖)
    public float amount; //  float      //持仓手数
    public float price; //  float      //挂单价格
    public String amountPerLot;//  string     //每手数量(字符串描述)
    public float openCost;////建仓成本
    public float openCharge;//   float      //建仓手续费
    public float limit;//  float      //止盈(不设则为空)
    public float stop;//  float      //止损(不设则为空)
    public float error;// float      //误差
    public float deferred;//  float      //过夜费
    public String validTime;//  string     //有效期(option)，仅（status==0时有效）
    public long positionId;// uint64_t   //仓位ID(option)，仅（status==1时有效）
    public String reason;//    string     //建仓撤销/失败原因(option)（仅 status==2, 3时有效）
}
