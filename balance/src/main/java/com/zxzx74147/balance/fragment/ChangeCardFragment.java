package com.zxzx74147.balance.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.balance.R;
import com.zxzx74147.balance.data.PayRequest;
import com.zxzx74147.balance.data.RechargeAmount;
import com.zxzx74147.balance.databinding.FragmentChangeBankBinding;
import com.zxzx74147.balance.databinding.FragmentRechargeBinding;
import com.zxzx74147.balance.storage.PayStorage;
import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;
import com.zxzx74147.devlib.utils.ZXFragmentJumpHelper;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.devlib.widget.GridSpaceItemDecoration;

import java.util.LinkedList;
import java.util.List;

/**
 */
public class ChangeCardFragment extends BaseDialogFragment {

    private FragmentChangeBankBinding mBinding = null;

    public static ChangeCardFragment newInstance() {
        ChangeCardFragment fragment = new ChangeCardFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_bank, container, false);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {

        RxView.clicks(mBinding.submit).subscribe(o->{

        });
    }


}
