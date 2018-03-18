
package com.zxzx74147.stock.data;

import java.io.Closeable;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class GoodType implements Serializable
{

    public String goodsType;
    public LinkedList<Good> goods = null;
    public String goodsTypeName;
    public Price price;
    public int buyUserNum;
    public int sellUserNum;

    public boolean mIsSelect = false;

    @Override
    public GoodType clone() {

//        try {
//            return (GoodType) super.clone();
//        } catch (CloneNotSupportedException e) {
//            e.printStackTrace();
//        }
        GoodType type=  new GoodType();
        type.goods = goods;
        type.goodsType=goodsType;
        type.goodsTypeName=goodsTypeName;
        type.price=price;
        type.buyUserNum=buyUserNum;
        type.sellUserNum=sellUserNum;
        return type;
    }

}
