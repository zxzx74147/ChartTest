package com.zxzx74147.devlib.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;

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

    private int layout_id = 0;
    private View mEmptyView = null;

    @Override
    protected void convert(BaseBindingViewHolder helper, T item) {
        ViewDataBinding itemCommonBinding = helper.mBinding;
        itemCommonBinding.setVariable(BR.data, item);
    }



    public void remove(T item) {
        if (mData.remove(item)) {
            notifyDataSetChanged();
        }
    }

    public void setCommonEmptyView(Context context, int content) {
        CommonNoItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.common_no_item, null, false);
        if (content != 0) {
            binding.setContent(context.getResources().getString(content));
        }
        setEmptyView(binding.getRoot());
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    public void setEmptyView(int layout_id) {
        layout_id = layout_id;
    }

    public void setNewData(List<T> data) {
        if (layout_id > 0) {
            super.setEmptyView(layout_id);
        } else if (mEmptyView != null) {
            super.setEmptyView(mEmptyView);
        }
        super.setNewData(data);

    }
}
