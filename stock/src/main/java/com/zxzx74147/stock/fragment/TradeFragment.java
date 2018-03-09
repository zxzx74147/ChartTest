package com.zxzx74147.stock.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.modules.account.UserViewModel;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;
import com.zxzx74147.profile.data.UserUniData;
import com.zxzx74147.stock.R;
import com.zxzx74147.stock.data.GoodType;
import com.zxzx74147.stock.databinding.FragmentTradeBinding;

/**
 */
public class TradeFragment extends BaseDialogFragment {

    public static int TYPE_POSITION_BUY_UP = 0;
    public static int TYPE_POSITION_BUY_DOWN = 1;
    public static int TYPE_MACH_POSITION_BUY_UP = 3;
    public static int TYPE_MACH_POSITION_BUY_DOWN = 4;
    private FragmentTradeBinding mBinding = null;
    private UserViewModel mUserViewModel = null;

    public static TradeFragment newInstance(GoodType mGoodType,int type) {
        TradeFragment fragment = new TradeFragment();
        Bundle args = new Bundle();
        IntentData<GoodType> intentData = new IntentData<>(mGoodType);
        intentData.type = type;
        args.putSerializable(ZXActivityJumpHelper.INTENT_DATA, intentData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_trade, container, false);
        mBinding.goodList.setProvider(ViewModelProviders.of(ViewUtil.getFragmentActivity(mBinding.getRoot())));
        mBinding.goodList.setLifeCircle(this);

        Bundle bundle = getArguments();
        IntentData goodIntent = (IntentData) bundle.getSerializable(ZXActivityJumpHelper.INTENT_DATA);
        mBinding.setGood((GoodType) goodIntent.data);
        mBinding.setType(goodIntent.type);
        mBinding.goodList.setCallback(item -> mBinding.setGood(item));
        onLazyInitView(savedInstanceState);

        return mBinding.getRoot();
    }

//    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        mUserViewModel = ViewModelProviders.of(ViewUtil.getFragmentActivity(getContext())).get(UserViewModel.class);
        mUserViewModel.getUserUniLiveData().observe(this, new Observer<UserUniData>() {
            @Override
            public void onChanged(@Nullable UserUniData userUniData) {
                mBinding.setUser(userUniData.user);
            }
        });
    }


}
