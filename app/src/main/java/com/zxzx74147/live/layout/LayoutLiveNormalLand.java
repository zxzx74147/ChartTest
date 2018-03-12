package com.zxzx74147.live.layout;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewEditorActionEvent;
import com.zxzx74147.charttest.R;
import com.zxzx74147.charttest.databinding.LayoutLiveNormalLandBinding;
import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.data.DialogItem;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.data.WheelSelectorData;
import com.zxzx74147.devlib.fragment.CommonFragmentDialog;
import com.zxzx74147.devlib.fragment.CommonWheelSelectorDialog;
import com.zxzx74147.devlib.modules.account.UserViewModel;
import com.zxzx74147.devlib.modules.busstation.StockBusStation;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.FormatUtil;
import com.zxzx74147.devlib.utils.KeyboardStatusDetector;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.utils.ZXFragmentJumpHelper;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.live.data.Msg;
import com.zxzx74147.live.data.MsgData;
import com.zxzx74147.live.stroage.LiveStorage;
import com.zxzx74147.live.viewmodel.LiveMsgViewModel;
import com.zxzx74147.profile.data.UserUniData;
import com.zxzx74147.stock.data.Good;
import com.zxzx74147.stock.data.GoodType;
import com.zxzx74147.stock.data.PositionData;
import com.zxzx74147.stock.storage.TradesStorage;
import com.zxzx74147.stock.util.FailDealUtil;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by zhengxin on 2018/3/10.
 */

public class LayoutLiveNormalLand extends FrameLayout {


    private LayoutLiveNormalLandBinding mBingding = null;
    private LiveMsgViewModel mMsgViewModel = null;
    private UserViewModel mModel = null;
    private CommonRecyclerViewAdapter<Msg> mAdapter = null;
    private LiveStorage mLiveStorage = RetrofitClient.getClient().create(LiveStorage.class);
    private boolean mIsTouchint = false;
    private KeyboardStatusDetector mDetector = new KeyboardStatusDetector();
    private UserUniData mUserUniData = null;
    private GoodType mSelectGoodType = null;
    private Good mSelectGood = null;
    private TradesStorage mTradeStorage = RetrofitClient.getClient().create(TradesStorage.class);


    public LayoutLiveNormalLand(Context context) {
        super(context);
        init();
    }

    public LayoutLiveNormalLand(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LayoutLiveNormalLand(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {

        mModel = ViewModelProviders.of((FragmentActivity) getContext()).get(UserViewModel.class);
        mUserUniData = mModel.getUserUniLiveData().getValue();
        mModel.getUserUniLiveData().observe((FragmentActivity) getContext(), new Observer<UserUniData>() {
            @Override
            public void onChanged(@Nullable UserUniData userUniData) {
                mUserUniData = userUniData;
            }
        });




        mMsgViewModel = ViewModelProviders.of((FragmentActivity) getContext()).get(LiveMsgViewModel.class);
        mBingding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_live_normal_land, this, true);

        mAdapter = new CommonRecyclerViewAdapter<>(null);
        mBingding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter.setMultiTypeDelegate(new CommonMultiTypeDelegate());
        mBingding.list.setAdapter(mAdapter);
        mBingding.bubble.setDefaultDrawableList();


        mMsgViewModel.getLiveMsgListLiveData().observe((BaseActivity) getContext(), liveMsgListData -> {
            if (liveMsgListData.hasError()) {
                return;
            }
            mBingding.setLiveMsg(liveMsgListData);
            if (mIsTouchint) {
                return;
            }
            mAdapter.setNewData(liveMsgListData.msgList.msg);
            mBingding.bubble.startAnimation(mBingding.bubble.getWidth() / 2, mBingding.bubble.getHeight() - getResources().getDimensionPixelSize(R.dimen.default_gap_100), 2);

        });


        RxTextView.editorActionEvents(mBingding.commentEdit).subscribe(new Consumer<TextViewEditorActionEvent>() {
            @Override
            public void accept(TextViewEditorActionEvent textViewEditorActionEvent) throws Exception {
                if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_SEND) {
                    doSendComment();
                }
            }
        });
        RxView.clicks(mBingding.comment).subscribe(v -> {
            showComment();
        });
        mDetector.registerActivity((Activity) getContext());
        mDetector.setmVisibilityListener(new KeyboardStatusDetector.KeyboardVisibilityListener() {
            @Override
            public void onVisibilityChanged(boolean keyboardVisible) {
                if (!keyboardVisible) {
                    ViewUtil.hideSoftPad(mBingding.commentEdit);
                    mBingding.commentEdit.setVisibility(View.GONE);
                }
            }
        });

        RxView.clicks(mBingding.openPosition).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                if (mBingding.tinyTrade.getRoot().getVisibility() == View.GONE) {
                    mBingding.tinyTrade.getRoot().setVisibility(View.VISIBLE);
                    ViewUtil.setSelect(mBingding.openPosition, true);
                    mBingding.tinyTrade.getRoot().bringToFront();
                } else {
                    mBingding.tinyTrade.getRoot().setVisibility(View.GONE);
                    ViewUtil.setSelect(mBingding.openPosition, false);
                }
            }
        });


        //TINY
        if (mUserUniData != null && mUserUniData.goodsTypeList.num > 0) {
            GoodType goodType = mUserUniData.goodsTypeList.goodType.get(0);
            mBingding.tinyTrade.setData(goodType);
            mSelectGoodType = goodType;
            if (goodType.goods != null && goodType.goods.size() > 0) {
                mBingding.tinyTrade.setGood(goodType.goods.get(0));
                mSelectGood= goodType.goods.get(0);
            }
            mBingding.tinyTrade.setAmount(1);
        }

        RxView.clicks(mBingding.tinyTrade.price).subscribe(v->{

            if(mSelectGoodType==null){
                return;
            }
            List<String> list = new LinkedList<>();
            for(Good good:mSelectGoodType.goods){
                list.add(String.format("%.1f元/手",good.depositFee));
            }
            WheelSelectorData data = new WheelSelectorData();
            data.items = list;
            data.offset = 0;
            CommonWheelSelectorDialog dialog = CommonWheelSelectorDialog.newInstance(new IntentData<>(data));
            ZXFragmentJumpHelper.startFragment(getContext(), dialog, new CommonCallback() {
                @Override
                public void callback(Object item) {
                    if(item==null){
                        return;
                    }
                    mSelectGood = mSelectGoodType.goods.get((Integer) item);
                    mBingding.tinyTrade.setGood(mSelectGood);
                }
            });
        });


        RxView.clicks(mBingding.tinyTrade.amount).subscribe(v->{

            if(mSelectGoodType==null){
                return;
            }
            List<String> list = new LinkedList<>();
            for(int i=1;i<11;i++){
                list.add(String.format(getResources().getString(R.string.format_lot),i));
            }
            WheelSelectorData data = new WheelSelectorData();
            data.items = list;
            data.offset = 0;
            CommonWheelSelectorDialog dialog = CommonWheelSelectorDialog.newInstance(new IntentData<>(data));
            ZXFragmentJumpHelper.startFragment(getContext(), dialog, new CommonCallback() {
                @Override
                public void callback(Object item) {
                    if(item==null){
                        return;
                    }
                    mBingding.tinyTrade.setAmount((Integer)item+1);
                }
            });
        });


        RxView.clicks(mBingding.tinyTrade.goodLayout).subscribe(v->{

            if(mUserUniData==null){
                return;
            }
            List<String> list = new LinkedList<>();
            for(GoodType goodType:mUserUniData.goodsTypeList.goodType){
                list.add(goodType.goodsTypeName);
            }
            WheelSelectorData data = new WheelSelectorData();
            data.items = list;
            data.offset = 0;
            CommonWheelSelectorDialog dialog = CommonWheelSelectorDialog.newInstance(new IntentData<>(data));
            ZXFragmentJumpHelper.startFragment(getContext(), dialog, new CommonCallback() {
                @Override
                public void callback(Object item) {
                    if(item==null){
                        return;
                    }
                    mSelectGoodType = mUserUniData.goodsTypeList.goodType.get((Integer) item);

                    if (mSelectGoodType.goods != null && mSelectGoodType.goods.size() > 0) {
                        mBingding.tinyTrade.setGood(mSelectGoodType.goods.get(0));
                        mSelectGood= mSelectGoodType.goods.get(0);
                    }
                    mBingding.tinyTrade.setGood(mSelectGood);
                    mBingding.tinyTrade.setData(mSelectGoodType);
                    mBingding.tinyTrade.setAmount(1);
                }
            });
        });

        RxView.clicks(mBingding.tinyTrade.byUp).subscribe(v->{
            doTrade(1);
        });

        RxView.clicks(mBingding.tinyTrade.byDown).subscribe(v->{
            doTrade(2);
        });


    }

    public void doTrade(int type){
        if(mSelectGood==null){
            return;
        }
        Observable<PositionData> observable = mTradeStorage.positionOpen(mSelectGood.goodsId, type, mBingding.tinyTrade.getAmount(), String.valueOf(mSelectGoodType.price.close), "0", "0", "0", 1);
        NetworkApi.ApiSubscribe(observable, machPositionData -> {
            if (machPositionData.hasError()) {
                if (FailDealUtil.dealFail(getContext(), machPositionData.failed)) {
                    return;
                }
                ToastUtil.showToast(getContext(), machPositionData.error.usermsg);
                return;
            }
            ToastUtil.showToast(getContext(), R.string.succ);
            mBingding.tinyTrade.getRoot().setVisibility(View.GONE);
            ViewUtil.setSelect(mBingding.openPosition, false);

        });
    }

    public void showComment() {
        mBingding.commentEdit.setVisibility(View.VISIBLE);
        ViewUtil.showSoftPad(mBingding.commentEdit);
    }

    private void doSendComment() {
        String comment = mBingding.commentEdit.getText().toString();
        if (TextUtils.isEmpty(comment)) {
            ToastUtil.showToast(getContext(), "请输入");
            return;
        }

        NetworkApi.ApiSubscribe(((BaseActivity) getContext()).getLifecycle(), mLiveStorage.msgSend(mMsgViewModel.getLive().liveId, comment), new Consumer<MsgData>() {
            @Override
            public void accept(MsgData msgData) throws Exception {
                if (msgData.hasError()) {
                    ToastUtil.showToast(getContext(), msgData.error.usermsg);
                    return;
                }
                ViewUtil.hideSoftPad(mBingding.commentEdit);
                mBingding.commentEdit.setText("");
                mBingding.commentEdit.setVisibility(View.GONE);

            }
        }, MsgData.class);

    }

}
