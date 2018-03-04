package com.zxzx74147.balance.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import com.zxzx74147.balance.R;
import com.zxzx74147.balance.data.Bank;
import com.zxzx74147.balance.databinding.ActivityBankListBinding;
import com.zxzx74147.balance.storage.BankStorage;
import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhengxin on 2018/3/4.
 */

public class BankListActivity extends BaseActivity {

    private ActivityBankListBinding mBinding = null;
    private BankStorage mBankStorage = null;

    private List<Bank> mData = new LinkedList<>();
    private CommonRecyclerViewAdapter<Bank> mAdapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_bank_list);

        mBankStorage = RetrofitClient.getClient().create(BankStorage.class);
        mAdapter = new CommonRecyclerViewAdapter<>(mData);

        CommonMultiTypeDelegate delegate = new CommonMultiTypeDelegate();
        delegate.registViewType(Bank.class, R.layout.item_bank);

        mAdapter.setMultiTypeDelegate(delegate);
        mBinding.list.setLayoutManager(new LinearLayoutManager(this));
        mBinding.list.setAdapter(mAdapter);
        refresh();

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if(mCallback!=null){
                mCallback.callback(mData.get(position));
            }
//            Intent intent = new Intent();
//            intent.putExtra(ZXActivityJumpHelper.INTENT_DATA,mData.get(position));
//            setResult(Activity.RESULT_OK,intent);
            finish();
        });
    }

    private void refresh() {
        NetworkApi.ApiSubscribe(mBankStorage.bankList(), bankListData -> {
            if (bankListData.hasError()) {
                return;
            }
            if (bankListData.bankList == null) {
                return;
            }
            mData.clear();
            mData.addAll(bankListData.bankList.bank);
            mAdapter.notifyDataSetChanged();
        });
    }
}
