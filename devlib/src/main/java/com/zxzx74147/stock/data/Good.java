
package com.zxzx74147.stock.data;

import java.io.Serializable;

public class Good implements Serializable
{
    /**
     * goodsId : AGB100G
     * name : 银饰品0.1KG
     * goodsType : AG
     * weight : 100g
     * amountPerLot : 0.1
     * profitPerUnit : 0.1
     * depositFee : 9
     * openChargeFee : 0.7
     * closeChargeFee : 0
     * deferredFee : 0.1
     * maxLot : 20
     * minLot : 1
     * category : 1
     * status : 1
     * sort : 1
     */

    public String goodsId;
    public String name;
    public String goodsType;
    public String weight;
    public float amountPerLot;
    public float profitPerUnit;
    public float depositFee;
    public float openChargeFee;
    public float closeChargeFee;
    public float deferredFee;
    public float costBack    ;      //多少点可以回本
    public int maxLot;
    public int minLot;
    public int category;
    public int status;
    public int sort;

//    public String goodsId;
//    public String name;
//    public String goodsType;
//    public String weight;
//    public int amountPerLot;
//    public int profitPerUnit;
//    public int depositFee;
//    public int openChargeFee;
//    public int closeChargeFee;
//    public int deferredFee;
//    public int maxLot;
//    public int minLot;
//    public int category;
//    public int status;
//    public int sort;
//    private final static long serialVersionUID = 8994065032781608136L;
//
//    /**
//     * amountPerLot : 0.1
//     * profitPerUnit : 0.1
//     * openChargeFee : 0.7
//     * deferredFee : 0.1
//     */
//
//    @SerializedName("amountPerLot")
//    public double amountPerLotX;
//    @SerializedName("profitPerUnit")
//    public double profitPerUnitX;
//    @SerializedName("openChargeFee")
//    public double openChargeFeeX;
//    @SerializedName("deferredFee")
//    public double deferredFeeX;
}
