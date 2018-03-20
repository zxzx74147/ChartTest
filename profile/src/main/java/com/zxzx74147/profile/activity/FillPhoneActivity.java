package com.zxzx74147.profile.activity;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.umeng.analytics.AnalyticsConfig;
import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.kvstore.KVStore;
import com.zxzx74147.devlib.modules.account.AccountManager;
import com.zxzx74147.devlib.modules.busstation.MainBusStation;
import com.zxzx74147.devlib.modules.sys.SysInitManager;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.os.DeviceIDMananger;
import com.zxzx74147.devlib.os.PackageInfoMananger;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.utils.WebviewUtil;
import com.zxzx74147.profile.R;
import com.zxzx74147.profile.data.UserUniData;
import com.zxzx74147.profile.databinding.ActivityFillPhoneBinding;
import com.zxzx74147.profile.databinding.ActivityLoginPhoneBinding;
import com.zxzx74147.profile.storage.AccountStorage;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FillPhoneActivity extends BaseActivity {
    private static final String TAG = FillPhoneActivity.class.getSimpleName();

    private ActivityFillPhoneBinding mBinding = null;
    private boolean mIsCountDonw = false;
    private AccountStorage mStockStorage = RetrofitClient.getClient().create(AccountStorage.class);
    private Disposable mDisposable = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_fill_phone);
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    private void init() {
        RxView.clicks(mBinding.layoutFillPhone.enterDelete).subscribe(a -> {
            mBinding.layoutFillPhone.phoneNumber.setText("");
        });
        Observable<CharSequence> phoneNumberOb = RxTextView.textChanges(mBinding.layoutFillPhone.phoneNumber);
        phoneNumberOb.subscribe(charSequence -> {
            if (mIsCountDonw) {
                return;
            }
            checkPhoneNum(charSequence);

        });

        RxView.clicks(mBinding.layoutFillPhone.start).subscribe(a -> {
            requestLogin();
        });

        RxView.clicks(mBinding.layoutFillPhone.close).subscribe(a -> {
            finish();
        });



        RxView.clicks(mBinding.layoutFillPhone.vcodeRemind).subscribe(a -> {
            if (mIsCountDonw) {
                return;
            }
            if (checkPhoneNum(mBinding.layoutFillPhone.phoneNumber.getText())) {
                requestVcode();
            }
        });


    }

    private boolean checkPhoneNum(CharSequence charSequence) {
        mBinding.layoutFillPhone.vcodeRemind.setText(R.string.login_vcode);
        if (charSequence.length() == 11) {
            mBinding.layoutFillPhone.vcodeRemind.setTextColor(getResources().getColor(R.color.text_blue));
            return true;
        } else {
            mBinding.layoutFillPhone.vcodeRemind.setTextColor(getResources().getColor(R.color.text_light_grey));
            return false;
        }
    }

    public void requestVcode() {

        NetworkApi.ApiSubscribe(this,mStockStorage.accountGetVCode(mBinding.layoutFillPhone.phoneNumber.getText().toString()),false, new Consumer<UniApiData>() {

            @Override
            public void accept(UniApiData uniApiData) throws Exception {
                if (uniApiData.hasError()) {
                    ToastUtil.showToast(FillPhoneActivity.this, uniApiData.error.usermsg);
                    return;
                }

                final long count = 60;
                ViewUtil.showSoftPad(mBinding.layoutFillPhone.vcode);
                Observable.interval(0, 1, TimeUnit.SECONDS).take(count + 1).map(aLong -> count - aLong).doOnSubscribe(disposable -> {
                    mDisposable = disposable;
                    mIsCountDonw = true;
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(t -> {
                    if (t <= 0) {
                        mIsCountDonw = false;
                        checkPhoneNum(mBinding.layoutFillPhone.phoneNumber.getText());
                    } else {
                        String format = getResources().getString(R.string.vcode_countdown);
                        mBinding.layoutFillPhone.vcodeRemind.setText(String.format(format, t));
                        mBinding.layoutFillPhone.vcodeRemind.setTextColor(getResources().getColor(R.color.text_light_grey));

                    }
                });

            }
        },UniApiData.class);

    }

    public void requestLogin() {
        if (!checkPhoneNum(mBinding.layoutFillPhone.phoneNumber.getText())) {
            return;
        }
        if (TextUtils.isEmpty(mBinding.layoutFillPhone.vcode.getText())) {
            return;
        }
        String token = KVStore.getString("push_id");
        Observable<UserUniData> observable = mStockStorage.acctountLogin(mBinding.layoutFillPhone.phoneNumber.getText().toString(),
                mBinding.layoutFillPhone.vcode.getText().toString(),
                DeviceIDMananger.sharedInstance().getDeviceID(), PackageInfoMananger.sharedInstance().getVersionInfo().getVersonName()
        , Build.MODEL,  AnalyticsConfig.getChannel(getApplication()),token);
        NetworkApi.ApiSubscribe(this,observable, true,new Consumer<UserUniData>() {
            @Override
            public void accept(UserUniData userUniData) throws Exception {
                if (userUniData.hasError()) {
                    ToastUtil.showToast(FillPhoneActivity.this, userUniData.error.usermsg);
                    return;
                }
                AccountManager.sharedInstance().saveUser(userUniData.user);
                MainBusStation.startMain(FillPhoneActivity.this);
                if(mCallback!=null){
                    mCallback.callback(userUniData);
                }
                finish();
            }
        },UserUniData.class);
    }


}
