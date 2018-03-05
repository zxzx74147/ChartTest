package com.zxzx74147.stock.widget;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding2.support.design.widget.RxTabLayout;
import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.data.DialogItem;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.data.UserData;
import com.zxzx74147.devlib.data.WheelSelectorData;
import com.zxzx74147.devlib.fragment.CommonFragmentDialog;
import com.zxzx74147.devlib.fragment.CommonWheelSelectorDialog;
import com.zxzx74147.devlib.interfaces.IViewModelHolder;
import com.zxzx74147.devlib.modules.busstation.ProfileBusStation;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.FormatUtil;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.utils.ZXFragmentJumpHelper;
import com.zxzx74147.stock.R;
import com.zxzx74147.stock.data.Fail;
import com.zxzx74147.stock.data.Good;
import com.zxzx74147.stock.data.GoodType;
import com.zxzx74147.stock.data.PositionData;
import com.zxzx74147.stock.databinding.WidgetTradeBinding;
import com.zxzx74147.stock.storage.TradesStorage;

import io.reactivex.Observable;

/**
 * Created by zhengxin on 2018/2/8.
 */

public class TradeWidget extends LinearLayout implements IViewModelHolder {
    private WidgetTradeBinding mBinding = null;
    private GoodType mGoodType = null;
    public static final int BUY_TYPE_UP = 1;
    public static final int BUY_TYPE_DOWN = 2;
    private int mTotalAmount = 0;
    private Good mSelectGood;
    private int mAmount = 1;
    private int mButType = BUY_TYPE_UP;
    private TradesStorage mTradeStorage = RetrofitClient.getClient().create(TradesStorage.class);


    @BindingAdapter({"good"})
    public static void setGood(TradeWidget view, GoodType good) {
        view.setGood(good);
    }


    @BindingAdapter({"user"})
    public static void setUser(TradeWidget view, UserData user) {
        view.setUser(user);
    }


    public TradeWidget(Context context) {
        this(context, null);
    }

    public TradeWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TradeWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }




    private void initView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.widget_trade, this, true);
        ViewUtil.setSelect(mBinding.byUp, true);
        RxView.clicks(mBinding.byUp).subscribe(o -> {
            ViewUtil.setSelect(mBinding.byUp, true);
            ViewUtil.setSelect(mBinding.byDown, false);
            mBinding.setByType(BUY_TYPE_UP);
            mButType = BUY_TYPE_UP;
        });
        mBinding.setByType(mButType);

        RxView.clicks(mBinding.byDown).subscribe(o -> {
            ViewUtil.setSelect(mBinding.byUp, false);
            ViewUtil.setSelect(mBinding.byDown, true);
            mBinding.setByType(BUY_TYPE_DOWN);
            mButType = BUY_TYPE_DOWN;
        });

        RxTabLayout.selectionEvents(mBinding.listType).subscribe(tabLayoutSelectionEvent -> {
            Good good = (Good) tabLayoutSelectionEvent.tab().getTag();
            mSelectGood = good;
            mBinding.setGood(mSelectGood);
            refreshAmount();
        });

        RxTabLayout.selectionEvents(mBinding.listAmount).subscribe(tabLayoutSelectionEvent -> {
            Integer amount = (Integer) tabLayoutSelectionEvent.tab().getTag();
            mAmount = amount;
            refreshAmount();
        });

        RxView.clicks(mBinding.doIt).subscribe(a -> {
            submitTrade();
        });

        RxView.clicks(mBinding.buyStopValue).subscribe(a -> {
            int offset = FormatUtil.getPureNum(mBinding.buyStopValue.getText().toString());
            WheelSelectorData data = new WheelSelectorData();
            data.items = FormatUtil.POINT_LIST;
            data.offset = offset;
            CommonWheelSelectorDialog dialog = CommonWheelSelectorDialog.newInstance(new IntentData<>(data));
            ZXFragmentJumpHelper.startFragment(getContext(), dialog, new CommonCallback() {
                @Override
                public void callback(Object item) {
                    if(item==null){
                        return;
                    }
                    mBinding.buyStopValue.setText(data.items.get((Integer) item));
                }
            });
        });

        RxView.clicks(mBinding.buyLimitValue).subscribe(a -> {
            int offset = FormatUtil.getPureNum(mBinding.buyLimitValue.getText().toString());
            WheelSelectorData data = new WheelSelectorData();
            data.items = FormatUtil.POINT_LIST;
            data.offset = offset;
            CommonWheelSelectorDialog dialog = CommonWheelSelectorDialog.newInstance(new IntentData<>(data));
            ZXFragmentJumpHelper.startFragment(getContext(), dialog, new CommonCallback() {
                @Override
                public void callback(Object item) {
                    if(item==null){
                        return;
                    }
                    mBinding.buyLimitValue.setText(data.items.get((Integer) item));
                }
            });
        });


    }

    public void setCallback(CommonCallback<GoodType> callback) {

    }

    public void setGood(GoodType good) {
        mGoodType = good;
        refreshData();
    }

    public void setUser(UserData user) {
        mBinding.setUser(user);
    }

    public void refreshAmount() {
        if (mSelectGood == null) {
            return;
        }
        float total = mAmount * mSelectGood.depositFee;
        mBinding.setTotal(total);
    }

    public void refreshData() {

        if (mGoodType == null) {
            return;
        }
        mBinding.listType.removeAllTabs();
        mBinding.listAmount.removeAllTabs();
        for (Good good : mGoodType.goods) {
            String temp = (String.valueOf(good.depositFee) + "元/手");
            TabLayout.Tab newTab = mBinding.listType.newTab();
            newTab.setText(temp);
            newTab.setTag(good);
            mBinding.listType.addTab(newTab.setText(temp));

        }
        if (mGoodType.goods != null && mGoodType.goods.size() > 0) {
            mSelectGood = mGoodType.goods.get(0);
            mBinding.setGood(mSelectGood);
        }

        for (int i = 1; i < 10; i++) {
            String temp = i + "手";
            TabLayout.Tab newTab = mBinding.listAmount.newTab();
            newTab.setText(temp);
            newTab.setTag(i);
            mBinding.listAmount.addTab(newTab);
        }
        mAmount = 1;
        refreshAmount();


    }

    public void submitTrade() {
        if (mSelectGood == null) {
            return;
        }
        int stop = FormatUtil.getPureNum(mBinding.buyStopValue.getText().toString());
        int limit = FormatUtil.getPureNum(mBinding.buyLimitValue.getText().toString());
        String stopStr = stop==0? "":String.valueOf(stop/100f);
        String limitStr = limit==0? "":String.valueOf(limit/100f);
        Observable<PositionData> observable = mTradeStorage.positionOpen(mSelectGood.goodsId, mButType, mAmount, null, limitStr, stopStr, null, 1);

        NetworkApi.ApiSubscribe(observable, machPositionData -> {
            if (machPositionData.hasError()) {
                if (machPositionData.failed != null) {
                    if(machPositionData.failed.errno== Fail.FAIL_PASS_OUT_OF_TIME){
                        ProfileBusStation.startTradeLogin(getContext());
                        return;
                    }
                    DialogItem dialogItem = new DialogItem();
                    dialogItem.title = machPositionData.failed.title;
                    dialogItem.content = machPositionData.failed.advice;
                    CommonFragmentDialog fragmentDialog = CommonFragmentDialog.newInstance(new IntentData<>(dialogItem));
                    ZXFragmentJumpHelper.startFragment(getContext(), fragmentDialog, new CommonCallback() {
                        @Override
                        public void callback(Object item) {
                            //TODO

                        }
                    });
                    return;
                }
                ToastUtil.showToast(getContext(), machPositionData.error.usermsg);
                return;
            }

        });
    }


    private void init() {
        initView();
    }

    @Override
    public void setProvider(ViewModelProvider provider) {

    }

    @Override
    public void setLifeCircle(LifecycleOwner owner) {

    }
}
