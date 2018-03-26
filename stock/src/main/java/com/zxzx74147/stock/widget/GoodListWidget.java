package com.zxzx74147.stock.widget;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.interfaces.IViewModelHolder;
import com.zxzx74147.devlib.modules.account.UserViewModel;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.devlib.widget.SpaceItemDecoration;
import com.zxzx74147.stock.R;
import com.zxzx74147.stock.data.GoodType;
import com.zxzx74147.stock.databinding.ItemMyPositionBinding;
import com.zxzx74147.stock.databinding.WidgetGoodListBinding;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhengxin on 2018/2/8.
 */

public class GoodListWidget extends RelativeLayout implements IViewModelHolder {
    private WidgetGoodListBinding mBinding = null;
    private ItemMyPositionBinding mMyPositionBinding = null;
    private UserViewModel mModel = null;
    private CommonCallback<GoodType> mCallback = null;
    private GoodType mGoodType = null;
    private boolean mNeedMyPosition;


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
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.GoodListWidget, defStyleAttr, 0);
        mNeedMyPosition = a.getBoolean(R.styleable.GoodListWidget_need_my_position, true);
        a.recycle();
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

        if(mNeedMyPosition) {
            mMyPositionBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.item_my_position, mBinding.list, false);
            mAdapter.addHeaderView(mMyPositionBinding.getRoot(), 0, LinearLayout.HORIZONTAL);
        }
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

    public void scrollToCurrent(){
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mBinding.list.scrollToItem(mGoodType);
            }
        },50);

    }

    public void refreshData(){

        if(mGoodType!=null&&mGoodType.goodsTypeName!=null){
            for(GoodType item:mData){
                if(mGoodType.goodsTypeName.equals(item.goodsTypeName)){
                    item.mIsSelect = true;
                    mGoodType = item;
                }else{
                    item.mIsSelect= false;
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    public void clearSelect(){
        for(GoodType item:mData){
                item.mIsSelect= false;
        }
    }


    private void init() {
        initView();

    }



    @Override
    public void setProvider(ViewModelProvider provider) {
        mModel = provider.get(UserViewModel.class);
        if(mModel.getUserUniLiveData().getValue()!=null&&mModel.getUserUniLiveData().getValue().goodsTypeList!=null) {
            mData.addAll(mModel.getUserUniLiveData().getValue().goodsTypeList.goodType);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setLifeCircle(LifecycleOwner owner) {
        mModel.getUserUniLiveData().observe(owner, userUniData -> {

            if (userUniData==null||userUniData.hasError()) {
                return;
            }
            if(mMyPositionBinding!=null) {
                mMyPositionBinding.setUserUni(userUniData);
            }

            mData.clear();
            if (userUniData.goodsTypeList == null||userUniData.goodsTypeList.goodType==null) {
                return;
            }
            for(GoodType type:userUniData.goodsTypeList.goodType)
                mData.add(type.clone());
            refreshData();

        });
    }
}
