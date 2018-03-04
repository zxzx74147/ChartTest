
package com.zxzx74147.stock.data;

import java.io.Serializable;

public class Price implements Serializable
{

    public static final int UP = 1;
    public static final int DOWN = 2;
    public String goodsType;
    public int curPrice;
    public int upDown;
    public int open;
    public int close;
    public int high;
    public int low;
    public String priceDate;
    public String priceTime;
    public String marketDate;
    private final static long serialVersionUID = -885665069966858729L;

}
