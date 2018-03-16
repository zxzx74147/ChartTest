package com.zxzx74147.balance.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.balance.R;
import com.zxzx74147.balance.activity.WithdrawActivity;
import com.zxzx74147.balance.data.BankCard;
import com.zxzx74147.balance.databinding.FragmentUnbindBankBinding;
import com.zxzx74147.balance.storage.BankStorage;
import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.fragment.CommonFragmentDialog;
import com.zxzx74147.devlib.fragment.CommonInfoDialog;
import com.zxzx74147.devlib.modules.account.AccountManager;
import com.zxzx74147.devlib.modules.busstation.BalanceBusStation;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;
import com.zxzx74147.devlib.utils.ZXFragmentJumpHelper;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 */
public class UnBindCardFragment extends BaseDialogFragment {

    private FragmentUnbindBankBinding mBinding = null;
    private boolean mIsCountDonw = false;
    private BankStorage mStorage = RetrofitClient.getClient().create(BankStorage.class);

    public static UnBindCardFragment newInstance(IntentData<BankCard> intentData) {
        UnBindCardFragment fragment = new UnBindCardFragment();
        Bundle args = new Bundle();
        args.putSerializable(ZXActivityJumpHelper.INTENT_DATA, intentData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_unbind_bank, container, false);
        initView();
        Bundle bundle = getArguments();
        IntentData goodIntent = (IntentData) bundle.getSerializable(ZXActivityJumpHelper.INTENT_DATA);
        mBinding.setData((BankCard) goodIntent.data);
        mBinding.setUser(AccountManager.sharedInstance().getUser());
        return mBinding.getRoot();
    }

    private void initView() {

        RxView.clicks(mBinding.unbind).subscribe(o->{
            doUnbind();
        });

        RxView.clicks(mBinding.vcodeRemind).subscribe(a -> {
            if (mIsCountDonw) {
                return;
            }
            requestVcode();
        });
    }



    private void doUnbind(){
        String vcode = mBinding.vcode.getText().toString();
        if(TextUtils.isEmpty(vcode)){
            ToastUtil.showToast(getActivity(),"请填写验证码");
            return;
        }

        NetworkApi.ApiSubscribe(getActivity(),mStorage.bindRemove(mBinding.getData().bankCardId,vcode),true, new Consumer<UniApiData>() {

            @Override
            public void accept(UniApiData uniApiData) throws Exception {
                if (uniApiData.hasError()) {
                    ToastUtil.showToast(getActivity(), uniApiData.error.usermsg);

                    CommonInfoDialog dialog = CommonInfoDialog.newInstance(new IntentData<Integer>(R.layout.unbind_fail_dialog));
                    ZXFragmentJumpHelper.startFragment(getActivity(), dialog, new CommonCallback() {
                        @Override
                        public void callback(Object item) {
                            //TODO
                            dismiss();
                        }
                    });

                    return;
                }

                CommonInfoDialog dialog = CommonInfoDialog.newInstance(new IntentData<Integer>(R.layout.unbind_succ_dialog));
                ZXFragmentJumpHelper.startFragment(getActivity(), dialog, new CommonCallback() {
                    @Override
                    public void callback(Object item) {
                        ((WithdrawActivity)getActivity()).refresh();
                        dismiss();
                    }
                });
            }
        },UniApiData.class);

    }


    public void requestVcode() {

        NetworkApi.ApiSubscribe(mStorage.unbindVCode(), new Consumer<UniApiData>() {

            @Override
            public void accept(UniApiData uniApiData) throws Exception {
                if (uniApiData.hasError()) {
                    ToastUtil.showToast(getActivity(), uniApiData.error.usermsg);
                    return;
                }
                ViewUtil.showSoftPad(mBinding.vcode);
                final long count = 60;
                Observable.interval(0, 1, TimeUnit.SECONDS).take(count + 1).map(aLong -> count - aLong).doOnSubscribe(disposable -> mIsCountDonw = true).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(t -> {
                    if (t <= 0) {
                        mIsCountDonw = false;
                    } else {
                        String format = getResources().getString(R.string.vcode_countdown);
                        mBinding.vcodeRemind.setText(String.format(format, t));
                        mBinding.vcodeRemind.setTextColor(getResources().getColor(R.color.text_light_grey));

                    }
                });

            }
        });

    }


}
