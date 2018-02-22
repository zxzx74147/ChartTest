package com.zxzx74147.stock.data;

import com.github.mikephil.charting.data.CandleEntry;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhengxin on 2018/2/12.
 */

public class PriceItem implements ICandleEntry,Serializable{
    private static SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    public String priceDateTime;
    public float op;//3691,
    public float close;//3691,
    public float high;// 3691,
    public float low;// 3691

    @Override
    public CandleEntry toCandleEntry() {
        long time = 0;
        try {
            Date date = parser.parse(priceDateTime);
            time = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new CandleEntry(time, high, low, op, low);
    }
}
