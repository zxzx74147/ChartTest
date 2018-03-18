package com.zxzx74147.profile.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.modules.account.AccountManager;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.profile.R;
import com.zxzx74147.profile.databinding.LayoutInputPassBinding;
import com.zxzx74147.profile.databinding.LayoutSetPasswordBinding;
import com.zxzx74147.profile.storage.AccountStorage;

/**
 */
public class PasswordInputFragment extends BaseDialogFragment {

    private LayoutInputPassBinding mBinding = null;
    private AccountStorage mStorage = RetrofitClient.getClient().create(AccountStorage.class);

    public static PasswordInputFragment newInstance() {
        PasswordInputFragment fragment = new PasswordInputFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.layout_input_pass, container, false);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        RxCompoundButton.checkedChanges(mBinding.checkPassword).subscribe(aBoolean -> {
            if (aBoolean) {
                mBinding.password.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else {
                mBinding.password.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            }

        });
//


        RxView.clicks(mBinding.cancel).subscribe(a -> {
            dismiss();
        });

        RxView.clicks(mBinding.ok).subscribe(a -> {
            String p1 = mBinding.password.getText().toString();

            if (TextUtils.isEmpty(p1)) {
                ToastUtil.showToast(getActivity(), "请输入密码");
                return;
            }

            NetworkApi.ApiSubscribe(mStorage.tradeLogin(p1), userUniData -> {
                if(userUniData.hasError()){
                    ToastUtil.showToast(getActivity(),userUniData.error.usermsg);
                    return;
                }
                AccountManager.sharedInstance().saveUser(userUniData.user);
                dismiss();
            });
        });


    }


}
