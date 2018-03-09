package com.zxzx74147.profile;

import android.app.Application;

import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.data.MessageEvent;
import com.zxzx74147.devlib.modules.busstation.ProfileBusStation;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;
import com.zxzx74147.devlib.utils.ZXFragmentJumpHelper;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.profile.activity.LoginPhoneActivity;
import com.zxzx74147.profile.activity.MessageCenterActivity;
import com.zxzx74147.profile.data.Message;
import com.zxzx74147.profile.fragment.PasswordInputFragment;
import com.zxzx74147.profile.fragment.PasswordSetFragment;
import com.zxzx74147.profile.fragment.ProfileEditFragment;
import com.zxzx74147.profile.fragment.ProfileFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by zhengxin on 2018/2/8.
 */

public class ModuleProfile {

    private static Application mApp = null;
    private static ModuleProfile mModule = null;

    public static void init(Application application) {
        mApp = application;
        mModule = new ModuleProfile();
    }

    private ModuleProfile() {
        EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.id == ProfileBusStation.BUS_ID_PROFILE_LOGIN) {
            ZXActivityJumpHelper.startActivity(event.context, LoginPhoneActivity.class);
        } else if (event.id == ProfileBusStation.BUS_ID_PROFILE_DETAIL) {
            ZXFragmentJumpHelper.startFragment(event.context, ProfileFragment.class,null);
        }else if (event.id == ProfileBusStation.BUS_ID_PROFILE_SET_TRADE_PASSWORD) {
            ZXFragmentJumpHelper.startFragment(event.context, PasswordSetFragment.class,null);
        }else if (event.id == ProfileBusStation.BUS_ID_PROFILE_TRADE_LOGIN) {
            ZXFragmentJumpHelper.startFragment(event.context, PasswordInputFragment.class,null);
        }else if (event.id == ProfileBusStation.BUS_ID_PROFILE_MESSAGE_CENTER) {
            ZXActivityJumpHelper.startActivity(event.context, MessageCenterActivity.class,null);
        }else if (event.id == ProfileBusStation.BUS_ID_PROFILE_MOTIFY) {
            ProfileEditFragment fragment = ProfileEditFragment.newInstance();
            fragment.show((ViewUtil.getFragmentActivity(event.context)).getSupportFragmentManager(), fragment.getTag());
        }
    }

    static {
        CommonMultiTypeDelegate.registDefaultViewType(Message.class,R.layout.item_message);
    }

}
