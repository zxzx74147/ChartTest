package com.zxzx74147.devlib.modules.busstation;

import android.content.Context;

import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.data.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import javax.security.auth.callback.Callback;

/**
 * Created by zhengxin on 2018/2/12.
 */

public class ProfileBusStation {




    public static final int BUS_ID_PROFILE_LOGIN = BusIDGen.genBusID("BUS_ID_PROFILE_LOGIN");
    public static final int BUS_ID_PROFILE_DETAIL = BusIDGen.genBusID("BUS_ID_PROFILE_DETAIL");
    public static final int BUS_ID_PROFILE_SET_TRADE_PASSWORD = BusIDGen.genBusID("BUS_ID_PROFILE_SET_TRADE_PASSWORD");
    public static final int BUS_ID_PROFILE_TRADE_LOGIN = BusIDGen.genBusID("BUS_ID_PROFILE_TRADE_LOGIN");
    public static final int BUS_ID_PROFILE_MESSAGE_CENTER = BusIDGen.genBusID("BUS_ID_PROFILE_MESSAGE_CENTER");
    public static final int BUS_ID_PROFILE_MOTIFY = BusIDGen.genBusID("BUS_ID_PROFILE_MOTIFY");
    public static final int BUS_ID_PROFILE_VOUCHER = BusIDGen.genBusID("BUS_ID_PROFILE_VOUCHER");

    public static final int BUS_ID_PROFILE_LOGOUT_VERIFY = BusIDGen.genBusID("BUS_ID_PROFILE_LOGOUT_VERIFY");
    public static final int BUS_ID_PROFILE_VOUCHER_LIST = BusIDGen.genBusID("BUS_ID_PROFILE_VOUCHER_LIST");
    public static final int BUS_ID_PROFILE_AUTH = BusIDGen.genBusID("BUS_ID_PROFILE_AUTH");
    public static final int BUS_ID_PROFILE_RESET_TRADE_PASSWORD = BusIDGen.genBusID("BUS_ID_PROFILE_RESET_TRADE_PASSWORD");


    private ProfileBusStation() {

    }

    public static void startLogin(Context context) {
        MessageEvent event = new MessageEvent<>(BUS_ID_PROFILE_LOGIN, context);
        EventBus.getDefault().post(event);
    }

    public static void startLogin(Context context, CommonCallback callback) {
        MessageEvent event = new MessageEvent<>(BUS_ID_PROFILE_LOGIN, context);
        event.callback = callback;
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

    public static void startMessageCenter(Context context) {
        MessageEvent event = new MessageEvent<>(BUS_ID_PROFILE_MESSAGE_CENTER, context);
        EventBus.getDefault().post(event);
    }

    public static void startProfileMotify(Context context) {
        MessageEvent event = new MessageEvent<>(BUS_ID_PROFILE_MOTIFY, context);
        EventBus.getDefault().post(event);
    }

    public static void startProfileVocher(Context context) {
        MessageEvent event = new MessageEvent<>(BUS_ID_PROFILE_VOUCHER, context);
        EventBus.getDefault().post(event);
    }

    public static void startProfileLogout(Context context) {
        MessageEvent event = new MessageEvent<>(BUS_ID_PROFILE_LOGOUT_VERIFY, context);
        EventBus.getDefault().post(event);
    }
    public static void startProfileVoucherList(Context context) {
        MessageEvent event = new MessageEvent<>(BUS_ID_PROFILE_VOUCHER_LIST, context);
        EventBus.getDefault().post(event);
    }

    public static void startProfileAuth(Context context) {
        MessageEvent event = new MessageEvent<>(BUS_ID_PROFILE_AUTH, context);
        EventBus.getDefault().post(event);
    }
    public static void startTradePasswordMotify(Context context) {
        MessageEvent event = new MessageEvent<>(BUS_ID_PROFILE_RESET_TRADE_PASSWORD, context);
        EventBus.getDefault().post(event);
    }






}
