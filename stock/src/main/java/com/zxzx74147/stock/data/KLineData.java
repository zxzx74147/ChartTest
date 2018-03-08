
package com.zxzx74147.stock.data;

import com.zxzx74147.devlib.data.UniApiData;

import java.io.Serializable;

public class KLineData extends UniApiData implements Serializable
{

    public PriceKChartList PriceKChartList;
    private final static long serialVersionUID = 312835172865273086L;
    public boolean mIsRealtime = false;
    public int mType = 0;

}
