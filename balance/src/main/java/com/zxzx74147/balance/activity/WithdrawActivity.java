package com.zxzx74147.balance.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.zxzx74147.balance.R;
import com.zxzx74147.balance.data.Bank;
import com.zxzx74147.balance.data.BankCard;
import com.zxzx74147.balance.data.BankCardData;
import com.zxzx74147.balance.data.BankListData;
import com.zxzx74147.balance.databinding.ActivityWithdrawBinding;
import com.zxzx74147.balance.fragment.BindCorfirmFragment;
import com.zxzx74147.balance.storage.BankStorage;
import com.zxzx74147.balance.storage.WithDrawStorage;
import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.data.DialogItem;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.fragment.CommonFragmentDialog;
import com.zxzx74147.devlib.modules.account.AccountManager;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;
import com.zxzx74147.devlib.utils.ZXFragmentJumpHelper;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhengxin on 2018/3/4.
 */

public class WithdrawActivity extends BaseActivity {

    private ActivityWithdrawBinding mBinding = null;
    private BankStorage mBankStorage = null;
    private WithDrawStorage mWithdrawStorage = null;
    private boolean mIsCountDonw = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_withdraw);
        mBankStorage = RetrofitClient.getClient().create(BankStorage.class);
        mWithdrawStorage = RetrofitClient.getClient().create(WithDrawStorage.class);
        initView();
        initData();
    }

    private void initData() {
        NetworkApi.ApiSubscribe(mBankStorage.myCardList(), bankListData -> {
            if(bankListData.hasError()){
                return;
            }
            mBinding.setBankCard(null);
            if(bankListData.bankCardList==null||bankListData.bankCardList.bankCard==null||bankListData.bankCardList.bankCard.size()==0){
                initNoCard();
                return;
            }
            mBinding.setBankCard(bankListData.bankCardList.bankCard.get(0));
            initHasCard();
        });
    }
    private void initView() {

        //binding amount
        mBinding.setUser(AccountManager.sharedInstance().getUser());
        RxTextView.textChanges(mBinding.withdrawAmount.amount).subscribe(charSequence -> {
            if (charSequence.length() == 0) {
                mBinding.withdrawAmount.amountRemind.setText(R.string.withdraw_amount_remind);
            } else {
                mBinding.withdrawAmount.amountRemind.setText(R.string.withdraw_amount);
            }
        });
        RxView.clicks(mBinding.withdrawAmount.all).subscribe(o -> {
            mBinding.withdrawAmount.amount.setText(String.format(getResources().getString(R.string.format_02),
                    AccountManager.sharedInstance().getUser().balance));
        });


        RxView.clicks(mBinding.withdrawBank.vcodeRemind).subscribe(a -> {
            if (mIsCountDonw) {
                return;
            }
            requestVcode();
        });


    }

    private void initHasCard(){
        RxView.clicks(mBinding.submit).subscribe(o->{
            withdraw();
        });
    }

    private void initNoCard(){
        RxView.clicks(mBinding.submit).subscribe(o->{
            if(TextUtils.isEmpty(mBinding.withdrawBank.editText2.getText())){
                ToastUtil.showToast(WithdrawActivity.this,R.string.hint_card_name);
                return;
            }
            if(TextUtils.isEmpty(mBinding.withdrawBank.editText.getText())){
                ToastUtil.showToast(WithdrawActivity.this,R.string.hint_card_num);
                return;
            }
            if(TextUtils.isEmpty(mBinding.withdrawBank.bankName.getText())){
                ToastUtil.showToast(WithdrawActivity.this,R.string.hint_card_bank);
                return;
            }
            if(TextUtils.isEmpty(mBinding.withdrawBank.vcode.getText())){
                ToastUtil.showToast(WithdrawActivity.this,"请输入验证码");
                return;
            }
            BankCard bankCard = new BankCard();
            bankCard.name = mBinding.withdrawBank.editText2.getText().toString();
            bankCard.cardNo = mBinding.withdrawBank.editText.getText().toString();
            bankCard.bank = mBinding.withdrawBank.bankName.getText().toString();
            BindCorfirmFragment fragment = BindCorfirmFragment.newInstance(new IntentData<>(bankCard));
            ZXFragmentJumpHelper.startFragment(WithdrawActivity.this, fragment, new CommonCallback() {
                @Override
                public void callback(Object item) {
                    if(item!=null){
                        bindCard(bankCard);
                    }
                }
            });

        });

        RxView.clicks(mBinding.withdrawBank.bankName).subscribe(o->{
            ZXActivityJumpHelper.startActivityWithCallback(WithdrawActivity.this, BankListActivity.class, new CommonCallback() {
                @Override
                public void callback(Object item) {
                    Bank bank = (Bank) item;
                    mBinding.withdrawBank.bankName.setText(bank.name);
                }
            });
        });
    }

    private void withdraw(){
        float amount = Float.parseFloat(mBinding.withdrawAmount.amount.getText().toString());
        if(amount>AccountManager.sharedInstance().getUser().balance){
            ToastUtil.showToast(WithdrawActivity.this,"最多提现"+AccountManager.sharedInstance().getUser().balance);
            return;
        }
        NetworkApi.ApiSubscribe(mWithdrawStorage.withdrawCase(mBinding.getBankCard().bankCardId, (int) (amount*100)), withdrawData -> {
            if(withdrawData.hasError()){
                DialogItem item = new DialogItem();
                item.title = getResources().getString(R.string.withdraw_fail);
                item.content = withdrawData.error.usermsg==null? getResources().getString(R.string.withdraw_fail_content):withdrawData.error.usermsg;
                item.cancel = getResources().getString(R.string.cancel);
                item.ok = getResources().getString(R.string.contact_service);
                CommonFragmentDialog dialog = CommonFragmentDialog.newInstance(new IntentData<>(item));
                ZXFragmentJumpHelper.startFragment(WithdrawActivity.this, dialog, new CommonCallback() {
                    @Override
                    public void callback(Object item) {
                        //TODO 联系客服
                        return;
                    }
                });
                return;
            }
            ToastUtil.showToast(WithdrawActivity.this,R.string.withdraw_succ);
            finish();
        });
    }

    private void requestVcode() {
        NetworkApi.ApiSubscribe(mBankStorage.bindVCode(), new Consumer<UniApiData>() {

            @Override
            public void accept(UniApiData uniApiData) throws Exception {
                if (uniApiData.hasError()) {
                    ToastUtil.showToast(WithdrawActivity.this, uniApiData.error.usermsg);
                    return;
                }

                final long count = 60;
                Observable.interval(0, 1, TimeUnit.SECONDS).take(count + 1).map(aLong -> count - aLong).doOnSubscribe(disposable -> mIsCountDonw = true).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(t -> {
                    if (t <= 0) {
                        mIsCountDonw = false;

                    } else {
                        String format = getResources().getString(R.string.vcode_countdown);
                        mBinding.withdrawBank.vcodeRemind.setText(String.format(format, t));
                        mBinding.withdrawBank.vcodeRemind.setTextColor(getResources().getColor(R.color.text_light_grey));
                    }
                });

            }
        });
    }

    private void bindCard(BankCard card) {
        NetworkApi.ApiSubscribe(mBankStorage.bindAdd(card.bank,card.cardNo,card.name,mBinding.withdrawBank.vcode.getText().toString()), bankCardData -> {
            if(bankCardData.hasError()){
                return;
            }
            mBinding.setBankCard(bankCardData.bankCard);
            DialogItem item = new DialogItem();
            item.title = getResources().getString(R.string.bind_succ);
            item.content = getResources().getString(R.string.bind_succ_content);
            item.cancel = getResources().getString(R.string.just_ok);
            item.ok = getResources().getString(R.string.go_withdarw);
            item.obj = bankCardData.bankCard;
            CommonFragmentDialog dialog = CommonFragmentDialog.newInstance(new IntentData<>(item));
            ZXFragmentJumpHelper.startFragment(WithdrawActivity.this, dialog, new CommonCallback() {
                @Override
                public void callback(Object item) {

                    initData();
                    return;
                }
            });

        });


    }
}
