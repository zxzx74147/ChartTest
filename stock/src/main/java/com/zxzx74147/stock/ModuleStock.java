package com.zxzx74147.stock;

import android.app.Application;

import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.data.MessageEvent;
import com.zxzx74147.devlib.modules.account.AccountManager;
import com.zxzx74147.devlib.modules.busstation.StockBusStation;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.stock.activity.TradeListActivity;
import com.zxzx74147.stock.data.GoodType;
import com.zxzx74147.stock.data.MachPosition;
import com.zxzx74147.stock.data.Position;
import com.zxzx74147.stock.fragment.DefferListFragment;
import com.zxzx74147.stock.fragment.PositionCloseFragment;
import com.zxzx74147.stock.fragment.PositionFragment;
import com.zxzx74147.stock.fragment.PositionModifyFragment;
import com.zxzx74147.stock.fragment.StockFragment;
import com.zxzx74147.stock.fragment.TradeFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by zhengxin on 2018/2/8.
 */

public class ModuleStock {

    private static Application mApp = null;
    private static ModuleStock mModule = null;

    public static void init(Application application) {
        mApp = application;
        mModule = new ModuleStock();
    }

    private ModuleStock() {
        EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.id == StockBusStation.BUS_ID_STOCK_VIEW) {
            StockFragment fragment = StockFragment.newInstance((GoodType) event.data);
            fragment.show(ViewUtil.getFragmentActivity(event.context).getSupportFragmentManager(),fragment.getTag());
        }else if (event.id == StockBusStation.BUS_ID_STOCK_TRADE) {
            TradeFragment fragment = TradeFragment.newInstance((GoodType) event.data,event.type);
            fragment.show((ViewUtil.getFragmentActivity(event.context)).getSupportFragmentManager(), fragment.getTag());
        }
        else if (event.id == StockBusStation.BUS_ID_VIEW_POSITION) {
            IntentData intent = new IntentData();
            intent.type = event.type;
            PositionFragment fragment = PositionFragment.newInstance(intent);
            if(AccountManager.sharedInstance().getUserUni()==null){
                ToastUtil.showToast(event.context,"加载中，请稍候");
                return;
            }
            fragment.show((ViewUtil.getFragmentActivity(event.context)).getSupportFragmentManager(), fragment.getTag());
        }
        else if (event.id == StockBusStation.BUS_ID_VIEW_TRADE) {
            ZXActivityJumpHelper.startActivity(event.context, TradeListActivity.class);
        }
        else if (event.id == StockBusStation.BUS_ID_POSITION_MODIFY) {
            PositionModifyFragment fragment = PositionModifyFragment.newInstance(new IntentData<>((Position) event.data));
            fragment.show((ViewUtil.getFragmentActivity(event.context)).getSupportFragmentManager(), fragment.getTag());
        }

        else if (event.id == StockBusStation.BUS_ID_POSITION_CLOSE) {
            PositionCloseFragment fragment = PositionCloseFragment.newInstance(new IntentData<>((Position) event.data));
            fragment.show((ViewUtil.getFragmentActivity(event.context)).getSupportFragmentManager(), fragment.getTag());
        }
        else
         if (event.id == StockBusStation.BUS_ID_MACH_POSITION_CLOSE) {
//            PositionCloseFragment fragment = PositionModifyFragment.newInstance(new IntentData<>((Position) event.data));
//            fragment.show((ViewUtil.getFragmentActivity(event.context)).getSupportFragmentManager(), fragment.getTag());
        }else if (event.id == StockBusStation.BUS_ID_MACH_POSITION_DEFFER) {
            DefferListFragment fragment = DefferListFragment.newInstance(new IntentData<>((Position) event.data));
            fragment.show((ViewUtil.getFragmentActivity(event.context)).getSupportFragmentManager(), fragment.getTag());
        }else if (event.id == StockBusStation.BUS_ID_MOTIFY_POSITION) {
            TradeFragment fragment = TradeFragment.newInstance((MachPosition) event.data);
            fragment.show((ViewUtil.getFragmentActivity(event.context)).getSupportFragmentManager(), fragment.getTag());
        }



    }

    static {
        CommonMultiTypeDelegate.registDefaultViewType(GoodType.class, R.layout.item_good);
        CommonMultiTypeDelegate.registDefaultViewType(Position.class, R.layout.item_position);
        CommonMultiTypeDelegate.registDefaultViewType(MachPosition.class, R.layout.item_machposition);
    }

}
