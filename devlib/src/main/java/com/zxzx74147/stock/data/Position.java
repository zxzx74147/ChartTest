package com.zxzx74147.stock.data;

import com.zxzx74147.devlib.DevLib;
import com.zxzx74147.devlib.R;

import java.io.Serializable;

/**
 * Created by zhengxin on 2018/2/21.
 */

public class Position implements Serializable {
    public static final int BUY_UP = 1;
    public static final int BUY_DOWN = 2;

    public long positionId;//仓位ID
    public String goodsId;//商品代码
    public String goodsType;//商品类型代码
    public String name;//商品名称
    public float amountPerLot;//每手数量
    public int buySell;//买卖方向(1-买,2-卖)
    public int amount;//持仓手数
    public float openPrice;//建仓价格
    public String positionTime;//建仓时间
    public float openCost;//建仓成本(保证金)
    public float openCharge;//建仓手续费
    public float couponCost;//建仓时红包支付金额
    public String closeTime;//平仓时间(未平仓时为空)
    public float closePrice;//平仓价格(未平仓时为当前价格)
    public float grossProfit;//交易盈亏(未平仓时为当前浮动盈亏)
    public float limit;//止盈(不设则为空)
    public float stop;//止损(不设则为空)
    public int closeType;//平仓类型 1:普通平仓; 2:自动平仓; 3:爆仓平仓; 4:收盘平仓; 5:强制平仓
    public int isDeferred;//是否过夜
    public float deferred;//累计过夜费
    public int isVoucher;
    public DefferList deferredList;

    public int mIsExpand = 0;

    public String getCloseTypeStr() {
        switch (closeType) {
            case 1:
                return "普通平仓";
            case 2:

                return "自动平仓";
            case 3:
                return "爆仓平仓";
            case 4:
                return "收盘平仓";
            case 5:
                return "强制平仓";
            default:
                return "";

        }
    }

    public String getBuyMethodStr() {
        if(isVoucher!=0){
            return DevLib.getApp().getString(R.string.buy_method_red);
        }
        return DevLib.getApp().getString(R.string.buy_method_money);
    }
}
