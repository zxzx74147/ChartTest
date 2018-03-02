package com.zxzx74147.devlib.modules.busstation;

import android.content.Context;

import com.zxzx74147.devlib.data.MessageEvent;
import com.zxzx74147.devlib.modules.account.AccountManager;
import com.zxzx74147.stock.data.GoodType;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhengxin on 2018/2/12.
 */

public class StockBusStation {

    public static final int BUS_ID_STOCK = BusIDGen.genBusID("BUS_ID_STOCK");

    public static final int BUS_ID_STOCK_TRADE = BusIDGen.genBusID("BUS_ID_STOCK_TRADE");
    public static final int BUS_ID_VIEW_STOKE = BusIDGen.genBusID("BUS_ID_VIEW_STOKE");


    private StockBusStation() {

    }

    public static void startStock(Context context, String type) {
        MessageEvent<String > event = new MessageEvent<>(BUS_ID_STOCK, context, type);
        EventBus.getDefault().post(event);
    }


    public static void startStockTrade(Context context, GoodType good,int type) {
        if(AccountManager.sharedInstance().getUser().needTradePasswd!=0&&AccountManager.sharedInstance().getUser().hasTradePasswd==0){
            ProfileBusStation.startSetTradePassword(context);
            return;
        }
        MessageEvent<GoodType> event = new MessageEvent<>(BUS_ID_STOCK_TRADE, context, good);
        event.type = type;
        EventBus.getDefault().post(event);
    }

    public static void viewPosition(Context context) {
        MessageEvent event = new MessageEvent<>(BUS_ID_VIEW_STOKE, context);
        EventBus.getDefault().post(event);
    }


}
