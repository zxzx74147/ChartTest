package com.zxzx74147.devlib.fragment;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.devlib.R;
import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.data.DialogItem;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.databinding.CommonFragmentDialogBinding;
import com.zxzx74147.devlib.json.JsonHelper;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;

/**
 */
public class CommonFragmentDialog extends BaseDialogFragment {
    private static final String TAG = CommonFragmentDialogBinding.class.getSimpleName();

    private CommonFragmentDialogBinding mBinding = null;


    public static CommonFragmentDialog newInstance(IntentData<DialogItem> item) {
        CommonFragmentDialog fragment = new CommonFragmentDialog();
        Bundle args = new Bundle();
        args.putSerializable(ZXActivityJumpHelper.INTENT_DATA, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.common_fragment_dialog, container, false);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        IntentData<DialogItem> intentData = (IntentData<DialogItem>) getArguments().getSerializable(ZXActivityJumpHelper.INTENT_DATA);
        DialogItem card = intentData.data;
        mBinding.setDialogItem(card);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (card.ok != null && card.ok.length() > 2) {
                mBinding.ok.setLetterSpacing(0f);
            }
            if (card.cancel != null && card.cancel.length() > 2) {
                mBinding.cancel.setLetterSpacing(0f);
            }
        }
//
//
        RxView.clicks(mBinding.cancel).subscribe(o -> {
            dismiss();
            if (mCallback != null) {
                mCallback.callback(null);
            } else {
                Log.e(TAG, "NO CALLBACK !" + JsonHelper.toJson(card));
            }
        });
//
        RxView.clicks(mBinding.ok).subscribe(o -> {
            dismiss();
            if (mCallback != null) {
                mCallback.callback(mBinding.getDialogItem().obj);
            } else {
                Log.e(TAG, "NO CALLBACK !" + JsonHelper.toJson(card));
            }
        });

    }


}
