package com.zxzx74147.devlib.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by zhengxin on 2018/2/12.
 */

public class BaseBindingViewHolder extends BaseViewHolder {
    public ViewDataBinding mBinding = null;
    public BaseBindingViewHolder(View view) {
        super(view);
        mBinding = DataBindingUtil.bind(view);
    }
}
