package com.zxzx74147.devlib.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.devlib.R;
import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.data.DialogItem;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.databinding.CommonFragmentDialogBinding;
import com.zxzx74147.devlib.databinding.CommonNetworkErrorDialogBinding;
import com.zxzx74147.devlib.json.JsonHelper;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;

import java.lang.ref.SoftReference;

/**
 */
public class CommonNetworkErrorDialog extends BaseDialogFragment {
    private static final String TAG = CommonFragmentDialogBinding.class.getSimpleName();
    public static SoftReference<CommonNetworkErrorDialog> mDialog  = null;

    private CommonNetworkErrorDialogBinding mBinding = null;


    public static CommonNetworkErrorDialog newInstance() {
        CommonNetworkErrorDialog fragment = new CommonNetworkErrorDialog();
        mDialog = new SoftReference<CommonNetworkErrorDialog>(fragment);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.common_network_error_dialog, container, false);
        initView();
        return mBinding.getRoot();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mDialog= null;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setWindowAnimations(R.style.dialogWindowAnim);
        dialog.setCancelable(false);

    }

    @Override
    public void dismiss() {
        super.dismiss();
        mDialog= null;
    }

    private void initView() {
        RxView.clicks(mBinding.refresh).subscribe(v->{
            if(mCallback!=null){
                mCallback.callback(null);
            }
        });
    }


}
