package com.zxzx74147.balance.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jakewharton.rxbinding2.support.design.widget.RxTabLayout;
import com.jakewharton.rxbinding2.support.design.widget.TabLayoutSelectionEvent;
import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.balance.R;
import com.zxzx74147.balance.data.Deposit;
import com.zxzx74147.balance.data.DepositListData;
import com.zxzx74147.balance.data.Withdraw;
import com.zxzx74147.balance.data.WithdrawData;
import com.zxzx74147.balance.data.WithdrawListData;
import com.zxzx74147.balance.databinding.ActivityDepositListBinding;
import com.zxzx74147.balance.storage.DepositStorage;
import com.zxzx74147.balance.storage.WithDrawStorage;
import com.zxzx74147.devlib.BR;
import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.base.BaseBindingViewHolder;
import com.zxzx74147.devlib.data.BaseListData;
import com.zxzx74147.devlib.interfaces.CommonListRequestCallback;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.RecyclerViewUtil;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.stock.data.Position;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by zhengxin on 2018/3/4.
 */

public class DepositListActivity extends BaseActivity {

    private ActivityDepositListBinding mBinding = null;
    private DepositStorage mDepositStorage = null;
    private WithDrawStorage mWithdrawStorage = null;

    private List<Deposit> mData = new LinkedList<>();
    private CommonRecyclerViewAdapter<Deposit> mAdapter = null;
    private CommonRecyclerViewAdapter<Withdraw> mAdapter2 = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_deposit_list);
        mWithdrawStorage= RetrofitClient.getClient().create(WithDrawStorage.class);
        mDepositStorage = RetrofitClient.getClient().create(DepositStorage.class);
        mAdapter = new CommonRecyclerViewAdapter<>(mData);

        CommonMultiTypeDelegate delegate = new CommonMultiTypeDelegate();
        delegate.registViewType(Deposit.class, R.layout.item_recharge_record);
        delegate.registViewType(Withdraw.class, R.layout.item_withdraw_record);

        mAdapter.setMultiTypeDelegate(delegate);
        mAdapter2 = new CommonRecyclerViewAdapter<>(null);
        mBinding.list.setLayoutManager(new LinearLayoutManager(this));
        mBinding.list.setAdapter(mAdapter);
        initView();

        mAdapter.setCommonEmptyView(this,R.string.no_deposit);
        mAdapter2.setCommonEmptyView(this,R.string.no_withdraw);

    }

    private void initView(){

        RxTabLayout.selectionEvents(mBinding.tabLayout2).subscribe(tabLayoutSelectionEvent -> {
            if(tabLayoutSelectionEvent.tab().getPosition()==0){
//                mBinding.refreshLayout.setVisibility(View.VISIBLE);
//                mBinding.refreshLayout2.setVisibility(View.GONE);
                mAdapter.loadMoreComplete();
                RecyclerViewUtil.setupRecyclerView(mBinding.refreshLayout, mBinding.list, mAdapter, new CommonListRequestCallback<Deposit>() {
                    @Override
                    public Observable<DepositListData> getObserverble(BaseListData listdata) {
                        if(listdata==null){
                            return mDepositStorage.depositList(0);
                        }else
                        {
                            return mDepositStorage.depositList(listdata.nextPage);
                        }
                    }
                });
            }else{
//                mBinding.refreshLayout.setVisibility(View.GONE);
//                mBinding.refreshLayout2.setVisibility(View.VISIBLE);

                mAdapter2.loadMoreComplete();
                RecyclerViewUtil.setupRecyclerView(mBinding.refreshLayout, mBinding.list, mAdapter2, new CommonListRequestCallback<Withdraw>() {
                    @Override
                    public Observable<WithdrawListData> getObserverble(BaseListData listdata) {
                        if(listdata==null){
                            return mWithdrawStorage.withdrawList(0);
                        }else
                        {
                            return mWithdrawStorage.withdrawList(listdata.nextPage);
                        }
                    }
                });
            }
        });




    }


}
