
package com.zxzx74147.stock.data;

import com.zxzx74147.devlib.data.BaseListData;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class GoodsList extends BaseListData implements Serializable
{

    public LinkedList<GoodType> goodType = null;
    private final static long serialVersionUID = -803843101521150948L;

    @Override
    public List getListItems() {
        return goodType;
    }
}
