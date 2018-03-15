package com.zxzx74147.devlib.modules.busstation;

import android.content.Context;

import com.zxzx74147.devlib.data.MessageEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhengxin on 2018/2/12.
 */

public class MainBusStation {




    public static final int BUS_ID_MAIN_START_MAIN = BusIDGen.genBusID("BUS_ID_MAIN_START_MAIN");

    public static final int BUS_ID_MAIN_LOGOUT = BusIDGen.genBusID("BUS_ID_MAIN_LOGOUT");

    public static final int BUS_ID_PUSH_SUCC = BusIDGen.genBusID("BUS_ID_PUSH_SUCC");

    public static final int BUS_ID_MAIN_TO_LIVE = BusIDGen.genBusID("BUS_ID_MAIN_TO_LIVE");

    public static final int BUS_ID_MAIN_START_UNICORN = BusIDGen.genBusID("BUS_ID_MAIN_TO_LIVE");
    public static final int BUS_ID_MAIN_SHOW_ABOUT = BusIDGen.genBusID("BUS_ID_MAIN_SHOW_ABOUT");


    private MainBusStation() {

    }

    public static void startMain(Context context) {
        MessageEvent event = new MessageEvent<>(BUS_ID_MAIN_START_MAIN, context);
        EventBus.getDefault().post(event);
    }

    public static void startAbout(Context context) {
        MessageEvent event = new MessageEvent<>(BUS_ID_MAIN_SHOW_ABOUT, context);
        EventBus.getDefault().post(event);
    }

    public static void logout(Context context) {
        MessageEvent event = new MessageEvent<>(BUS_ID_MAIN_LOGOUT, context);
        EventBus.getDefault().post(event);
    }

    public static void onPushSucc(Context context,String token) {
        MessageEvent event = new MessageEvent<>(BUS_ID_PUSH_SUCC, context);
        event.data= token;
        EventBus.getDefault().post(event);
    }

    public static void toLive(Context context) {
        MessageEvent event = new MessageEvent<>(BUS_ID_MAIN_TO_LIVE, context);
        EventBus.getDefault().post(event);
    }

    public static void toUnicorn(Context context) {
        MessageEvent event = new MessageEvent<>(BUS_ID_MAIN_START_UNICORN, context);
        EventBus.getDefault().post(event);
    }






}
