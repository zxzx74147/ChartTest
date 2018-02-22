package com.zxzx74147.profile;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.modules.account.CommonUserViewModel;

public class ProfileActivity extends BaseActivity {

    private CommonUserViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mViewModel = ViewModelProviders.of(this).get(CommonUserViewModel.class);
//        mViewModel.getUserData().observe(this,user -> {
//
//        });

    }
}
