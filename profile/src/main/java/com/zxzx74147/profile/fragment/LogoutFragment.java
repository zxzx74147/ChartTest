package com.zxzx74147.profile.fragment;

import android.app.LauncherActivity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.base.BaseFragment;
import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.modules.busstation.MainBusStation;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;
import com.zxzx74147.profile.R;
import com.zxzx74147.profile.data.UserUniData;
import com.zxzx74147.profile.databinding.LayoutLogoutBinding;
import com.zxzx74147.profile.storage.AccountStorage;

import io.reactivex.functions.Consumer;

/**
 * Created by zhengxin on 2018/2/15.
 */

public class LogoutFragment extends BaseDialogFragment {
    private LayoutLogoutBinding mBinding = null;
    private AccountStorage mAccountStorage = RetrofitClient.getClient().create(AccountStorage.class);

    public static LogoutFragment newInstance() {
        LogoutFragment fragment = new LogoutFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.layout_logout, container, false);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        RxView.clicks(mBinding.cancel).subscribe(v->{
            dismiss();
        });

        RxView.clicks(mBinding.ok).subscribe(v->{
            logout();
        });

    }

    private void logout(){
        NetworkApi.ApiSubscribe(this.getLifecycle(),mAccountStorage.logout(), t -> {
            if(t.hasError()){
                ToastUtil.showToast(getContext(),t.error.usermsg);
                return;
            }
            dismiss();
            MainBusStation.logout(getActivity());
        },UniApiData.class);
    }

}
