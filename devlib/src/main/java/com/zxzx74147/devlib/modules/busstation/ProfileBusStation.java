package com.zxzx74147.devlib.modules.busstation;

import android.content.Context;

import com.zxzx74147.devlib.data.MessageEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhengxin on 2018/2/12.
 */

public class ProfileBusStation {




    public static final int BUS_ID_PROFILE_LOGIN = BusIDGen.genBusID("BUS_ID_PROFILE_LOGIN");
    public static final int BUS_ID_PROFILE_DETAIL = BusIDGen.genBusID("BUS_ID_PROFILE_DETAIL");
    public static final int BUS_ID_PROFILE_SET_TRADE_PASSWORD = BusIDGen.genBusID("BUS_ID_PROFILE_SET_TRADE_PASSWORD");
    public static final int BUS_ID_PROFILE_TRADE_LOGIN = BusIDGen.genBusID("BUS_ID_PROFILE_TRADE_LOGIN");


    private ProfileBusStation() {

    }

    public static void startLogin(Context context) {
        MessageEvent event = new MessageEvent<>(BUS_ID_PROFILE_LOGIN, context);
        EventBus.getDefault().post(event);
    }

    public static void startProfile(Context context) {
        MessageEvent event = new MessageEvent<>(BUS_ID_PROFILE_DETAIL, context);
        EventBus.getDefault().post(event);
    }

    public static void startSetTradePassword(Context context) {
        MessageEvent event = new MessageEvent<>(BUS_ID_PROFILE_SET_TRADE_PASSWORD, context);
        EventBus.getDefault().post(event);
    }

    public static void startTradeLogin(Context context) {
        MessageEvent event = new MessageEvent<>(BUS_ID_PROFILE_TRADE_LOGIN, context);
        EventBus.getDefault().post(event);
    }




}
