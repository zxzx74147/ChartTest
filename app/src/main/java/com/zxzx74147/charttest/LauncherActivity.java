package com.zxzx74147.charttest;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zxzx74147.charttest.databinding.ActivityLauncherBinding;
import com.zxzx74147.devlib.data.Upgrade;
import com.zxzx74147.devlib.modules.account.AccountManager;
import com.zxzx74147.devlib.modules.busstation.MainBusStation;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.os.DeviceIDMananger;
import com.zxzx74147.devlib.os.PackageInfoMananger;
import com.zxzx74147.devlib.utils.AnimationUtil;
import com.zxzx74147.profile.storage.SysStorage;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LauncherActivity extends AppCompatActivity {

    private ActivityLauncherBinding mBinding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_launcher);
        sysInit();

        Observable.just("").delay(100, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(s -> {
            if (AccountManager.sharedInstance().isLogin()) {
                openMain();
            } else {
                showLoginButton();
            }
        });
    }

    private void showLoginButton() {
        float y = mBinding.logo.getTop() + mBinding.logo.getHeight() - mBinding.launcherApp.getTop() + getResources().getDimensionPixelOffset(R.dimen.default_gap_80);
        AnimationUtil.translationView(mBinding.launcherApp, 0, y);
        AnimationUtil.showViewAlpha(mBinding.loginPhone);
        AnimationUtil.showViewAlpha(mBinding.loginWechat);
    }

    private void openMain() {
        MainBusStation.startMain(this);
        finish();
//        ZXActivityJumpHelper.startActivity(LauncherActivity.this, StockActivity.class);
    }

    private void sysInit() {

        SysStorage mStorage = RetrofitClient.getClient().create(SysStorage.class);
        Observable<Upgrade> initObser = mStorage.sysInit("main", DeviceIDMananger.sharedInstance().getDeviceID(), "", "", PackageInfoMananger.sharedInstance().getVersionInfo().getVersonName());
        NetworkApi.ApiSubscribe(initObser, upgrade -> {
            if (upgrade.hasError()) {
                return;
            }
        });
    }
}
