package com.zxzx74147.balance.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.balance.R;
import com.zxzx74147.balance.data.Deposit;
import com.zxzx74147.balance.databinding.ActivityDepositListBinding;
import com.zxzx74147.balance.storage.DepositStorage;
import com.zxzx74147.devlib.BR;
import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.base.BaseBindingViewHolder;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.stock.data.Position;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhengxin on 2018/3/4.
 */

public class DepositListActivity extends BaseActivity {

    private ActivityDepositListBinding mBinding = null;
    private DepositStorage mDepositStorage = null;

    private List<Deposit> mData = new LinkedList<>();
    private CommonRecyclerViewAdapter<Deposit> mAdapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_deposit_list);

        mDepositStorage = RetrofitClient.getClient().create(DepositStorage.class);
        mAdapter = new CommonRecyclerViewAdapter<>(mData);

        CommonMultiTypeDelegate delegate = new CommonMultiTypeDelegate();
        delegate.registViewType(Deposit.class, R.layout.item_recharge_record);

        mAdapter.setMultiTypeDelegate(delegate);
        mBinding.list.setLayoutManager(new LinearLayoutManager(this));
        mBinding.list.setAdapter(mAdapter);
        refresh();
    }

    private void refresh() {
        NetworkApi.ApiSubscribe(mDepositStorage.depositList(0), depositListData -> {
            if (depositListData.hasError()) {
                return;
            }
            if (depositListData.depositList == null) {
                return;
            }
            mData.clear();
            mData.addAll(depositListData.depositList.deposit);
            mAdapter.notifyDataSetChanged();
        });
    }
}
