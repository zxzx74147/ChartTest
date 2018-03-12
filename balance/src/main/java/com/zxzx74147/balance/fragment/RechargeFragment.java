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
import com.zxzx74147.balance.databinding.FragmentRechargeBinding;
import com.zxzx74147.balance.storage.PayStorage;
import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.data.DepositItem;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.modules.sys.SysInitManager;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.utils.ZXFragmentJumpHelper;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.devlib.widget.GridSpaceItemDecoration;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 */
public class RechargeFragment extends BaseDialogFragment {
    private int amount = 1000;
    private FragmentRechargeBinding mBinding = null;
    private CommonRecyclerViewAdapter mAdapter = null;
    private List<DepositItem> mData = new LinkedList<>();
    private PayStorage mPayStorage = RetrofitClient.getClient().create(PayStorage.class);

    public static RechargeFragment newInstance() {
        RechargeFragment fragment = new RechargeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recharge, container, false);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        mAdapter = new CommonRecyclerViewAdapter<>(mData);
        CommonMultiTypeDelegate temp = new CommonMultiTypeDelegate();
        temp.registViewType(DepositItem.class, R.layout.item_recharge_amount);
        mAdapter.setMultiTypeDelegate(temp);
        mBinding.list.setAdapter(mAdapter);
        GridLayoutManager lm = new GridLayoutManager(getContext(), 3);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.default_gap_22);
        mBinding.list.getItemAnimator().setChangeDuration(0);// 通过设置动画执行时间为0来解决闪烁问题
        mBinding.list.addItemDecoration(new GridSpaceItemDecoration(3, spacingInPixels, spacingInPixels));
        mBinding.list.setLayoutManager(lm);

        if(SysInitManager.sharedInstance().getSysInitData().depositItemList!=null) {
            mData.addAll(SysInitManager.sharedInstance().getSysInitData().depositItemList.depositItem);
        }
        amount = mData.get(0).amount;
        mData.get(0).mIsSelect = true;


        mAdapter.notifyDataSetChanged();

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            for (int i = 0; i < mData.size(); i++) {
                if (position == i) {
                    if (mData.get(i).mIsSelect != true) {
                        mData.get(i).mIsSelect = true;
                        mAdapter.notifyItemChanged(i);
                        amount = mData.get(i).amount;
                    }
                } else {
                    if (mData.get(i).mIsSelect != false) {
                        mData.get(i).mIsSelect = false;
                        mAdapter.notifyItemChanged(i);
                    }
                }
            }
        });
//        ViewUtil.mergeRadioButton(mBinding.checkboxWechat, mBinding.checkboxAli);


        RxView.clicks(mBinding.wechatLayout).subscribe(v->{
            mBinding.checkboxWechat.setChecked(true);
        });

        RxView.clicks(mBinding.alipayLayout).subscribe(v->{
            mBinding.checkboxAli.setChecked(true);
        });
        RxView.clicks(mBinding.recharge).subscribe(o -> {
            IntentData<PayRequest> intent = new IntentData<>();
            PayRequest payRequest = new PayRequest();
            payRequest.amount = amount;
            intent.data = payRequest;
            if (mBinding.checkboxWechat.isChecked()) {
                payRequest.type = PayRequest.TYPE_WECHAT;
                ZXFragmentJumpHelper.startFragment(getContext(),RechargeWechatFragment.class,intent);
            } else if (mBinding.checkboxAli.isChecked()) {
                payRequest.type = PayRequest.TYPE_ALIPAY;
                ZXFragmentJumpHelper.startFragment(getContext(),RechargeWechatFragment.class,intent);
            }
        });
    }


}
