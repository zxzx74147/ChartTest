package com.zxzx74147.devlib.modules.busstation;

import android.content.Context;

import com.zxzx74147.devlib.data.MessageEvent;
import com.zxzx74147.live.data.Teacher;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhengxin on 2018/2/12.
 */

public class BalanceBusStation {




    public static final int BUS_ID_BALANCE_RECHARGE= BusIDGen.genBusID("BUS_ID_BALANCE_RECHARGE");


    private BalanceBusStation() {

    }

    public static void startRecharge(Context context) {
        MessageEvent event = new MessageEvent<>(BUS_ID_BALANCE_RECHARGE, context);
        EventBus.getDefault().post(event);
    }








}
