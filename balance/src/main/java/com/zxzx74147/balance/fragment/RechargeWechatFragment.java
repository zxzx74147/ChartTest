package com.zxzx74147.balance.fragment;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.balance.R;
import com.zxzx74147.balance.data.PayNewData;
import com.zxzx74147.balance.data.PayRequest;
import com.zxzx74147.balance.data.PayResultData;
import com.zxzx74147.balance.data.PayResultLiveData;
import com.zxzx74147.balance.databinding.FragmentRechargeFailBinding;
import com.zxzx74147.balance.databinding.FragmentRechargeSuccBinding;
import com.zxzx74147.balance.databinding.FragmentRechargeWechatBinding;
import com.zxzx74147.balance.databinding.ShareRechargeWechatBinding;
import com.zxzx74147.balance.storage.PayStorage;
import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.image.ImageBinder;
import com.zxzx74147.devlib.modules.account.AccountManager;
import com.zxzx74147.devlib.modules.busstation.ProfileBusStation;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.FileUtil;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;

import io.reactivex.Observable;

/**
 */
public class RechargeWechatFragment extends BaseDialogFragment {
    private FragmentRechargeWechatBinding mBinding = null;
    private PayStorage mPayStorage = RetrofitClient.getClient().create(PayStorage.class);
    private PayResultLiveData mPayResultLiveDat = new PayResultLiveData();
    private PayRequest mPayReqeust;
    private boolean mHasSaved = false;

    public static RechargeWechatFragment newInstance(IntentData<PayRequest> amount) {
        RechargeWechatFragment fragment = new RechargeWechatFragment();
        Bundle args = new Bundle();
        args.putSerializable(ZXActivityJumpHelper.INTENT_DATA, amount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recharge_wechat, container, false);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        IntentData<PayRequest> intentData = (IntentData<PayRequest>) getArguments().getSerializable(ZXActivityJumpHelper.INTENT_DATA);
        mPayReqeust = intentData.data;
        mBinding.setPay(mPayReqeust);
        Observable<PayNewData> obs = null;
        switch (mPayReqeust.type) {
            case PayRequest.TYPE_WECHAT:
                obs = mPayStorage.payWechat(mPayReqeust.amount);
                break;
            case PayRequest.TYPE_ALIPAY:
                obs = mPayStorage.payAli(mPayReqeust.amount);
                break;
        }
        NetworkApi.ApiSubscribe(getActivity(), obs, false, payNewData -> {
            mBinding.progress.setVisibility(View.GONE);
            if (payNewData.hasError()) {
                ToastUtil.showToast(getActivity(), payNewData.error.usermsg);
                return;
            }
            mBinding.setData(payNewData);
            mBinding.setPay(mPayReqeust);
            mPayResultLiveDat.setDepositId(payNewData.depositId);
        }, PayNewData.class);


        //支付结果
        mPayResultLiveDat.observe(RechargeWechatFragment.this, payResultData -> {
            if (payResultData.hasError()) {
                return;
            }
            switch (payResultData.depositStatus) {
                case PayResultData.RESULT_DOING:
                case PayResultData.RESULT_NONE:
                    break;
                case PayResultData.RESULT_SUCC:
                    showSucc();
//                    dismiss();
                    break;
                case PayResultData.RESULT_FAIL:
                    showFail();
                    dismiss();
                    break;
            }
        });


        //保存图片
        RxView.clicks(mBinding.save).subscribe(o -> {

            if (!FileUtil.isExternalStorageWritable()) {
                FileUtil.verifyStoragePermissions(getActivity());
                return;
            }
            if (mBinding.getData() == null) {
                ToastUtil.showToast(getActivity(), "加载中，请稍后");
                return;
            }
            if (mHasSaved) {
                ToastUtil.showToast(getActivity(), "已保存,请到相册查看");
                return;
            }

            ShareRechargeWechatBinding mShareBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.share_recharge_wechat, null, false);
            switch (mPayReqeust.type) {
                case PayRequest.TYPE_WECHAT:
                    mShareBinding.root.setBackgroundResource(R.drawable.wechat_save);
                    break;
                case PayRequest.TYPE_ALIPAY:
                    mShareBinding.root.setBackgroundResource(R.drawable.alipay_save);
                    break;
            }
            mShareBinding.amount.setText(mPayReqeust.amount / 100 + "");
            ImageBinder.loadImageFromBase64(mShareBinding.qrCode, mBinding.getData().codeImg);
            mBinding.getRoot().postDelayed(() -> {
                Bitmap bm = ViewUtil.generateBitmapFromView(mShareBinding.getRoot(), getResources().getDimensionPixelSize(R.dimen.default_gap_512), getResources().getDimensionPixelSize(R.dimen.default_gap_918));
                if (ViewUtil.saveImageToGallery(getContext(), bm)) {
                    ToastUtil.showToast(getActivity(), "保存成功");
                    mHasSaved = true;
                }
            }, 200);
        });

    }

    public void showSucc() {
        AccountManager.sharedInstance().doRefresh();
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        FragmentRechargeSuccBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_recharge_succ, null, false);
        binding.setAmount(mPayReqeust.amount);
        dialog.setContentView(binding.getRoot());
        dialog.show();
        RxView.clicks(binding.recharge).subscribe(v -> {
                    ProfileBusStation.startProfile(getActivity());
                    dialog.dismiss();
                    dismiss();
                }
        );

    }

    public void showFail() {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        FragmentRechargeFailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_recharge_fail, null, false);
        dialog.setContentView(binding.getRoot());
        dialog.show();
    }


}
