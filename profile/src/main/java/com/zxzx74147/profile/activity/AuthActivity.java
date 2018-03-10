package com.zxzx74147.profile.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;

import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.profile.R;
import com.zxzx74147.profile.data.AuthApplyData;
import com.zxzx74147.profile.databinding.ActivityAuthBinding;
import com.zxzx74147.profile.storage.AccountStorage;

import io.reactivex.functions.Consumer;

public class AuthActivity extends BaseActivity {
    private static final String TAG = AuthActivity.class.getSimpleName();

    private ActivityAuthBinding mBinding = null;
    private AccountStorage mStockStorage = RetrofitClient.getClient().create(AccountStorage.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_auth);
        init();
    }

    private void init() {
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

        NetworkApi.ApiSubscribe(mStockStorage.authApply(name, cid), new Consumer<AuthApplyData>() {

            @Override
            public void accept(AuthApplyData uniApiData) throws Exception {
                if (uniApiData.hasError()) {
                    ToastUtil.showToast(AuthActivity.this, uniApiData.error.usermsg);
                    return;
                }
                ToastUtil.showToast(AuthActivity.this, R.string.succ);

                finish();

            }
        });

    }


}
