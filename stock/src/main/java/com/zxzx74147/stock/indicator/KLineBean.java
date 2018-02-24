package com.zxzx74147.stock.indicator;

import com.zxzx74147.stock.data.PriceKChart;
import com.zxzx74147.stock.utils.ChartUtil;

/**
 * Created by loro on 2017/2/8.
 */
public class KLineBean {

    public KLineBean(PriceKChart chart) {
        date = chart.priceDateTime;
        open = chart.open;
        close = chart.close;
        high = chart.high;
        low = chart.low;
        vol = 1;
        xVal = ChartUtil.parserTime(date);
    }

    public String date;
    public float xVal;
    public float open;
    public float close;
    public float high;
    public float low;
    public float vol;
}
