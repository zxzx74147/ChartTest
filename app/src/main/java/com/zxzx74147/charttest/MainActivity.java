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

//        ZXFragmentJumpHelper.startFragment(this, ProfileFragment.class, null);
    };

    private void initView() {
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