package com.zxzx74147.stock.utils;

import com.github.mikephil.charting.data.CandleEntry;
import com.zxzx74147.stock.data.KLineData;
import com.zxzx74147.stock.data.PriceItem;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhengxin on 2018/2/8.
 */

public class DataConvUtil {
    public static List<CandleEntry> convKLine(KLineData kLineData) {
        List<CandleEntry> result = new LinkedList<>();
        int count = 0;
        for (PriceItem kData : kLineData.priceKChart) {
            CandleEntry entry = kData.toCandleEntry();
            entry.setX(count++);
            result.add(entry);
        }
        return result;

    }
}
