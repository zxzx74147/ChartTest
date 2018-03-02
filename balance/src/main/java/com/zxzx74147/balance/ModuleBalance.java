package com.zxzx74147.balance;

import android.app.Application;
import android.view.View;

import com.zxzx74147.balance.data.RechargeAmount;
import com.zxzx74147.balance.fragment.RechargeFragment;
import com.zxzx74147.devlib.data.MessageEvent;
import com.zxzx74147.devlib.modules.busstation.BalanceBusStation;
import com.zxzx74147.devlib.modules.busstation.ProfileBusStation;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;
import com.zxzx74147.devlib.utils.ZXFragmentJumpHelper;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by zhengxin on 2018/2/8.
 */

public class ModuleBalance {

    private static Application mApp = null;
    private static ModuleBalance mModule = null;

    public static void init(Application application) {
        mApp = application;
        mModule = new ModuleBalance();
    }

    private ModuleBalance() {
        EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if(event.id== BalanceBusStation.BUS_ID_BALANCE_RECHARGE){
            ZXFragmentJumpHelper.startFragment(ViewUtil.getFragmentActivity(event.context),RechargeFragment.class,null);
        }
    }

    static {
        CommonMultiTypeDelegate.registDefaultViewType(RechargeAmount.class, R.layout.item_recharge_amount);
    }
}
