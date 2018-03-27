package com.zxzx74147.charttest;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.jakewharton.rxbinding2.support.design.widget.RxTabLayout;
import com.zxzx74147.charttest.databinding.ActivityMainBinding;
import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.modules.account.UserViewModel;
import com.zxzx74147.devlib.modules.sys.SysInitManager;
import com.zxzx74147.devlib.umeng.UmengAction;
import com.zxzx74147.devlib.umeng.UmengAgent;
import com.zxzx74147.devlib.utils.DisplayUtil;
import com.zxzx74147.profile.data.UnReadManager;
import com.zxzx74147.stock.data.GoodType;
import com.zxzx74147.stock.fragment.StockFragment;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding mBinding = null;
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
            if(e2==null){
                return true;
            }
            if (velocityX < 0&&Math.abs(e1.getX()-e2.getX())>2*Math.abs(e1.getY()-e2.getY())) {
                if (SysInitManager.sharedInstance().getSysInitData().swich.liveOpen != 0) {
                    finish();
                    return true;
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
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UmengAgent.onEvent(UmengAction.ALUmengPageLiveList);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.goodList.setCallback(mCallback);
        mBinding.goodList.setProvider(ViewModelProviders.of(this));
        mBinding.goodList.setLifeCircle(this);
        initView();
        initData();
    }

    private CommonCallback<GoodType> mCallback = item -> {
        StockFragment fragment = StockFragment.newInstance(item);
        fragment.show(getSupportFragmentManager(), fragment.getTag());
//        Live live = new Live();
//        live.liveId=4;
//        live.status=2;
//        live.rtmpList = new RtmpList();
//        live.rtmpList.num=1;
//        live.rtmpList.rtmp=new LinkedList<>();
//        Rtmp rtmp = new Rtmp();
//        rtmp.url="abd";
//        live.rtmpList.rtmp.add(rtmp);
//        live.teacherList= new TeacherList();
//        live.teacherList.teacher = new LinkedList<>();
//        LiveBusStation.startLive(this,live);

    };

    private void initView() {
        mGestureDetector = new GestureDetectorCompat(this, mOnGestureListener);
        RxTabLayout.selectionEvents(mBinding.tabLayout).subscribe(tabLayoutSelectionEvent -> {
            if(tabLayoutSelectionEvent.tab().getPosition()==1){
                finish();
            }
        });

    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition( R.anim.slide_in_hold,R.anim.slide_out_left);
    }

    private void initData() {
        mUserViewModel = ViewModelProviders.of(MainActivity.this).get(UserViewModel.class);
//        mBinding.setUser(mUserViewModel.getUserUniLiveData().getValue().user);
        mUserViewModel.getUserUniLiveData().observe(MainActivity.this, userUniData -> {
            if (userUniData.hasError()) {
                return;
            }
            mBinding.setUser(userUniData.user);
            mBinding.setUnRead(UnReadManager.sharedInstance().getUnReadNum());
        });
    }


}