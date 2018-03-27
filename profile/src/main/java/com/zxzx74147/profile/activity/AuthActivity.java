package com.zxzx74147.profile.activity;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;

import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.modules.account.AccountManager;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.umeng.UmengAction;
import com.zxzx74147.devlib.umeng.UmengAgent;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.profile.R;
import com.zxzx74147.profile.data.AuthApplyData;
import com.zxzx74147.profile.databinding.ActivityAuthBinding;
import com.zxzx74147.profile.storage.AccountStorage;

import io.reactivex.functions.Consumer;
@SuppressLint("CheckResult")
public class AuthActivity extends BaseActivity {
    private static final String TAG = AuthActivity.class.getSimpleName();

    private ActivityAuthBinding mBinding = null;
    private AccountStorage mStockStorage = RetrofitClient.getClient().create(AccountStorage.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UmengAgent.onEvent(UmengAction.ALUmengPageRealName);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_auth);
        init();
    }


    private void init() {
        mBinding.setAuth(AccountManager.sharedInstance().getUserUni().auth);
        RxView.clicks(mBinding.submit).subscribe(v -> {
            submit();
        });

    }


    public void submit() {

        String name = mBinding.nameEdit.getText().toString();
        String cid = mBinding.cidEdit.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showToast(this, R.string.name_hint);
            return;
        }

        if (TextUtils.isEmpty(cid)) {
            ToastUtil.showToast(this, R.string.cid_hint);
            return;
        }

        NetworkApi.ApiSubscribe(this,mStockStorage.authApply(name, cid),true, uniApiData -> {
            if (uniApiData.hasError()) {
                ToastUtil.showToast(AuthActivity.this, uniApiData.error.usermsg);
                return;
            }
            ToastUtil.showToast(AuthActivity.this, R.string.succ);
            AccountManager.sharedInstance().doRefresh();

            finish();

        },AuthApplyData.class);

    }


}
