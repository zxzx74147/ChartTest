
package com.zxzx74147.profile.data;

import com.zxzx74147.stock.data.Good;

import java.io.Serializable;

public class Voucher implements Serializable
{

    public int voucherId;
    public String goodsId;
    public String getTime;
    public String validStartDt;
    public String validEndDt;
    public String voucherTypeId;
    public int status;
    public int positionId;
    public String title;
    public Good goods;
    private final static long serialVersionUID = 6384799606472812016L;

}
