package com.zxzx74147.charttest;

import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.jakewharton.rxbinding2.view.RxView;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.zxzx74147.charttest.databinding.ActivityLauncherBinding;
import com.zxzx74147.charttest.databinding.LayoutUpgradeBinding;
import com.zxzx74147.devlib.DevLib;
import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.data.SysInitData;
import com.zxzx74147.devlib.data.Upgrade;
import com.zxzx74147.devlib.kvstore.KVStore;
import com.zxzx74147.devlib.modules.account.AccountManager;
import com.zxzx74147.devlib.modules.busstation.MainBusStation;
import com.zxzx74147.devlib.modules.busstation.ProfileBusStation;
import com.zxzx74147.devlib.modules.sys.SysInitManager;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.os.DeviceIDMananger;
import com.zxzx74147.devlib.os.PackageInfoMananger;
import com.zxzx74147.devlib.utils.AnimationUtil;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.devlib.wxapi.WxApiHandler;
import com.zxzx74147.profile.storage.SysStorage;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LauncherActivity extends BaseActivity {

    private ActivityLauncherBinding mBinding = null;
    private boolean mIsSysInit = false;
    private boolean misAnimation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_launcher);
        sysInit();

        Observable.just("").delay(100, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(s -> {
            misAnimation = true;
            if (mIsSysInit) {
                if (AccountManager.sharedInstance().isLogin()) {
                    openMain();
                } else {
                    showLoginButton();
                }
            }
        });
    }

    private void showLoginButton() {
        float y = mBinding.logo.getTop() + mBinding.logo.getHeight() - mBinding.launcherApp.getTop() + getResources().getDimensionPixelOffset(R.dimen.default_gap_80);
        AnimationUtil.translationView(mBinding.launcherApp, 0, y);
        AnimationUtil.showViewAlpha(mBinding.loginPhone);
//        AnimationUtil.showViewAlpha(mBinding.loginWechat);
        RxView.clicks(mBinding.loginWechat).subscribe(v -> {
            loginWechat();
        });
        RxView.clicks(mBinding.loginPhone).subscribe(v -> {
            ProfileBusStation.startLogin(this);
            finish();
        });
    }

    private void loginWechat() {
        WxApiHandler.doLogin(this, new CommonCallback<SendAuth.Resp>() {
            @Override
            public void callback(SendAuth.Resp item) {
                String code = item.code;
            }
        });
    }

    private void openMain() {
        MainBusStation.startMain(this);
        finish();
//        ZXActivityJumpHelper.startActivity(LauncherActivity.this, StockActivity.class);
    }

    private void sysInit() {
        String token = KVStore.getString("push_id");
        SysStorage mStorage = RetrofitClient.getClient().create(SysStorage.class);
        Observable<SysInitData> initObser = mStorage.sysInit("main", DeviceIDMananger.sharedInstance().getDeviceID(), token, "", PackageInfoMananger.sharedInstance().getVersionInfo().getVersonName());
        NetworkApi.ApiSubscribe(initObser, sysInit -> {
            if (sysInit.hasError()) {
                ToastUtil.showToast(LauncherActivity.this, sysInit.error.usermsg);
                return;
            }
            SysInitManager.sharedInstance().setSysInitData(sysInit);
//            sysInit.upgrade = new Upgrade();
//            sysInit.upgrade.show = 1;
//            sysInit.upgrade.force = 1;
//            sysInit.upgrade.msg = "hahah";
//            sysInit.upgrade.url = "http://pws.myhug.cn/android_apk/avalon/559818/common/channels/avalon_common_main_2.0.0.apk";

//            if (dealUpgrade(sysInit.upgrade)) {
//                return;
//            }
            mIsSysInit = true;
            if (misAnimation) {
                if (AccountManager.sharedInstance().isLogin()) {
                    openMain();
                } else {
                    showLoginButton();
                }
            }
        });
    }

    private boolean dealUpgrade(Upgrade upgrade) {
        if (upgrade == null || upgrade.show == 0) {
            return false;
        }
        Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        LayoutUpgradeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(DevLib.getApp()), R.layout.layout_upgrade, null, false);
        dialog.setContentView(binding.getRoot());
        RxView.clicks(binding.close).subscribe(v -> {
            dialog.dismiss();
        });
        RxView.clicks(binding.upgrade).subscribe(v -> {
            AllenVersionChecker
                    .getInstance()
                    .downloadOnly(
                            UIData.create().setDownloadUrl(upgrade.url).setTitle("开始下载").setContent("")
                    ).setSilentDownload(true).setDownloadUrl(upgrade.url).excuteMission(getApplication());
        });
        binding.setData(upgrade);
        dialog.show();
        if (upgrade.force != 0) {
            binding.close.setVisibility(View.GONE);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });
        } else {
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    mIsSysInit = true;
                    if (misAnimation) {
                        if (AccountManager.sharedInstance().isLogin()) {
                            openMain();
                        } else {
                            showLoginButton();
                        }
                    }
                }
            });

        }
        return true;
    }
}
