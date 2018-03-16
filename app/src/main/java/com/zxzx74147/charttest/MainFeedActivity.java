package com.zxzx74147.charttest;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.charttest.databinding.ActivityMainFeedBinding;
import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.data.DialogItem;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.fragment.CommonFragmentDialog;
import com.zxzx74147.devlib.modules.account.AccountManager;
import com.zxzx74147.devlib.modules.account.UserViewModel;
import com.zxzx74147.devlib.modules.busstation.LiveBusStation;
import com.zxzx74147.devlib.modules.busstation.MainBusStation;
import com.zxzx74147.devlib.modules.sys.SysInitManager;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.utils.ZXFragmentJumpHelper;
import com.zxzx74147.live.data.HomeData;
import com.zxzx74147.live.data.Live;
import com.zxzx74147.live.stroage.LiveStorage;
import com.zxzx74147.stock.data.GoodType;
import com.zxzx74147.stock.fragment.StockFragment;

import io.reactivex.functions.Consumer;

public class MainFeedActivity extends BaseActivity {

    private ActivityMainFeedBinding mBinding = null;
    private UserViewModel mUserViewModel = null;

    private GestureDetectorCompat mGestureDetector;
    private GestureDetector.OnGestureListener mOnGestureListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (velocityX < 0) {
                if (SysInitManager.sharedInstance().getSysInitData().swich.liveOpen != 0) {
//                    MainBusStation.toLive(MainFeedActivity.this);
                    checkLive(MainFeedActivity.this);
                }
            }
            return true;
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        this.mGestureDetector.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }


    public static void startMainActivity(Context context) {
        Intent intent = new Intent(context, MainFeedActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main_feed);
        mBinding.goodList.setCallback(mCallback);
        mBinding.goodList.setProvider(ViewModelProviders.of(this));
        mBinding.goodList.setLifeCircle(this);
        mBinding.setUser(AccountManager.sharedInstance().getUser());
        ViewUtil.startAnimition(mBinding.toLive);
        initView();
        initData();
    }

    private CommonCallback<GoodType> mCallback = item -> {
        StockFragment fragment = StockFragment.newInstance(item);
        fragment.show(getSupportFragmentManager(), fragment.getTag());

//        ZXFragmentJumpHelper.startFragment(this, ProfileFragment.class, null);
    };

    private void initView() {

//        mHeaderBinding= mBinding.headLayout;
//        mHeaderBinding =(LayoutLiveHeadBinding)mBinding.headLayout;
//        RxView.clicks(mBinding.headLayout.assetTotal).subscribe(o -> ZXFragmentJumpHelper.startFragment(MainActivity.this, ProfileFragment.class,null));
        mGestureDetector = new GestureDetectorCompat(this, mOnGestureListener);
        RxView.clicks(mBinding.sendFeed).subscribe(o -> {
            DialogItem item = new DialogItem();
            item.title = getResources().getString(R.string.send_remind);
            item.cancel = null;
            item.ok = getResources().getString(com.zxzx74147.balance.R.string.i_know);
            CommonFragmentDialog dialog = CommonFragmentDialog.newInstance(new IntentData<>(item));
            ZXFragmentJumpHelper.startFragment(MainFeedActivity.this, dialog, new CommonCallback() {
                @Override
                public void callback(Object item) {

                    return;
                }
            });
        });


    }

    private void initData() {
        mUserViewModel = ViewModelProviders.of(MainFeedActivity.this).get(UserViewModel.class);
        mUserViewModel.getUserUniLiveData().observe(MainFeedActivity.this, userUniData -> {
            if (userUniData.hasError()) {
                return;
            }
            mBinding.setUser(userUniData.user);
        });
    }

    public static void checkLive(Context context) {
        LiveStorage mStorage = RetrofitClient.getClient().create(LiveStorage.class);
        NetworkApi.ApiSubscribe(mStorage.roomGetList(), new Consumer<HomeData>() {
            @Override
            public void accept(HomeData homeData) throws Exception {
                if (homeData.hasError()) {
                    ToastUtil.showToast(context, homeData.error.usermsg);
                    return;
                }
                if (homeData.liveList != null) {
                    for (Live live : homeData.liveList.live) {
                        if (live.status == 2) {
                            LiveBusStation.startLive(context, live);
                            return;
                        }
                    }
                }
                MainBusStation.toLive(context);
            }
        });
    }


}