package com.zxzx74147.devlib.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
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
public class CommonInfoDialog extends BaseDialogFragment {
    private static final String TAG = CommonFragmentDialogBinding.class.getSimpleName();

    private ViewDataBinding mBinding = null;


    public static CommonInfoDialog newInstance(IntentData<Integer> item) {
        CommonInfoDialog fragment = new CommonInfoDialog();
        Bundle args = new Bundle();
        args.putSerializable(ZXActivityJumpHelper.INTENT_DATA, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        IntentData<Integer> intentData = (IntentData<Integer>) getArguments().getSerializable(ZXActivityJumpHelper.INTENT_DATA);
        Integer id = intentData.data;
        mBinding = DataBindingUtil.inflate(inflater, id, container, false);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
    }


}
