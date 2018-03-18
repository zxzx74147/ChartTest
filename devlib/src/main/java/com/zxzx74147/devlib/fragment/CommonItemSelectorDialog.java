package com.zxzx74147.devlib.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.devlib.R;
import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.data.DialogItem;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.data.WheelSelectorData;
import com.zxzx74147.devlib.databinding.CommonFragmentDialogBinding;
import com.zxzx74147.devlib.databinding.CommonItemSelectorDialogBinding;
import com.zxzx74147.devlib.databinding.CommonSelectorBinding;
import com.zxzx74147.devlib.json.JsonHelper;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;

import cn.qqtheme.framework.widget.WheelView;

/**
 */
public class CommonItemSelectorDialog extends BaseDialogFragment {
    private static final String TAG = CommonFragmentDialogBinding.class.getSimpleName();

    private CommonItemSelectorDialogBinding mBinding = null;


    public static CommonItemSelectorDialog newInstance(IntentData<DialogItem> item) {
        CommonItemSelectorDialog fragment = new CommonItemSelectorDialog();
        Bundle args = new Bundle();
        args.putSerializable(ZXActivityJumpHelper.INTENT_DATA, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.common_item_selector_dialog, container, false);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        IntentData<DialogItem> intentData = (IntentData<DialogItem>) getArguments().getSerializable(ZXActivityJumpHelper.INTENT_DATA);
        DialogItem dialogItem = intentData.data;
        mBinding.setDialogItem(dialogItem);

        for(int i = 0;i<dialogItem.items.length;i++){
            String item=dialogItem.items[i];
            TextView textView = new TextView(getContext());
            textView.setText(item);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(getResources().getColor(R.color.text_black));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.default_size_34));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.default_gap_80));
            mBinding.content.addView(textView,lp);
            int id = i;
            RxView.clicks(textView).subscribe(o->{
                dismiss();
                if(mCallback!=null){
                    mCallback.callback(id);
                }else{
                    Log.e(TAG, "NO CALLBACK !"+ JsonHelper.toJson(dialogItem));
                }
            });
//            mBinding.
        }

        RxView.clicks(mBinding.cancel).subscribe(o->{
            dismiss();
            if(mCallback!=null){
                mCallback.callback(null);
            }else{
                Log.e(TAG, "NO CALLBACK !"+ JsonHelper.toJson(dialogItem));
            }
        });

    }


}
