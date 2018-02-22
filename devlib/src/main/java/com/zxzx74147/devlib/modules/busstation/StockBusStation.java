package com.zxzx74147.devlib.modules.busstation;

import android.content.Context;

import com.zxzx74147.devlib.data.MessageEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhengxin on 2018/2/12.
 */

public class StockBusStation {

    public static final int BUS_ID_STOCK = BusIDGen.genBusID("BUS_ID_STOCK");


    private StockBusStation() {

    }

    public static void startStock(Context context, String type) {
        MessageEvent<String > event = new MessageEvent<>(BUS_ID_STOCK, context, type);
        EventBus.getDefault().post(event);
    }



}
