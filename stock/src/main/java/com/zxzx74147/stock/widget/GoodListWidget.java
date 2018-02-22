package com.zxzx74147.stock.widget;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.devlib.widget.SpaceItemDecoration;
import com.zxzx74147.stock.R;
import com.zxzx74147.stock.data.GoodListData;
import com.zxzx74147.stock.data.GoodType;
import com.zxzx74147.stock.databinding.WidgetGoodListBinding;
import com.zxzx74147.stock.viewmodel.GoodViewModel;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhengxin on 2018/2/8.
 */

public class GoodListWidget extends RelativeLayout {
    private WidgetGoodListBinding mBinding = null;
    private GoodViewModel mModel = null;
    private CommonCallback<GoodType> mCallback = null;

    private List<GoodType> mData = new LinkedList<>();
    private CommonRecyclerViewAdapter<GoodType> mAdapter = null;

    public GoodListWidget(Context context) {
        this(context, null);
        init();
    }

    public GoodListWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public GoodListWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void initView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.widget_good_list, this, true);
        mAdapter = new CommonRecyclerViewAdapter<>(mData);
        mAdapter.setMultiTypeDelegate(new CommonMultiTypeDelegate());
        mBinding.list.setAdapter(mAdapter);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.default_gap_15);
        mBinding.list.addItemDecoration(new SpaceItemDecoration(spacingInPixels,0));
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        mBinding.list.setLayoutManager(lm);
        mAdapter.loadMoreComplete();
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            GoodType goodType = (GoodType) adapter.getItem(position);
            if(mCallback!=null){
                mCallback.callback(goodType);
            }
        });
    }

    public void setCallback(CommonCallback<GoodType> callback){
        mCallback = callback;
    }

    private void init() {
        initView();


        mModel = ViewModelProviders.of(ViewUtil.getFragmentActivity(this)).get(GoodViewModel.class);
        mModel.getGoodLiveData().observe(ViewUtil.getFragmentActivity(this), goodListData -> {
            if (goodListData.hasError()) {
                return;
            }

            mData.clear();
            if (goodListData.goodsList == null) {
                return;
            }
            mData.addAll(goodListData.goodsList.goodType);
            mAdapter.notifyDataSetChanged();

        });
    }


}
