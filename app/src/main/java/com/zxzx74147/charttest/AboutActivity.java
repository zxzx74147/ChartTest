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
import com.zxzx74147.charttest.databinding.ActivityAboutBinding;
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

public class AboutActivity extends BaseActivity {

    private ActivityAboutBinding mBinding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_about);

    }


}
