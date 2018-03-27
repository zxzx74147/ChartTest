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
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.profile.R;
import com.zxzx74147.profile.data.UserUniData;
import com.zxzx74147.profile.databinding.LayoutSetPasswordBinding;
import com.zxzx74147.profile.storage.AccountStorage;

import io.reactivex.functions.Consumer;

/**
 */
public class PasswordSetFragment extends BaseDialogFragment {

    private LayoutSetPasswordBinding mBinding = null;
    private AccountStorage mStorage = RetrofitClient.getClient().create(AccountStorage.class);

    public static PasswordSetFragment newInstance() {
        PasswordSetFragment fragment = new PasswordSetFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.layout_set_password, container, false);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        RxCompoundButton.checkedChanges(mBinding.checkPassword).subscribe(aBoolean -> {
            if (aBoolean) {
                mBinding.password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                mBinding.password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            ViewUtil.locateEditCursor(mBinding.password);
        });
//
        RxCompoundButton.checkedChanges(mBinding.checkPassword2).subscribe(aBoolean -> {
            if (aBoolean) {
                mBinding.password2.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                mBinding.password2.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            ViewUtil.locateEditCursor(mBinding.password2);
        });

        RxView.clicks(mBinding.cancel).subscribe(a -> {
            dismiss();
        });

        RxView.clicks(mBinding.ok).subscribe(a -> {
            String p1 = mBinding.password.getText().toString();
            String p2 = mBinding.password2.getText().toString();
            if (TextUtils.isEmpty(p1)) {
                ToastUtil.showToast(getActivity(), "请输入密码");
                return;
            }
            if (TextUtils.isEmpty(p2)) {
                ToastUtil.showToast(getActivity(), "请重复密码");
                return;
            }
            if (!p1.equals(p2)) {
                ToastUtil.showToast(getActivity(), "两次输入不一致");
                return;
            }
            if (p1.length() < 6 || p1.length() > 12) {
                ToastUtil.showToast(getActivity(), "请设置6-12位登录密码");
                return;
            }
            NetworkApi.ApiSubscribe(getActivity(),mStorage.tradePassword(p1),true, userUniData -> {
                if(userUniData.hasError()){
                    ToastUtil.showToast(getActivity(),userUniData.error.usermsg);
                    return;
                }
                AccountManager.sharedInstance().saveUser(userUniData.user);
                dismiss();
            },UserUniData.class);
        });


    }


}
