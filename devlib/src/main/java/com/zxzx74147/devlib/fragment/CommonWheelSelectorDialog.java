package com.zxzx74147.devlib.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.devlib.R;
import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.data.WheelSelectorData;
import com.zxzx74147.devlib.databinding.CommonFragmentDialogBinding;
import com.zxzx74147.devlib.databinding.CommonSelectorBinding;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;

import cn.qqtheme.framework.widget.WheelView;

/**
 */
public class CommonWheelSelectorDialog extends BaseDialogFragment {
    private static final String TAG = CommonFragmentDialogBinding.class.getSimpleName();

    private CommonSelectorBinding mBinding = null;


    public static CommonWheelSelectorDialog newInstance(IntentData<WheelSelectorData> item) {
        CommonWheelSelectorDialog fragment = new CommonWheelSelectorDialog();
        Bundle args = new Bundle();
        args.putSerializable(ZXActivityJumpHelper.INTENT_DATA, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.common_selector, container, false);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        IntentData<WheelSelectorData> intentData = (IntentData<WheelSelectorData>) getArguments().getSerializable(ZXActivityJumpHelper.INTENT_DATA);
        WheelSelectorData wheelSelectorData = intentData.data;
        mBinding.wheel.setItems(wheelSelectorData.items, wheelSelectorData.offset);
        float textSize = getResources().getDimension(R.dimen.default_size_26)/2;
        mBinding.wheel.setDividerColor(getResources().getColor(R.color.red));
        mBinding.wheel.setTextColor(getResources().getColor(R.color.text_dark_grey), getResources().getColor(R.color.text_red));
        mBinding.wheel.setTextSize(textSize);
        mBinding.wheel.setVisibleItemCount(5);
        mBinding.wheel.setCycleDisable(false);
        mBinding.wheel.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
            @Override
            public void onSelected(int index) {

            }
        });

//
//
        RxView.clicks(mBinding.cancel).subscribe(o -> {
            dismiss();
        });
//
        RxView.clicks(mBinding.ok).subscribe(o -> {
            if (mCallback != null) {
                mCallback.callback(mBinding.wheel.getSelectedIndex());
            } else {
                Log.e(TAG, "NO CALLBACK !");
            }
            dismiss();
        });

    }


}
