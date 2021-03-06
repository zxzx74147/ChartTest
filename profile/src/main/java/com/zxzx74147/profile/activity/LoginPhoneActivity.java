package com.zxzx74147.profile.activity;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.zxzx74147.devlib.DevLib;
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
import com.zxzx74147.devlib.umeng.UmengAction;
import com.zxzx74147.devlib.umeng.UmengAgent;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.utils.WebviewUtil;
import com.zxzx74147.profile.R;
import com.zxzx74147.profile.data.UserUniData;
import com.zxzx74147.profile.databinding.ActivityLoginPhoneBinding;
import com.zxzx74147.profile.storage.AccountStorage;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
@SuppressLint("CheckResult")
public class LoginPhoneActivity extends BaseActivity {
    private static final String TAG = LoginPhoneActivity.class.getSimpleName();

    private ActivityLoginPhoneBinding mBinding = null;
    private boolean mIsCountDonw = false;
    private AccountStorage mStockStorage = RetrofitClient.getClient().create(AccountStorage.class);
    private Disposable mDisposable = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobclickAgent.onEvent(DevLib.getApp(), UmengAction.ALUmengPagePhoneLogin);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login_phone);
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
        RxView.clicks(mBinding.layoutRegist.enterDelete).subscribe(a -> {
            mBinding.layoutRegist.phoneNumber.setText("");
        });
        Observable<CharSequence> phoneNumberOb = RxTextView.textChanges(mBinding.layoutRegist.phoneNumber);
        phoneNumberOb.subscribe(charSequence -> {
            if (mIsCountDonw) {
                return;
            }
            checkPhoneNum(charSequence);

        });

        RxView.clicks(mBinding.layoutRegist.start).subscribe(a -> {
            requestLogin();
        });

        RxView.clicks(mBinding.layoutRegist.close).subscribe(a -> {
            finish();
        });

        RxView.clicks(mBinding.layoutRegist.protocol).subscribe(a -> {
            WebviewUtil.showWebActivity(this, SysInitManager.sharedInstance().getSysInitData().config.userProtocolUrl);
        });

        RxView.clicks(mBinding.layoutRegist.vcodeRemind).subscribe(a -> {
            if (mIsCountDonw) {
                return;
            }
            if (checkPhoneNum(mBinding.layoutRegist.phoneNumber.getText())) {
                requestVcode();
            }
        });


    }

    private boolean checkPhoneNum(CharSequence charSequence) {
        mBinding.layoutRegist.vcodeRemind.setText(R.string.login_vcode);
        if (charSequence.length() == 11) {
            mBinding.layoutRegist.vcodeRemind.setTextColor(getResources().getColor(R.color.text_blue));
            return true;
        } else {
            mBinding.layoutRegist.vcodeRemind.setTextColor(getResources().getColor(R.color.text_light_grey));
            return false;
        }
    }

    public void requestVcode() {
        MobclickAgent.onEvent(DevLib.getApp(), UmengAction.ALUmengPagePhoneLoginGetVCode);
        NetworkApi.ApiSubscribe(this,mStockStorage.accountGetVCode(mBinding.layoutRegist.phoneNumber.getText().toString()),false, new Consumer<UniApiData>() {

            @Override
            public void accept(UniApiData uniApiData) throws Exception {
                if (uniApiData.hasError()) {
                    ToastUtil.showToast(LoginPhoneActivity.this, uniApiData.error.usermsg);
                    return;
                }

                final long count = 60;
                ViewUtil.showSoftPad(mBinding.layoutRegist.vcode);
                Observable.interval(0, 1, TimeUnit.SECONDS).take(count + 1).map(aLong -> count - aLong).doOnSubscribe(disposable -> {
                    mDisposable = disposable;
                    mIsCountDonw = true;
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(t -> {
                    if (t <= 0) {
                        mIsCountDonw = false;
                        checkPhoneNum(mBinding.layoutRegist.phoneNumber.getText());
                    } else {
                        String format = getResources().getString(R.string.vcode_countdown);
                        mBinding.layoutRegist.vcodeRemind.setText(String.format(format, t));
                        mBinding.layoutRegist.vcodeRemind.setTextColor(getResources().getColor(R.color.text_light_grey));

                    }
                });

            }
        },UniApiData.class);

    }

    public void requestLogin() {
        if (!checkPhoneNum(mBinding.layoutRegist.phoneNumber.getText())) {
            return;
        }
        UmengAgent.onEvent(UmengAction.ALUmengPagePhoneLoginEnterPhoneNumber);
        if (TextUtils.isEmpty(mBinding.layoutRegist.vcode.getText())) {
            return;
        }
        UmengAgent.onEvent(UmengAction.ALUmengPagePhoneLoginEnterVCode);
        UmengAgent.onEvent(UmengAction.ALUmengPagePhoneLoginButtonPressed);
        String token = KVStore.getString("push_id");
        Observable<UserUniData> observable = mStockStorage.acctountLogin(mBinding.layoutRegist.phoneNumber.getText().toString(),
                mBinding.layoutRegist.vcode.getText().toString(),
                DeviceIDMananger.sharedInstance().getDeviceID(), PackageInfoMananger.sharedInstance().getVersionInfo().getVersonName()
        , Build.MODEL,  AnalyticsConfig.getChannel(getApplication()),token);
        NetworkApi.ApiSubscribe(this,observable, true,new Consumer<UserUniData>() {
            @Override
            public void accept(UserUniData userUniData) throws Exception {
                if (userUniData.hasError()) {
                    ToastUtil.showToast(LoginPhoneActivity.this, userUniData.error.usermsg);
                    return;
                }
                AccountManager.sharedInstance().saveUser(userUniData.user);
                MainBusStation.startMain(LoginPhoneActivity.this);
                if(mCallback!=null){
                    mCallback.callback(userUniData);
                }
                finish();
            }
        },UserUniData.class);
    }


}
