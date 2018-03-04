
package com.zxzx74147.profile.data;

import com.zxzx74147.stock.data.Good;

import java.io.Serializable;

public class Voucher implements Serializable
{

    public static final int TYPE_END_TIME=1;
    public static final int TYPE_DAY_NUM=2;
    public String voucherId;
    public String title;
    public String goodsId;
    public String activeStartDt;
    public String activeEndDt;
    public int useType;
    public String useStartDt;
    public String useEndDt;
    public int useDays;
    public int amount;
    public int customerAmount;
    public int receiveAmount;
    public String comment;
    public Good goods;
    private final static long serialVersionUID = 1551373688982540207L;

}
