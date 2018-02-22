package com.zxzx74147.devlib.modules.busstation;

import android.util.SparseArray;

import java.util.HashMap;

/**
 * Created by zhengxin on 2018/2/12.
 */

public class BusIDGen {
    private static int BUS_ID_START = 1000;
    private static SparseArray<String> mBusIDMap = new SparseArray<>();

    public static int genBusID(String name) {
        BUS_ID_START++;
        mBusIDMap.put(BUS_ID_START,name);
        return BUS_ID_START;
    }
}
