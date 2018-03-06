package com.zxzx74147.devlib.modules.busstation;

import android.content.Context;

import com.zxzx74147.devlib.data.MessageEvent;
import com.zxzx74147.live.data.Teacher;
import com.zxzx74147.live.data.Text;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhengxin on 2018/2/12.
 */

public class LiveBusStation {




    public static final int BUS_ID_LIVE_TEACHER = BusIDGen.genBusID("BUS_ID_LIVE_TEACHER");

    public static final int BUS_ID_FEED_VIEW = BusIDGen.genBusID("BUS_ID_FEED_VIEW");


    private LiveBusStation() {

    }

    public static void startTeacherProfile(Context context, Teacher teacher) {
        MessageEvent event = new MessageEvent<>(BUS_ID_LIVE_TEACHER, context);
        event.data = teacher;
        EventBus.getDefault().post(event);
    }

    public static void startFeedView(Context context, Text text) {
        MessageEvent event = new MessageEvent<>(BUS_ID_FEED_VIEW, context);
        event.data = text;
        EventBus.getDefault().post(event);
    }






}
