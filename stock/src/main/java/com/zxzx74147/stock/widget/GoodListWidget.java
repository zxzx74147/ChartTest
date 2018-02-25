package com.zxzx74147.stock.widget;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.devlib.widget.SpaceItemDecoration;
import com.zxzx74147.stock.R;
import com.zxzx74147.stock.data.Good;
import com.zxzx74147.stock.data.GoodType;
import com.zxzx74147.stock.databinding.ItemMyPositionBinding;
import com.zxzx74147.stock.databinding.WidgetGoodListBinding;
import com.zxzx74147.stock.viewmodel.GoodViewModel;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhengxin on 2018/2/8.
 */

public class GoodListWidget extends RelativeLayout {
    private WidgetGoodListBinding mBinding = null;
    private ItemMyPositionBinding mMyPositionBinding = null;
    private GoodViewModel mModel = null;
    private CommonCallback<GoodType> mCallback = null;
    private GoodType mGoodType = null;

    private List<GoodType> mData = new LinkedList<>();
    private CommonRecyclerViewAdapter<GoodType> mAdapter = null;

    public GoodListWidget(Context context) {
        this(context, null);
    }

    public GoodListWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
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
        mBinding.list.addItemDecoration(new SpaceItemDecoration(spacingInPixels, 0));
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        mBinding.list.setLayoutManager(lm);


        mMyPositionBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.item_my_position, mBinding.list, false);
        mAdapter.addHeaderView(mMyPositionBinding.getRoot(), 0, LinearLayout.HORIZONTAL);
        mAdapter.loadMoreComplete();
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            GoodType goodType = (GoodType) adapter.getItem(position);
            if (mCallback != null) {
                mCallback.callback(goodType);
            }
        });
    }

    public void setCallback(CommonCallback<GoodType> callback) {
        mCallback = callback;
    }

    public void setGood(GoodType good){
        mGoodType = good;
        refreshData();
    }

    public void refreshData(){

        if(mGoodType!=null&&mGoodType.goodsTypeName!=null){
            for(GoodType item:mData){
                if(mGoodType.goodsTypeName.equals(item.goodsTypeName)){
                    item.mIsSelect = true;
                }else{
                    item.mIsSelect= false;
                }
            }
        }
        mAdapter.notifyDataSetChanged();
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
            refreshData();

        });
    }


}
