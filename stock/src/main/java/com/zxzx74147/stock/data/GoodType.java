
package com.zxzx74147.stock.data;

import java.io.Serializable;
import java.util.List;

public class GoodType implements Serializable
{

    public String goodsType;
    public List<Good> goods = null;
    public String goodsTypeName;
    public Price price;
    private final static long serialVersionUID = -3100535611730018923L;

}
