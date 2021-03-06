package com.zxzx74147.balance;

import android.app.Application;
import android.view.View;

import com.zxzx74147.balance.activity.DepositListActivity;
import com.zxzx74147.balance.activity.WithdrawActivity;
import com.zxzx74147.balance.data.BankCard;
import com.zxzx74147.balance.data.RechargeAmount;
import com.zxzx74147.balance.fragment.RechargeFragment;
import com.zxzx74147.balance.fragment.UnBindCardFragment;
import com.zxzx74147.devlib.data.IntentData;
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
        }else if(event.id== BalanceBusStation.BUS_ID_BALANCE_DEPOSIT_LIST){
           ZXActivityJumpHelper.startActivity(event.context, DepositListActivity.class,null);
        }else if(event.id== BalanceBusStation.BUS_ID_BALANCE_WITHDRAW){
            ZXActivityJumpHelper.startActivity(event.context, WithdrawActivity.class,null);
        }else if(event.id== BalanceBusStation.BUS_ID_BALANCE_UNBIND){
            UnBindCardFragment fragement = UnBindCardFragment.newInstance(new IntentData<BankCard>((BankCard) event.data));
            ZXFragmentJumpHelper.startFragment(event.context,fragement,null);
        }
    }

    static {
        CommonMultiTypeDelegate.registDefaultViewType(RechargeAmount.class, R.layout.item_recharge_amount);
    }
}
