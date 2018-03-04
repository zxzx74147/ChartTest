package com.zxzx74147.stock;

import android.app.Application;

import com.zxzx74147.devlib.data.MessageEvent;
import com.zxzx74147.devlib.modules.busstation.StockBusStation;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.stock.activity.TradeListActivity;
import com.zxzx74147.stock.data.GoodType;
import com.zxzx74147.stock.data.Position;
import com.zxzx74147.stock.fragment.PositionFragment;
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
//            IntentData<GoodItem> intentData = new IntentData<>();
//            intentData.data = (GoodItem) event.data;
//            ZXActivityJumpHelper.startActivity(event.context, StockActivity.class, intentData);
            StockFragment fragment = StockFragment.newInstance((GoodType) event.data);
            fragment.show(ViewUtil.getFragmentActivity(event.context).getSupportFragmentManager(),fragment.getTag());
        }else if (event.id == StockBusStation.BUS_ID_STOCK_TRADE) {
            TradeFragment fragment = TradeFragment.newInstance((GoodType) event.data,event.type);
            fragment.show((ViewUtil.getFragmentActivity(event.context)).getSupportFragmentManager(), fragment.getTag());
        }
        else if (event.id == StockBusStation.BUS_ID_VIEW_POSITION) {
            PositionFragment fragment = PositionFragment.newInstance();
            fragment.show((ViewUtil.getFragmentActivity(event.context)).getSupportFragmentManager(), fragment.getTag());
        }
        else if (event.id == StockBusStation.BUS_ID_VIEW_TRADE) {
            ZXActivityJumpHelper.startActivity(event.context, TradeListActivity.class);
        }
    }

    static {
        CommonMultiTypeDelegate.registDefaultViewType(GoodType.class, R.layout.item_good);
        CommonMultiTypeDelegate.registDefaultViewType(Position.class, R.layout.item_position);
    }

}
