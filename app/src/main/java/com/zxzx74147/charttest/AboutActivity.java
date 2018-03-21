package com.zxzx74147.charttest;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.sdk.android.push.common.util.AppInfoUtil;
import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.charttest.databinding.ActivityAboutBinding;
import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.data.Upgrade;
import com.zxzx74147.devlib.modules.sys.SysInitManager;
import com.zxzx74147.devlib.os.PackageInfoMananger;

public class AboutActivity extends BaseActivity {

    private ActivityAboutBinding mBinding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_about);
        String version = AppInfoUtil.getAppVersionName(this);
        mBinding.setVersion(version+ "("+PackageInfoMananger.sharedInstance().getVersionInfo().buildVersion+")");
        mBinding.setUpgrade(SysInitManager.sharedInstance().getSysInitData().upgrade);
        RxView.clicks(mBinding.upgrade).subscribe(v->{
            dealUpgrade(mBinding.getUpgrade());
        });

    }


    private void dealUpgrade(Upgrade upgrade) {
        if (upgrade == null || upgrade.show == 0) {
            return;
        }
        if(TextUtils.isEmpty(upgrade.url)){
            return;
        }

        AllenVersionChecker
                .getInstance()
                .downloadOnly(
                        UIData.create().setDownloadUrl(upgrade.url).setTitle("开始下载").setContent("")
                ).setSilentDownload(true).setDownloadUrl(upgrade.url).excuteMission(getApplication());


    }


}
