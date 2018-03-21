package com.zxzx74147.stock.widget;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.image.ImageLoader;
import com.zxzx74147.devlib.interfaces.IViewModelHolder;
import com.zxzx74147.devlib.modules.account.UserViewModel;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.devlib.widget.SpaceItemDecoration;
import com.zxzx74147.profile.data.UserUniData;
import com.zxzx74147.stock.R;
import com.zxzx74147.stock.data.GoodType;
import com.zxzx74147.stock.databinding.ItemGoodLiteBinding;
import com.zxzx74147.stock.databinding.ItemMyPositionBinding;
import com.zxzx74147.stock.databinding.WidgetGoodListBinding;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhengxin on 2018/2/8.
 */

public class GoodLiteWidget extends FrameLayout implements IViewModelHolder {
    private ItemGoodLiteBinding mBinding = null;
    private UserViewModel mModel = null;
    private String mGoodType ;
    private UserUniData mUniData = null;


    @BindingAdapter({"goodType"})
    public static void setGood(GoodLiteWidget view, String goodType) {
        view.setGoodType(goodType);
    }

    @BindingAdapter({"goodName"})
    public static void setGoodName(GoodLiteWidget view, String goodName) {
        view.setGoodname(goodName);
    }

    private void setGoodname(String goodName) {
        mBinding.setGoodName(goodName);
    }

    public GoodLiteWidget(Context context) {
        this(context, null);
    }

    public GoodLiteWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoodLiteWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.item_good_lite, this, true);
        mModel = ViewModelProviders.of(ViewUtil.getFragmentActivity(this)).get(UserViewModel.class);
        mUniData = mModel.getUserUniLiveData().getValue();
    }



    public void setGood(GoodType good){
        mBinding.setData(good);
        refreshData();
    }

    public void setGoodType(String goodType){
        mGoodType = goodType;
        refreshData();
    }

    public void refreshData(){
        if(mUniData==null||mGoodType==null||mUniData.goodsTypeList==null||mUniData.goodsTypeList.goodType==null){
            return;
        }
        for(GoodType good:mUniData.goodsTypeList.goodType){
            if(mGoodType.equals(good.goodsType)){
                mBinding.setData(good);
                return;
            }
        }
    }





    @Override
    public void setProvider(ViewModelProvider provider) {

    }

    @Override
    public void setLifeCircle(LifecycleOwner owner) {
        mModel.getUserUniLiveData().observe(owner, userUniData -> {
            if (userUniData.hasError()) {
                return;
            }
            mUniData = userUniData;
            refreshData();

        });
    }
}
