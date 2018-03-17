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
import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.modules.account.AccountManager;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.profile.R;
import com.zxzx74147.profile.activity.LoginPhoneActivity;
import com.zxzx74147.profile.data.UserUniData;
import com.zxzx74147.profile.databinding.LayoutResetPasswordBinding;
import com.zxzx74147.profile.databinding.LayoutSetPasswordBinding;
import com.zxzx74147.profile.storage.AccountStorage;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 */
public class PasswordResetFragment extends BaseDialogFragment {

    private LayoutResetPasswordBinding mBinding = null;
    private AccountStorage mStorage = RetrofitClient.getClient().create(AccountStorage.class);
    private boolean mIsCountDonw = false;

    public static PasswordResetFragment newInstance() {
        PasswordResetFragment fragment = new PasswordResetFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.layout_reset_password, container, false);
        mBinding.setUser(AccountManager.sharedInstance().getUser());
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
        RxCompoundButton.checkedChanges(mBinding.checkPassword2).subscribe(aBoolean -> {
            if (aBoolean) {
                mBinding.password2.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else {
                mBinding.password2.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            }
        });

        RxView.clicks(mBinding.cancel).subscribe(a -> {
            dismiss();
        });

        RxView.clicks(mBinding.vcodeRemind).subscribe(a -> {
            if(mIsCountDonw){
                return;
            }
            requestVcode();
        });


        RxView.clicks(mBinding.ok).subscribe(a -> {
            String p1 = mBinding.password.getText().toString();
            String p2 = mBinding.password2.getText().toString();
            String vcode = mBinding.vcode.getText().toString();
            if (TextUtils.isEmpty(p1)) {
                ToastUtil.showToast(getActivity(), "请输入密码");
                return;
            }
            if (TextUtils.isEmpty(p2)) {
                ToastUtil.showToast(getActivity(), "请重复密码");
                return;
            }

            if (TextUtils.isEmpty(vcode)) {
                ToastUtil.showToast(getActivity(), "请输入验证码");
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
            NetworkApi.ApiSubscribe(getActivity(),mStorage.tradePasswordModify(p1,vcode),true, userUniData -> {
                if(userUniData.hasError()){
                    ToastUtil.showToast(getActivity(),userUniData.error.usermsg);
                    return;
                }
                AccountManager.sharedInstance().saveUser(userUniData.user);
                dismiss();
            },UserUniData.class);
        });


    }

    public void requestVcode() {

        NetworkApi.ApiSubscribe(this,mStorage.tradePasswordVcode(),false, new Consumer<UniApiData>() {

            @Override
            public void accept(UniApiData uniApiData) throws Exception {
                if (uniApiData.hasError()) {
                    ToastUtil.showToast(getActivity(), uniApiData.error.usermsg);
                    return;
                }

                final long count = 60;
                ViewUtil.showSoftPad(mBinding.vcode);
                Observable.interval(0, 1, TimeUnit.SECONDS).take(count + 1).map(aLong -> count - aLong).doOnSubscribe(disposable -> mIsCountDonw = true).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(t -> {
                    if (t <= 0) {
                        mIsCountDonw = false;
                        mBinding.vcodeRemind.setTextColor(getResources().getColor(R.color.text_blue));
                    } else {
                        String format = getResources().getString(R.string.vcode_countdown);
                        mBinding.vcodeRemind.setText(String.format(format, t));
                        mBinding.vcodeRemind.setTextColor(getResources().getColor(R.color.text_light_grey));

                    }
                });

            }
        },UniApiData.class);

    }


}
