package com.zxzx74147.balance.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.zxzx74147.balance.R;
import com.zxzx74147.balance.data.PayNewData;
import com.zxzx74147.balance.data.PayRequest;
import com.zxzx74147.balance.databinding.FragmentRechargeBinding;
import com.zxzx74147.balance.storage.PayStorage;
import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.data.DepositItem;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.modules.sys.SysInitManager;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.ColorUtil;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.devlib.utils.ZXFragmentJumpHelper;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.devlib.widget.GridSpaceItemDecoration;

import java.util.LinkedList;
import java.util.List;

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
        mBinding.setSwich(SysInitManager.sharedInstance().getSysInitData().swich);
        CommonMultiTypeDelegate temp = new CommonMultiTypeDelegate();
        temp.registViewType(DepositItem.class, R.layout.item_recharge_amount);
        mAdapter.setMultiTypeDelegate(temp);
        mBinding.list.setAdapter(mAdapter);
        GridLayoutManager lm = new GridLayoutManager(getContext(), 3);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.default_gap_22);
        mBinding.list.getItemAnimator().setChangeDuration(0);// 通过设置动画执行时间为0来解决闪烁问题
        mBinding.list.addItemDecoration(new GridSpaceItemDecoration(3, spacingInPixels, spacingInPixels));
        mBinding.list.setLayoutManager(lm);
        String dst = "18元代金券";
        String str = getResources().getString(R.string.recharge_remind);
        int start = str.indexOf(dst);
        Spannable strSpan = new SpannableString(str);
        strSpan.setSpan(new ForegroundColorSpan(ColorUtil.getColor(R.color.red)), start, start + dst.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mBinding.textView2.setText(strSpan);

        if (SysInitManager.sharedInstance().getSysInitData().depositItemList != null) {
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


        RxCompoundButton.checkedChanges(mBinding.checkboxWechat).subscribe(checked -> {
            mBinding.checkboxAli.setChecked(!checked);
        });
        RxCompoundButton.checkedChanges(mBinding.checkboxAli).subscribe(checked -> {
            mBinding.checkboxWechat.setChecked(!checked);
        });
        RxView.clicks(mBinding.wechatLayout).subscribe(v -> {
            mBinding.checkboxWechat.setChecked(true);
        });

        RxView.clicks(mBinding.alipayLayout).subscribe(v -> {
            mBinding.checkboxAli.setChecked(true);
        });

        RxView.clicks(mBinding.recharge).subscribe(o -> {
            IntentData<PayRequest> intent = new IntentData<>();
            PayRequest payRequest = new PayRequest();
            payRequest.amount = amount;
            intent.data = payRequest;
            if (mBinding.checkboxWechat.isChecked()&&mBinding.wechatLayout.getVisibility()==View.VISIBLE) {
                payRequest.type = PayRequest.TYPE_WECHAT;
                ZXFragmentJumpHelper.startFragment(getContext(), RechargeWechatFragment.class, intent);
            } else if (mBinding.checkboxAli.isChecked()&&mBinding.alipayLayout.getVisibility()==View.VISIBLE) {
//                payRequest.type = PayRequest.TYPE_ALIPAY;
//                ZXFragmentJumpHelper.startFragment(getContext(), RechargeWechatFragment.class, intent);
                startAliPay(amount);
            }else{
                ToastUtil.showToast(getContext(),R.string.select_pay_method);
            }
        });
    }

    private void startAliPay(int amount){

        NetworkApi.ApiSubscribe(getActivity(), mPayStorage.payAli(amount), true, payNewData -> {

            if (payNewData.hasError()) {
                ToastUtil.showToast(getActivity(),payNewData.error.usermsg);
                return;
            }
            Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(payNewData.codeUrl));
            startActivity(in);
        }, PayNewData.class);

    }


}
