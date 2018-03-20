
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
    public boolean equals(Object goodType){
        if(goodType==null){
            return false;
        }
        if(!(goodType instanceof GoodType)){
            return false;
        }
        if(((GoodType) goodType).goodsType==null){
            return ((GoodType) goodType).goodsType==goodType;
        }
        if(((GoodType) goodType).goodsType.equals(goodsType)){
            return true;
        }
        return false;
    }
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
