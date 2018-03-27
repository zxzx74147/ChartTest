package com.zxzx74147.stock.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.modules.account.UserViewModel;
import com.zxzx74147.devlib.umeng.UmengAction;
import com.zxzx74147.devlib.umeng.UmengAgent;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;
import com.zxzx74147.stock.R;
import com.zxzx74147.stock.data.GoodType;
import com.zxzx74147.stock.databinding.FragmentStockBinding;

/**
 */
public class StockFragment extends BaseDialogFragment {

    private FragmentStockBinding mBinding = null;
    private UserViewModel mModel = null;

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
        UmengAgent.onEvent(UmengAction.ALUmengPageTradeInfo);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_stock, container, false);
        mBinding.goodList.setProvider(ViewModelProviders.of(ViewUtil.getFragmentActivity(mBinding.getRoot())));
        mBinding.goodList.setLifeCircle(this);

        mBinding.stock.setProvider(ViewModelProviders.of(this));
        mBinding.stock.setLifeCircle(this);
        mModel = ViewModelProviders.of(this).get(UserViewModel.class);

        Bundle bundle = getArguments();
        GoodType good = (GoodType) bundle.getSerializable(ZXActivityJumpHelper.INTENT_DATA);
        mBinding.setGood(good);
        mBinding.goodList.scrollToCurrent();
        mBinding.goodList.setCallback(item -> mBinding.setGood(item));
        initData();

        return mBinding.getRoot();
    }

    private void initData(){
        mModel.getUserUniLiveData().observe(this, userUniData -> {
            if(userUniData.hasError()){
                return;
            }
            if(userUniData!=null&&userUniData.goodsTypeList!=null&&userUniData.goodsTypeList.goodType!=null) {
                for(GoodType goodType:userUniData.goodsTypeList.goodType){
                    if(goodType.goodsType.equals(mBinding.getGood().goodsType)){
                        mBinding.setGood(goodType);
                        break;
                    }
                }
            }
        });
    }

    public void  onDestroyView(){
        super.onDestroyView();
        mBinding.goodList.clearSelect();

    }


}
