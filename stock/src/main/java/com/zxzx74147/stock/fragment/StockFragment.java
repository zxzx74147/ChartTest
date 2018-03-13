package com.zxzx74147.stock.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;
import com.zxzx74147.stock.R;
import com.zxzx74147.stock.data.GoodType;
import com.zxzx74147.stock.databinding.FragmentStockBinding;

/**
 */
public class StockFragment extends BaseDialogFragment {

    private FragmentStockBinding mBinding = null;

    public static StockFragment newInstance(GoodType mGoodType) {
        StockFragment fragment = new StockFragment();
        Bundle args = new Bundle();
        args.putSerializable(ZXActivityJumpHelper.INTENT_DATA, mGoodType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_stock, container, false);
        mBinding.goodList.setProvider(ViewModelProviders.of(ViewUtil.getFragmentActivity(mBinding.getRoot())));
        mBinding.goodList.setLifeCircle(this);

        mBinding.stock.setProvider(ViewModelProviders.of(this));
        mBinding.stock.setLifeCircle(this);

        Bundle bundle = getArguments();
        GoodType good = (GoodType) bundle.getSerializable(ZXActivityJumpHelper.INTENT_DATA);
        mBinding.setGood(good);
        mBinding.goodList.setCallback(item -> mBinding.setGood(item));


        return mBinding.getRoot();
    }

    public void  onDestroyView(){
        super.onDestroyView();
        mBinding.goodList.clearSelect();

    }


}
