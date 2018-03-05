package com.zxzx74147.devlib.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxzx74147.devlib.BR;
import com.zxzx74147.devlib.R;
import com.zxzx74147.devlib.base.BaseBindingViewHolder;
import com.zxzx74147.devlib.databinding.CommonNoItemBinding;

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

    public void addCommonEmptyView(Context context,int content){
        CommonNoItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.common_no_item,null,false);
        if (content!=0){
            binding.setContent(context.getResources().getString(content));
        }
        setEmptyView(binding.getRoot());
    }
}
