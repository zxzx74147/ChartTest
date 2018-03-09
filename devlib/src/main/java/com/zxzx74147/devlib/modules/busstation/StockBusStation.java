package com.zxzx74147.devlib.modules.busstation;

import android.content.Context;

import com.zxzx74147.devlib.data.MessageEvent;
import com.zxzx74147.devlib.modules.account.AccountManager;
import com.zxzx74147.stock.data.GoodType;
import com.zxzx74147.stock.data.MachPosition;
import com.zxzx74147.stock.data.Position;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhengxin on 2018/2/12.
 */

public class StockBusStation {

    public static final int BUS_ID_STOCK_VIEW = BusIDGen.genBusID("BUS_ID_STOCK_VIEW");

    public static final int BUS_ID_STOCK_TRADE = BusIDGen.genBusID("BUS_ID_STOCK_TRADE");
    public static final int BUS_ID_VIEW_POSITION = BusIDGen.genBusID("BUS_ID_VIEW_POSITION");
    public static final int BUS_ID_VIEW_TRADE = BusIDGen.genBusID("BUS_ID_VIEW_TRADE");
    public static final int BUS_ID_POSITION_MODIFY = BusIDGen.genBusID("BUS_ID_POSITION_MODIFY");
    public static final int BUS_ID_POSITION_CLOSE = BusIDGen.genBusID("BUS_ID_POSITION_CLOSE");
    public static final int BUS_ID_MACH_POSITION_MODIFY = BusIDGen.genBusID("BUS_ID_MACH_POSITION_MODIFY");
    public static final int BUS_ID_MACH_POSITION_CLOSE = BusIDGen.genBusID("BUS_ID_MACH_POSITION_CLOSE");


    private StockBusStation() {

    }

    public static void startStock(Context context, GoodType type) {
        MessageEvent<GoodType > event = new MessageEvent<>(BUS_ID_STOCK_VIEW, context, type);
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
        MessageEvent event = new MessageEvent<>(BUS_ID_VIEW_POSITION, context);
        EventBus.getDefault().post(event);
    }
    public static void viewMachPosition(Context context) {
        MessageEvent event = new MessageEvent<>(BUS_ID_VIEW_POSITION, context);
        event.type = 1;
        EventBus.getDefault().post(event);
    }

    public static void viewTrade(Context context) {
        MessageEvent event = new MessageEvent<>(BUS_ID_VIEW_TRADE, context);
        EventBus.getDefault().post(event);
    }

    public static void modifyPosition(Context context, Position position) {
        MessageEvent event = new MessageEvent<>(BUS_ID_POSITION_MODIFY, context);
        event.data = position;
        EventBus.getDefault().post(event);
    }



    public static void closePosition(Context context, Position position) {
        MessageEvent event = new MessageEvent<>(BUS_ID_POSITION_CLOSE, context);
        event.data = position;
        EventBus.getDefault().post(event);
    }

    public static void modifyMachposition(Context context, MachPosition position) {
        MessageEvent event = new MessageEvent<>(BUS_ID_MACH_POSITION_MODIFY, context);
        event.data = position;
        EventBus.getDefault().post(event);
    }

    public static void closeMachposition(Context context, MachPosition position) {
        MessageEvent event = new MessageEvent<>(BUS_ID_MACH_POSITION_CLOSE, context);
        event.data = position;
        EventBus.getDefault().post(event);
    }



}
