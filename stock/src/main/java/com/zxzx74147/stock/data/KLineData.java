package com.zxzx74147.stock.data;

import com.zxzx74147.devlib.data.UniApiData;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhengxin on 2018/2/8.
 */

public class KLineData extends UniApiData implements Serializable {
    public int num;
    public List<PriceItem> priceKChart;


}
