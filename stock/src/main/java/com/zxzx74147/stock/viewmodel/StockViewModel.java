package com.zxzx74147.stock.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.zxzx74147.devlib.base.BaseViewModel;
import com.zxzx74147.stock.data.KLineLiveData;

import java.util.List;

/**
 * Created by zhengxin on 2018/2/8.
 */

public class StockViewModel extends BaseViewModel{

    public KLineLiveData mKLineData = null;


    public KLineLiveData getKLineData(){
       return mKLineData;
    }
}
