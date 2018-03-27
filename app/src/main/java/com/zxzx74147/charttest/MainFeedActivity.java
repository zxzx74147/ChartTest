package com.zxzx74147.charttest;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;

import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.charttest.databinding.ActivityMainFeedBinding;
import com.zxzx74147.devlib.DevLib;
import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.data.DialogItem;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.fragment.CommonFragmentDialog;
import com.zxzx74147.devlib.json.JsonHelper;
import com.zxzx74147.devlib.kvstore.KVStore;
import com.zxzx74147.devlib.modules.account.AccountManager;
import com.zxzx74147.devlib.modules.account.UserViewModel;
import com.zxzx74147.devlib.modules.busstation.LiveBusStation;
import com.zxzx74147.devlib.modules.busstation.MainBusStation;
import com.zxzx74147.devlib.modules.busstation.StockBusStation;
import com.zxzx74147.devlib.modules.sys.SysInitManager;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.umeng.UmengAction;
import com.zxzx74147.devlib.umeng.UmengAgent;
import com.zxzx74147.devlib.utils.DisplayUtil;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.utils.ZXFragmentJumpHelper;
import com.zxzx74147.live.data.HomeData;
import com.zxzx74147.live.data.Live;
import com.zxzx74147.live.data.Rtmp;
import com.zxzx74147.live.data.RtmpList;
import com.zxzx74147.live.data.TeacherList;
import com.zxzx74147.live.stroage.LiveStorage;
import com.zxzx74147.profile.data.ComVoucher;
import com.zxzx74147.profile.data.UnReadManager;
import com.zxzx74147.profile.databinding.LayoutComVoucherBinding;
import com.zxzx74147.profile.util.ComVoucherUtil;
import com.zxzx74147.stock.data.GoodType;
import com.zxzx74147.stock.fragment.StockFragment;
import com.zxzx74147.stock.fragment.TradeFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.interfaces.RSAPublicKey;
import java.util.HashSet;
import java.util.LinkedList;

import io.reactivex.functions.Consumer;
@SuppressLint("CheckResult")
public class MainFeedActivity extends BaseActivity {

    private ActivityMainFeedBinding mBinding = null;
    private UserViewModel mUserViewModel = null;

    private GestureDetectorCompat mGestureDetector;
    private MotionEvent mLastOnDownEvent = null;
    private GestureDetector.OnGestureListener mOnGestureListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            mLastOnDownEvent = e;
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
            if(e1==null){
                e1 = mLastOnDownEvent;
            }
            if(e1==null||e2==null){
                return true;
            }
            if (velocityX > 0&&Math.abs(e1.getX()-e2.getX())>2*Math.abs(e1.getY()-e2.getY())&&Math.abs(e1.getX()-e2.getX())>DisplayUtil.getDisplayMetrics().widthPixels/3) {
                if (SysInitManager.sharedInstance().getSysInitData().swich.liveOpen != 0) {
                    checkLive(MainFeedActivity.this);
                }
            }
            return true;
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(event.getY()< DisplayUtil.getDisplayMetrics().heightPixels*3/4) {
            this.mGestureDetector.onTouchEvent(event);
        }
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
        UmengAgent.onEvent(UmengAction.ALUmengPageFeedDetail);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main_feed);
        mBinding.goodList.setCallback(mCallback);
        mBinding.goodList.setProvider(ViewModelProviders.of(this));
        mBinding.goodList.setLifeCircle(this);
        mBinding.setUser(AccountManager.sharedInstance().getUser());
//        ViewUtil.startAnimition(mBinding.toLive);
        initView();
        initData();

    }

    private CommonCallback<GoodType> mCallback = item -> {
        StockFragment fragment = StockFragment.newInstance(item);
        fragment.show(getSupportFragmentManager(), fragment.getTag());

//        ProfileBusStation.startTradePasswordMotify(this);

//        ZXFragmentJumpHelper.startFragment(this, ProfileFragment.class, null);
    };

    private void initView() {
        mBinding.tabLayout.getTabAt(1).select();
        ViewUtil.disableTabLayout(mBinding.tabLayout);
        RxView.clicks(mBinding.mask).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                checkLive(MainFeedActivity.this);
            }
        });
//        mHeaderBinding= mBinding.headLayout;
//        mHeaderBinding =(LayoutLiveHeadBinding)mBinding.headLayout;
//        RxView.clicks(mBinding.headLayout.assetTotal).subscribe(o -> ZXFragmentJumpHelper.startFragment(MainActivity.this, ProfileFragment.class,null));
        mGestureDetector = new GestureDetectorCompat(this, mOnGestureListener);
        RxView.clicks(mBinding.sendFeed).subscribe(o -> {
            DialogItem item = new DialogItem();
            item.title = getResources().getString(R.string.remind);
            item.content=getResources().getString(R.string.send_remind);
            item.cancel = null;
            item.ok = getResources().getString(com.zxzx74147.balance.R.string.i_know);
            UmengAgent.onEvent(UmengAction.ALUmengPagePostAlert);
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
            if (userUniData!=null&&userUniData.hasError()) {
                return;
            }
            mBinding.setUserUni(userUniData);
            mBinding.setUser(userUniData.user);
            mBinding.setUnRead(UnReadManager.sharedInstance().getUnReadNum());
            if(userUniData.userComVoucherInfo!=null){
                ComVoucherUtil.showComVoucher(MainFeedActivity.this,userUniData.userComVoucherInfo);
            }
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

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.tabLayout.getTabAt(1).select();
    }

    public static String  loadString(InputStream in){
        try {
            BufferedReader br= new BufferedReader(new InputStreamReader(in));
            String readLine= null;
            StringBuilder sb= new StringBuilder();
            while((readLine= br.readLine())!=null){
                if(readLine.charAt(0)=='-'){
                    continue;
                }else{
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            return sb.toString();
        } catch (IOException e) {

        } catch (NullPointerException e) {

        }
        return null;
    }
//    private void startLiveTest(){
//        InputStream stream = DevLib.getApp().getResources().openRawResource(R.raw.live);
//        String str = loadString(stream);
//        HomeData home = JsonHelper.convertJson(str,HomeData.class);
//        Live live = home.liveList.live.get(0);
//        live.liveId=1;
//        live.rtmpList = new RtmpList();
//        live.rtmpList.num = 2;
//        live.rtmpList.rtmp = new LinkedList<>();
//        Rtmp rtmp1 = new Rtmp();
//        Rtmp rtmp2 = new Rtmp();
//        rtmp1.url="rtmp://116.213.200.53/tslsChannelLive/PCG0DuD/live";
//        rtmp2.url="rtmp://live.hkstv.hk.lxdns.com/live/hks";
//        live.rtmpList.rtmp.add(rtmp1);
//        live.rtmpList.rtmp.add(rtmp2);
////        live.teacherList = new TeacherList();
//        live.status = 2;
//
//        LiveBusStation.startLive(this, live);
//    }


}