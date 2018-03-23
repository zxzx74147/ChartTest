package com.zxzx74147.stock.widget;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
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
import com.zxzx74147.profile.data.ComVoucher;
import com.zxzx74147.profile.data.Voucher;
import com.zxzx74147.stock.R;
import com.zxzx74147.stock.data.Good;
import com.zxzx74147.stock.data.GoodType;
import com.zxzx74147.stock.data.MachPosition;
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
    private MachPosition mMachPosition = null;
    private TradeFragment fragment;
    private boolean mIsComVoucherShow = false;


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

    @BindingAdapter({"fragment"})
    public static void setUser(TradeWidget view, TradeFragment fragment) {
        view.setFragment(fragment);
    }

    private void setFragment(TradeFragment fragment) {
        this.fragment = fragment;
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
            } else {
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
            } else {
                mType = TradeFragment.TYPE_MACH_POSITION_BUY_DOWN;
            }
            mBinding.setType(mType);
        });

        RxTabLayout.selectionEvents(mBinding.listType).subscribe(tabLayoutSelectionEvent -> {
            Good good = (Good) tabLayoutSelectionEvent.tab().getTag();
            mSelectGood = good;
            mBinding.setGood(mSelectGood);
            refreshVucher();
            refreshAmount();
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
            data.items = FormatUtil.getPointCal(mSelectGood, mAmount);
            offset = offset==0? 0:offset-6;
            offset = Math.max(0,offset);
            data.offset = offset;
            CommonWheelSelectorDialog dialog = CommonWheelSelectorDialog.newInstance(new IntentData<>(data));
            ZXFragmentJumpHelper.startFragment(getContext(), dialog, new CommonCallback() {
                @Override
                public void callback(Object item) {
                    if (item == null) {
                        return;
                    }
                    int sel = (int) item;
                    mBinding.buyStopValue.setText((sel==0? 0:sel+6) + "点");
                }
            });
        });

        RxView.clicks(mBinding.buyLimitValue).subscribe(a -> {
            int offset = FormatUtil.getPureNum(mBinding.buyLimitValue.getText().toString());
            WheelSelectorData data = new WheelSelectorData();
            data.items = FormatUtil.getPointCal(mSelectGood, mAmount);
            offset = offset==0? 0:offset-6;
            offset = Math.max(0,offset);
            data.offset = offset;
            CommonWheelSelectorDialog dialog = CommonWheelSelectorDialog.newInstance(new IntentData<>(data));
            ZXFragmentJumpHelper.startFragment(getContext(), dialog, new CommonCallback() {
                @Override
                public void callback(Object item) {
                    if (item == null) {
                        return;
                    }
                    int sel = (int) item;
                    mBinding.buyLimitValue.setText((sel==0? 0:sel+6) + "点");
                }
            });
        });

        RxCompoundButton.checkedChanges(mBinding.balance).subscribe(isChecked -> {
            if (!isChecked && mBinding.voucher.getVisibility() == View.GONE) {
                mBinding.balance.setChecked(true);
                return;
            }
            mBinding.voucher.setChecked(!isChecked);
        });

        RxCompoundButton.checkedChanges(mBinding.voucher).subscribe(isChecked -> {
            mBinding.balance.setChecked(!isChecked);
            if (mIsComVoucherShow) {
                onComVucherChecked(isChecked);
            } else {
                onVucherChecked(isChecked);
            }
        });

//        RxView.clicks(mBinding.machRemind).subscribe(v -> {
//            int offset = FormatUtil.getPureNum(mBinding.machRemind.getText().toString());
//            WheelSelectorData data = new WheelSelectorData();
//            data.items = FormatUtil.POINT_LIST;
//            data.offset = offset;
//            CommonWheelSelectorDialog dialog = CommonWheelSelectorDialog.newInstance(new IntentData<>(data));
//            ZXFragmentJumpHelper.startFragment(getContext(), dialog, new CommonCallback() {
//                @Override
//                public void callback(Object item) {
//                    if (item == null) {
//                        return;
//                    }
//                    mBinding.buyStopValue.setText(data.items.get((Integer) item));
//                }
//            });
//        });


    }

    public void setCallback(CommonCallback<GoodType> callback) {

    }

    public void setGood(GoodType good) {
        if (good.equals(mGoodType)) {
            mGoodType = good;
            return;
        }
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

        float total = mAmount * mSelectGood.depositFee+(mBinding.voucher.isChecked()? 0:mAmount*mSelectGood.openChargeFee);

        mBinding.setTotal(total);
    }

    public void refreshData() {
        mIsComVoucherShow = false;
        if (mGoodType == null) {
            return;
        }
        if (mGoodType.sellUserNum + mGoodType.buyUserNum != 0) {
            int upPer = mGoodType.buyUserNum * 100 / (mGoodType.buyUserNum + mGoodType.sellUserNum);
            mBinding.upPer.setText(upPer + "%人选择");
            mBinding.downPer.setText((100 - upPer) + "%人选择");
        }

        mBinding.price.setText("");
        mBinding.setType(mType);
        mBinding.setAmount(1);
        mBinding.listType.removeAllTabs();
        mBinding.listAmount.removeAllTabs();
        for (Good good : mGoodType.goods) {
            String format = "%.0f元/手";
            String temp = String.format(format,good.depositFee);
//            String temp = (String.valueOf(good.depositFee) + "元/手");
            TabLayout.Tab newTab = mBinding.listType.newTab();
            newTab.setText(temp);
            newTab.setTag(good);
            mBinding.listType.addTab(newTab.setText(temp));

        }
        if (mGoodType.goods != null && mGoodType.goods.size() > 0) {
            mSelectGood = mGoodType.goods.get(0);
            mBinding.setGood(mSelectGood);
        }

        for (int i = mSelectGood.minLot; i <= mSelectGood.maxLot; i++) {
            String temp = i + "手";
            TabLayout.Tab newTab = mBinding.listAmount.newTab();
            newTab.setText(temp);
            newTab.setTag(i);
            mBinding.listAmount.addTab(newTab);
        }
        mAmount = mSelectGood.minLot;
        mBinding.listAmount.getTabAt(0).select();
        mBinding.listAmount.post(new Runnable() {
            @Override
            public void run() {
                mBinding.listAmount.setSmoothScrollingEnabled(false);
                mBinding.listAmount.setScrollX(0);
            }
        });

        refreshAmount();

        if (mType == TradeFragment.TYPE_MACH_POSITION_BUY_UP || mType == TradeFragment.TYPE_POSITION_BUY_UP) {
            ViewUtil.setSelect(mBinding.byUp, true);
            ViewUtil.setSelect(mBinding.byDown, false);
        } else {
            ViewUtil.setSelect(mBinding.byUp, false);
            ViewUtil.setSelect(mBinding.byDown, true);
        }
        refreshVucher();


    }

    public void submitTrade() {
        if (mSelectGood == null) {
            return;
        }
        int stop = FormatUtil.getPureNum(mBinding.buyStopValue.getText().toString());
        int limit = FormatUtil.getPureNum(mBinding.buyLimitValue.getText().toString());
        String stopStr = stop == 0 ? "" : String.valueOf(stop);
        String limitStr = limit == 0 ? "" : String.valueOf(limit);

        if (mMachPosition != null) {
            double test = FormatUtil.getPureDoubleNum(mBinding.price.getText().toString());
            String price=mBinding.price.getText().toString();
//            String price = FormatUtil.getPureDoubleNum(mBinding.price.getText().toString());
            if (test == 0) {
                ToastUtil.showToast(getContext(), R.string.mach_price_remind);
                return;
            }
            float error = FormatUtil.getPureFloatNum(mBinding.machRemind.getText().toString());
            if(TextUtils.isEmpty(mBinding.machRemind.getText().toString())){
                ToastUtil.showToast(getContext(),"请输入允许误差");
                return;
            }
            Observable<MachPositionData> observable = mTradeStorage.machpositionModify(mMachPosition.machPositionId, mSelectGood.goodsId, mType - 2, mAmount, price, limitStr, stopStr, String.valueOf(error), 1);
            NetworkApi.ApiSubscribe(ViewUtil.getLivecirceOwer(this), observable, true, machPositionData -> {
                if (machPositionData.hasError()) {
                    if (FailDealUtil.dealFail(getContext(), machPositionData.failed)) {
                        return;
                    }
                    ToastUtil.showToast(getContext(), machPositionData.error.usermsg);
                    return;
                }
                AccountManager.sharedInstance().doRefresh();
                DialogItem dialogItem = new DialogItem();
                dialogItem.title = getResources().getString(R.string.machposition_motify_succ);
                dialogItem.content = null;
                dialogItem.cancel = getResources().getString(R.string.position_view);
                CommonFragmentDialog fragmentDialog = CommonFragmentDialog.newInstance(new IntentData<>(dialogItem));
                ZXFragmentJumpHelper.startFragment(getContext(), fragmentDialog, new CommonCallback() {
                    @Override
                    public void callback(Object item) {
                        if (fragment != null) {
                            fragment.dismiss();
                            if (fragment.mCallback != null) {
                                fragment.mCallback.callback(machPositionData);
                            }
                        }
                    }
                });

            }, MachPositionData.class);
        } else if (mBinding.voucher.isChecked()) {
            if (mIsComVoucherShow) {
                Observable<PositionData> observable = mTradeStorage.comvoucherOpen(mSelectGood.goodsType, mType + 1, limitStr, stopStr);
                NetworkApi.ApiSubscribe(ViewUtil.getLivecirceOwer(this), observable, true, positionData -> {
                    if (positionData.hasError()) {
                        if (FailDealUtil.dealFail(getContext(), positionData.failed)) {
                            return;
                        }
                        ToastUtil.showToast(getContext(), positionData.error.usermsg);
                        return;
                    }
                    DialogItem dialogItem = new DialogItem();
                    dialogItem.title = getResources().getString(R.string.position_open_succ);
                    dialogItem.content = null;
                    dialogItem.cancel = getResources().getString(R.string.position_view);
                    dialogItem.ok = getResources().getString(R.string.continu_trade);
                    dialogItem.obj = positionData;
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
                }, PositionData.class);
            } else {
                Observable<PositionData> observable = mTradeStorage.voucherOpen(mSelectGood.goodsId, mType + 1, mAmount, String.valueOf(mGoodType.price.curPrice), limitStr, stopStr, null, 0);
                NetworkApi.ApiSubscribe(ViewUtil.getLivecirceOwer(this), observable, true, positionData -> {
                    if (positionData.hasError()) {
                        if (FailDealUtil.dealFail(getContext(), positionData.failed)) {
                            return;
                        }
                        ToastUtil.showToast(getContext(), positionData.error.usermsg);
                        return;
                    }
                    AccountManager.sharedInstance().doRefresh();
                    DialogItem dialogItem = new DialogItem();
                    dialogItem.title = getResources().getString(R.string.position_open_succ);
                    dialogItem.content = null;
                    dialogItem.cancel = getResources().getString(R.string.position_view);
                    dialogItem.ok = getResources().getString(R.string.continu_trade);
                    dialogItem.obj = positionData;
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
                }, PositionData.class);
            }
        } else if (mType <= TradeFragment.TYPE_POSITION_BUY_DOWN) {
            Observable<PositionData> observable = mTradeStorage.positionOpen(mSelectGood.goodsId, mType + 1, mAmount, String.valueOf(mGoodType.price.curPrice), limitStr, stopStr, null, 1);
            NetworkApi.ApiSubscribe(ViewUtil.getLivecirceOwer(this), observable, true, positionData -> {
                if (positionData.hasError()) {
                    if (FailDealUtil.dealFail(getContext(), positionData.failed)) {
                        return;
                    }
                    ToastUtil.showToast(getContext(), positionData.error.usermsg);
                    return;
                }
                AccountManager.sharedInstance().doRefresh();
                DialogItem dialogItem = new DialogItem();
                dialogItem.title = getResources().getString(R.string.position_open_succ);
                dialogItem.content = null;
                dialogItem.cancel = getResources().getString(R.string.position_view);
                dialogItem.ok = getResources().getString(R.string.continu_trade);
                dialogItem.obj = positionData;
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
            }, PositionData.class);
        } else {

            double test = FormatUtil.getPureDoubleNum(mBinding.price.getText().toString());
            String price=mBinding.price.getText().toString();
//            String price = FormatUtil.getPureDoubleNum(mBinding.price.getText().toString());
            if (test == 0) {
                ToastUtil.showToast(getContext(), R.string.mach_price_remind);
                return;
            }
            if(TextUtils.isEmpty(mBinding.machRemind.getText().toString())){
                ToastUtil.showToast(getContext(),"请输入允许误差");
                return;
            }
            float error = FormatUtil.getPureFloatNum(mBinding.machRemind.getText().toString());
            Observable<MachPositionData> observable = mTradeStorage.machpositionOpen(mSelectGood.goodsId, mType - 2, mAmount, price, limitStr, stopStr, String.valueOf(error), 1);
            NetworkApi.ApiSubscribe(ViewUtil.getLivecirceOwer(this), observable, true, machPositionData -> {
                if (machPositionData.hasError()) {
                    if (FailDealUtil.dealFail(getContext(), machPositionData.failed)) {
                        return;
                    }
                    ToastUtil.showToast(getContext(), machPositionData.error.usermsg);
                    return;
                }
                AccountManager.sharedInstance().doRefresh();
                DialogItem dialogItem = new DialogItem();
                dialogItem.title = getResources().getString(R.string.machposition_open_succ);
                dialogItem.content = null;
                dialogItem.cancel = getResources().getString(R.string.machposition_view);
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

            }, MachPositionData.class);
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

    public void refreshVucher() {

        if (AccountManager.sharedInstance().getUserUni() == null) {
            return;
        }
        if (mIsComVoucherShow) {
            return;
        }
        mIsComVoucherShow = false;
        mBinding.balance.setChecked(true);
        mBinding.voucher.setVisibility(View.GONE);
        mBinding.setVoucher(false);
        if (mType != TradeFragment.TYPE_POSITION_BUY_UP && mType != TradeFragment.TYPE_POSITION_BUY_DOWN) {
            return;
        }

        if (mType > TradeFragment.TYPE_POSITION_BUY_DOWN) {
            return;
        }
        ViewUtil.enableTabLayout(mBinding.listAmount);
        ViewUtil.enableTabLayout(mBinding.listType);
        mBinding.buyLimitValue.setClickable(true);
        mBinding.buyStopValue.setClickable(true);

        ComVoucher comVoucher = AccountManager.sharedInstance().getUserUni().userComVoucherInfo;
        if (comVoucher != null) {
            if (comVoucher.applyGoodsType != null) {
                for (GoodType type : comVoucher.applyGoodsType) {
                    if (type.goodsType.equals(mSelectGood.goodsType)) {
                        mBinding.voucher.setVisibility(View.VISIBLE);
                        mBinding.voucher.setText(comVoucher.name);
                        mBinding.voucher.setChecked(false);
                        mIsComVoucherShow = true;
                        return;
                    }
                }
            }

        }

        List<Voucher> myVouchers = AccountManager.sharedInstance().getUserUni().userVoucherList.getListItems();
        if (myVouchers == null) {
            return;
        }

        List<Voucher> validVochers = new LinkedList<>();
        for (Voucher voucher : myVouchers) {
            if (voucher.goods.goodsId.equals(mSelectGood.goodsId)) {
                validVochers.add(voucher);
            }
        }
        if (validVochers.size() == 0) {
            mBinding.balance.setChecked(true);
            return;
        }

        mBinding.voucher.setVisibility(View.VISIBLE);
        String text = String.format(getResources().getString(R.string.format_voucher_amount), mSelectGood.depositFee, validVochers.size());
        mBinding.voucher.setText(text);
        mBinding.voucher.setChecked(false);
    }

    public void onComVucherChecked(boolean check) {
        mBinding.setVoucher(check);
        ComVoucher comVoucher = AccountManager.sharedInstance().getUserUni().userComVoucherInfo;
        if (check) {
            int vFee = (int) comVoucher.depositFee;
            int count = 0;
            for (Good good : mGoodType.goods) {
                int gFee = (int) good.depositFee;
                if (gFee > 0) {
                    if (vFee % gFee == 0) {
                        mBinding.buyLimitValue.setText("10点");
                        mBinding.buyStopValue.setText("10点");
                        mBinding.listAmount.getTabAt(vFee / gFee - 1).select();
                        mBinding.listAmount.setScrollX(0);
                        mBinding.listType.getTabAt(count).select();
                        ViewUtil.disableTabLayout(mBinding.listAmount);
                        ViewUtil.disableTabLayout(mBinding.listType);
                        count++;
                    }
                }
            }
        } else {
            mBinding.buyLimitValue.setText("无");
            mBinding.buyStopValue.setText("无");
            ViewUtil.enableTabLayout(mBinding.listAmount);
            ViewUtil.enableTabLayout(mBinding.listType);
        }
        refreshAmount();
    }

    public void onVucherChecked(boolean check) {

        mBinding.setVoucher(check);
        if (check) {
            mBinding.buyLimitValue.setText("10点");
            mBinding.buyStopValue.setText("10点");
            mBinding.buyLimitValue.setClickable(false);
            mBinding.buyStopValue.setClickable(false);
            if (mBinding.listAmount.getTabCount() > 0) {
                mBinding.listAmount.getTabAt(0).select();
                mBinding.listAmount.setScrollX(0);
            }
            ViewUtil.disableTabLayout(mBinding.listAmount);
        } else {
            mBinding.buyLimitValue.setText("无");
            mBinding.buyStopValue.setText("无");
            mBinding.buyLimitValue.setClickable(true);
            mBinding.buyStopValue.setClickable(true);
            ViewUtil.enableTabLayout(mBinding.listAmount);
        }
        refreshAmount();
    }

    //    private GoodType  mGoodType;
//    public void setGoodType(GoodType goodType) {
//        mGoodType = goodType;
//    }
    public void setMachPosition(MachPosition machPosition) {
        mMachPosition = machPosition;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mBinding.machRemind.setText(String.format("%.0f", mMachPosition.error));
                mBinding.price.setText(String.format("%.1f",machPosition.price));
                mBinding.buyLimitValue.setText(String.format("%.0f点", machPosition.limit));
                mBinding.buyStopValue.setText(String.format("%.0f点", machPosition.stop));
                GoodType goodType = mGoodType;
                int i = 0;
                for (Good good : goodType.goods) {
                    if (good.goodsId.equals(machPosition.goodsId)) {
                        mSelectGood = good;
                        mBinding.listAmount.getTabAt(i).select();
                        break;
                    }
                    i++;
                }
                mAmount = (int) machPosition.amount;
                mBinding.listAmount.getTabAt(mAmount - 1).select();
                mType = machPosition.buySell + 2;
                mBinding.setType(mType);

                if (mType == TradeFragment.TYPE_MACH_POSITION_BUY_DOWN) {
                    ViewUtil.setSelect(mBinding.byUp, false);
                    ViewUtil.setSelect(mBinding.byDown, true);
                } else {
                    ViewUtil.setSelect(mBinding.byUp, true);
                    ViewUtil.setSelect(mBinding.byDown, false);
                }
            }
        }, 50);


    }
}
