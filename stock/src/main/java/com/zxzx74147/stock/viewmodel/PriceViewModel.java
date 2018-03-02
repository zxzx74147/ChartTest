package com.zxzx74147.stock.viewmodel;

import android.arch.lifecycle.LiveData;

import com.zxzx74147.devlib.base.BaseViewModel;
import com.zxzx74147.stock.data.GoodListData;
import com.zxzx74147.stock.data.GoodListLiveData;
import com.zxzx74147.stock.data.PriceListLiveData;

/**
 * Created by zhengxin on 2018/2/8.
 */

public class PriceViewModel extends BaseViewModel {

    private GoodListLiveData mGoodLiveData = new GoodListLiveData(null);
    private PriceListLiveData mReadTimeLiveData = new PriceListLiveData();

    public LiveData<GoodListData> getGoodLiveData() {
        return mGoodLiveData;
    }

    public PriceListLiveData getReadTimeLiveData() {
        return mReadTimeLiveData;
    }


}
