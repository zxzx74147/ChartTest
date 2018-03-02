package com.zxzx74147.stock.viewmodel;

import android.arch.lifecycle.LiveData;

import com.zxzx74147.devlib.base.BaseViewModel;
import com.zxzx74147.stock.data.GoodListData;
import com.zxzx74147.stock.data.GoodListLiveData;

/**
 * Created by zhengxin on 2018/2/8.
 */

public class GoodViewModel extends BaseViewModel {

    private GoodListLiveData mGoodLiveData = new GoodListLiveData(null);


    public LiveData<GoodListData> getGoodLiveData() {
        return mGoodLiveData;
    }


}
