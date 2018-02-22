package com.zxzx74147.devlib.widget;

import android.databinding.ViewDataBinding;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxzx74147.devlib.BR;
import com.zxzx74147.devlib.base.BaseBindingViewHolder;

import java.util.List;

/**
 * Created by zhengxin on 16/8/21.
 */

public class CommonRecyclerViewAdapter<T> extends BaseQuickAdapter<T, BaseBindingViewHolder> {

    public CommonRecyclerViewAdapter(List<T> data) {
        super(data);
    }

    @Override
    protected void convert(BaseBindingViewHolder helper, T item) {
        ViewDataBinding itemCommonBinding = helper.mBinding;
        itemCommonBinding.setVariable(BR.data, item);
    }
}
