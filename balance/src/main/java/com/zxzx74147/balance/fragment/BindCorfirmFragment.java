package com.zxzx74147.balance.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.balance.R;
import com.zxzx74147.balance.data.BankCard;
import com.zxzx74147.balance.data.PayRequest;
import com.zxzx74147.balance.databinding.FragmentRechargeBinding;
import com.zxzx74147.balance.databinding.LayoutBindConfirmBinding;
import com.zxzx74147.balance.storage.PayStorage;
import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.data.DepositItem;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.modules.sys.SysInitManager;
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
public class BindCorfirmFragment extends BaseDialogFragment {

    private LayoutBindConfirmBinding mBinding = null;


    public static BindCorfirmFragment newInstance(IntentData<BankCard> card) {
        BindCorfirmFragment fragment = new BindCorfirmFragment();
        Bundle args = new Bundle();
        args.putSerializable(ZXActivityJumpHelper.INTENT_DATA, card);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.layout_bind_confirm, container, false);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        IntentData<BankCard> intentData = (IntentData<BankCard>) getArguments().getSerializable(ZXActivityJumpHelper.INTENT_DATA);
        BankCard card = intentData.data;
        mBinding.setBankCard(card);


        RxView.clicks(mBinding.cancel).subscribe(o->{
            dismiss();
        });

        RxView.clicks(mBinding.ok).subscribe(o->{
            if(mCallback!=null){
                mCallback.callback(mBinding.getBankCard());
            }
        });

    }


}
