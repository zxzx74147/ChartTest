package com.zxzx74147.stock.widget;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding2.support.design.widget.RxTabLayout;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.data.DialogItem;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.data.UserData;
import com.zxzx74147.devlib.data.WheelSelectorData;
import com.zxzx74147.devlib.fragment.CommonFragmentDialog;
import com.zxzx74147.devlib.fragment.CommonWheelSelectorDialog;
import com.zxzx74147.devlib.interfaces.IViewModelHolder;
import com.zxzx74147.devlib.modules.account.AccountManager;
import com.zxzx74147.devlib.modules.busstation.StockBusStation;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.FormatUtil;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.utils.ZXFragmentJumpHelper;
import com.zxzx74147.profile.data.UserUniData;
import com.zxzx74147.profile.data.Voucher;
import com.zxzx74147.stock.R;
import com.zxzx74147.stock.data.Good;
import com.zxzx74147.stock.data.GoodType;
import com.zxzx74147.stock.data.MachPositionData;
import com.zxzx74147.stock.data.PositionData;
import com.zxzx74147.stock.databinding.WidgetTradeBinding;
import com.zxzx74147.stock.fragment.TradeFragment;
import com.zxzx74147.stock.storage.TradesStorage;
import com.zxzx74147.stock.util.FailDealUtil;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by zhengxin on 2018/2/8.
 */

public class TradeWidget extends LinearLayout implements IViewModelHolder {
    private WidgetTradeBinding mBinding = null;
    private GoodType mGoodType = null;
    private int mType = 0;
    private Good mSelectGood;
    private int mAmount = 1;
    private TradesStorage mTradeStorage = RetrofitClient.getClient().create(TradesStorage.class);



    @BindingAdapter({"good"})
    public static void setGood(TradeWidget view, GoodType good) {
        view.setGood(good);
    }

    @BindingAdapter({"type"})
    public static void setType(TradeWidget view, int type) {
        view.setType(type);
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
            if (mType <= TradeFragment.TYPE_POSITION_BUY_DOWN) {
                mType = TradeFragment.TYPE_POSITION_BUY_UP;
            }else {
                mType = TradeFragment.TYPE_MACH_POSITION_BUY_UP;
            }
            mBinding.setType(mType);

        });
        mBinding.setType(mType);

        RxView.clicks(mBinding.byDown).subscribe(o -> {
            ViewUtil.setSelect(mBinding.byUp, false);
            ViewUtil.setSelect(mBinding.byDown, true);
            if (mType <= TradeFragment.TYPE_POSITION_BUY_DOWN) {
                mType = TradeFragment.TYPE_POSITION_BUY_DOWN;
            }else {
                mType = TradeFragment.TYPE_MACH_POSITION_BUY_DOWN;
            }
            mBinding.setType(mType);
        });

        RxTabLayout.selectionEvents(mBinding.listType).subscribe(tabLayoutSelectionEvent -> {
            Good good = (Good) tabLayoutSelectionEvent.tab().getTag();
            mSelectGood = good;
            mBinding.setGood(mSelectGood);
            refreshAmount();
            refreshVucher();
        });

        RxTabLayout.selectionEvents(mBinding.listAmount).subscribe(tabLayoutSelectionEvent -> {
            Integer amount = (Integer) tabLayoutSelectionEvent.tab().getTag();
            mAmount = amount;
            refreshAmount();
            mBinding.setAmount(amount);
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
                    if (item == null) {
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
                    if (item == null) {
                        return;
                    }
                    mBinding.buyLimitValue.setText(data.items.get((Integer) item));
                }
            });
        });

        RxCompoundButton.checkedChanges(mBinding.balance).subscribe(isChecked->{
            mBinding.voucher.setChecked(!isChecked);
        });

        RxCompoundButton.checkedChanges(mBinding.voucher).subscribe(isChecked->{
            mBinding.balance.setChecked(!isChecked);
        });


    }

    public void setCallback(CommonCallback<GoodType> callback) {

    }

    public void setGood(GoodType good) {
        mGoodType = good;
        refreshData();
    }

    public void setType(int type) {
        mType = type;
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

        mBinding.setType(mType);
        mBinding.setAmount(1);
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

        if (mType == TradeFragment.TYPE_MACH_POSITION_BUY_UP || mType == TradeFragment.TYPE_POSITION_BUY_UP) {
            ViewUtil.setSelect(mBinding.byUp, true);
            ViewUtil.setSelect(mBinding.byDown, false);
        } else {
            ViewUtil.setSelect(mBinding.byUp, false);
            ViewUtil.setSelect(mBinding.byDown, true);
        }


    }

    public void submitTrade() {
        if (mSelectGood == null) {
            return;
        }
        int stop = FormatUtil.getPureNum(mBinding.buyStopValue.getText().toString());
        int limit = FormatUtil.getPureNum(mBinding.buyLimitValue.getText().toString());
        String stopStr = stop == 0 ? "" : String.valueOf(stop );
        String limitStr = limit == 0 ? "" : String.valueOf(limit );



        if (mType <= TradeFragment.TYPE_POSITION_BUY_DOWN) {
            Observable<PositionData> observable = mTradeStorage.positionOpen(mSelectGood.goodsId, mType+1, mAmount, String.valueOf(mGoodType.price.close), limitStr, stopStr, null, 1);
            NetworkApi.ApiSubscribe(observable, machPositionData -> {
                if (machPositionData.hasError()) {
                    if (FailDealUtil.dealFail(getContext(), machPositionData.failed)) {
                        return;
                    }
                    ToastUtil.showToast(getContext(), machPositionData.error.usermsg);
                    return;
                }
                DialogItem dialogItem = new DialogItem();
                dialogItem.title = getResources().getString(R.string.position_open_succ);
                dialogItem.content = null;
                dialogItem.cancel = getResources().getString(R.string.position_view);
                dialogItem.cancel = getResources().getString(R.string.continu_trade);
                CommonFragmentDialog fragmentDialog = CommonFragmentDialog.newInstance(new IntentData<>(dialogItem));
                ZXFragmentJumpHelper.startFragment(getContext(), fragmentDialog, new CommonCallback() {
                    @Override
                    public void callback(Object item) {
                        //TODO
                        if (item == null) {
                            StockBusStation.viewPosition(getContext());
                        } else {
                            ;
                        }

                    }
                });

            });
        }else{

            float price = FormatUtil.getPureNum(mBinding.price.getText().toString());
            if(price==0){
                ToastUtil.showToast(getContext(),R.string.mach_price_remind);
                return;
            }
            Observable<MachPositionData> observable = mTradeStorage.machpositionOpen(mSelectGood.goodsId, mType-2, mAmount, price, limitStr, stopStr, null, 1);
            NetworkApi.ApiSubscribe(observable, machPositionData -> {
                if (machPositionData.hasError()) {
//                    if (FailDealUtil.dealFail(getContext(), machPositionData.failed)) {
//                        return;
//                    }
                    ToastUtil.showToast(getContext(), machPositionData.error.usermsg);
                    return;
                }
                DialogItem dialogItem = new DialogItem();
                dialogItem.title = getResources().getString(R.string.machposition_open_succ);
                dialogItem.content = null;
                dialogItem.cancel = getResources().getString(R.string.position_view);
                dialogItem.cancel = getResources().getString(R.string.continu_trade);
                CommonFragmentDialog fragmentDialog = CommonFragmentDialog.newInstance(new IntentData<>(dialogItem));
                ZXFragmentJumpHelper.startFragment(getContext(), fragmentDialog, new CommonCallback() {
                    @Override
                    public void callback(Object item) {
                        //TODO
                        if (item == null) {
                            StockBusStation.viewMachPosition(getContext());
                        } else {
                            ;
                        }

                    }
                });

            });
        }
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

    public void refreshVucher(){
        if(AccountManager.sharedInstance().getUserUni()==null){
            return;
        }
        mBinding.voucher.setVisibility(View.GONE);
        List<Voucher> myVouchers = AccountManager.sharedInstance().getUserUni().userVoucherList.getListItems();
        if(myVouchers==null){
            return;
        }
        List<Voucher> validVochers = new LinkedList<>();
        for(Voucher voucher:myVouchers){
            if(voucher.goods.goodsId.equals(mSelectGood.goodsId)){
                validVochers.add(voucher);
            }
        }
        if(validVochers.size()==0){
            mBinding.balance.setChecked(true);
            return;
        }
        mBinding.voucher.setVisibility(View.VISIBLE);
        String text = String.format(getResources().getString(R.string.format_voucher_amount),mSelectGood.depositFee,validVochers.size());
        mBinding.voucher.setText(text);
    }
}
