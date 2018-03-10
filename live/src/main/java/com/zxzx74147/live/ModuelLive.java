package com.zxzx74147.live;

import android.app.Application;
import android.support.v4.app.FragmentActivity;

import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.data.MessageEvent;
import com.zxzx74147.devlib.modules.busstation.LiveBusStation;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.live.activity.FeedReplyActivity;
import com.zxzx74147.live.data.Live;
import com.zxzx74147.live.data.Msg;
import com.zxzx74147.live.data.Reply;
import com.zxzx74147.live.data.Teacher;
import com.zxzx74147.live.data.TeacherLive;
import com.zxzx74147.live.data.Text;
import com.zxzx74147.live.fragments.TeacherFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;

/**
 * Created by zhengxin on 2018/2/7.
 */

public class ModuelLive {
    private static Application mApp = null;
    private static ModuelLive mModule = null;

    private ModuelLive() {
        EventBus.getDefault().register(this);
    }

    public static void init(Application application) {
        mApp = application;
        mModule = new ModuelLive();
    }

    static {
        CommonMultiTypeDelegate.registDefaultViewType(Live.class, R.layout.item_live);
        CommonMultiTypeDelegate.registDefaultViewType(TeacherLive.class, R.layout.item_live_time);
        CommonMultiTypeDelegate.registDefaultViewType(Text.class, R.layout.item_text);
        CommonMultiTypeDelegate.registDefaultViewType(Reply.class, R.layout.item_reply);
        CommonMultiTypeDelegate.registDefaultViewType(Msg.class, R.layout.item_msg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.id == LiveBusStation.BUS_ID_LIVE_TEACHER) {
            BaseDialogFragment fragment = TeacherFragment.newInstance((Teacher) event.data);
            fragment.show(((FragmentActivity) event.context).getSupportFragmentManager(), fragment.getTag());
        }else  if (event.id == LiveBusStation.BUS_ID_FEED_VIEW) {
            ZXActivityJumpHelper.startActivity(event.context, FeedReplyActivity.class,new IntentData((Serializable) event.data));
        }
    }
}
