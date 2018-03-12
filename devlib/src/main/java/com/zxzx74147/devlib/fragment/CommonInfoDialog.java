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
    private IntentData<Integer> intentData;


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
        intentData = (IntentData<Integer>) getArguments().getSerializable(ZXActivityJumpHelper.INTENT_DATA);
        Integer id = intentData.data;
        mBinding = DataBindingUtil.inflate(inflater, id, container, false);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        View cancel = mBinding.getRoot().findViewById(R.id.cancel);
        View ok = mBinding.getRoot().findViewById(R.id.ok);
        View close = mBinding.getRoot().findViewById(R.id.close);
        if(close!=null)
        RxView.clicks(close).subscribe(v->{
            dismiss();

        });

        if(cancel!=null)
        RxView.clicks(cancel).subscribe(v->{
            dismiss();
            if(mCallback!=null){
                mCallback.callback(null);
            }else{
                Log.e(TAG, "NO CALLBACK !");
            }
        });
        if(ok!=null)
        RxView.clicks(ok).subscribe(v->{
            dismiss();
            if(mCallback!=null){
                mCallback.callback(intentData);
            }else{
                Log.e(TAG, "NO CALLBACK !");
            }
        });
    }


}
