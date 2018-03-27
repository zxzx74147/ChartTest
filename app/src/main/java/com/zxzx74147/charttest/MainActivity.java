package com.zxzx74147.charttest;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.zxzx74147.charttest.databinding.ActivityMainBinding;
import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.modules.account.UserViewModel;
import com.zxzx74147.stock.data.GoodType;
import com.zxzx74147.stock.fragment.StockFragment;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding mBinding = null;
    private UserViewModel mUserViewModel = null;


    public static void startMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        });
    }


}