package com.zxzx74147.live.layout;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.charttest.R;
import com.zxzx74147.charttest.databinding.ActivityLiveBinding;
import com.zxzx74147.charttest.databinding.LayoutTinyTradeBinding;
import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.modules.account.UserViewModel;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.live.data.Live;
import com.zxzx74147.live.media.IjkVideoViewHolder;
import com.zxzx74147.live.stroage.LiveStorage;
import com.zxzx74147.live.viewmodel.LiveMsgViewModel;

import io.reactivex.functions.Consumer;

public class LayoutTinyTrade extends LinearLayout {
    private LayoutTinyTradeBinding mBinding = null;


    public LayoutTinyTrade(Context context) {
        super(context);
        init();
    }

    public LayoutTinyTrade(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LayoutTinyTrade(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),R.layout.layout_tiny_trade,this,true);

    }
}
