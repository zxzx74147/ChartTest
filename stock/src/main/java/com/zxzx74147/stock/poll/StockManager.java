package com.zxzx74147.stock.poll;

import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.stock.data.KLineData;
import com.zxzx74147.stock.storage.StockStorage;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by zhengxin on 2018/2/8.
 */

public class StockManager {

    private static StockManager mInstance;

    private StockManager() {

    }


    private StockStorage mStockStorage = RetrofitClient.getStockClient().create(StockStorage.class);

    public static StockManager sharedInstance() {
        if (mInstance == null) {
            mInstance = new StockManager();
        }
        return mInstance;
    }

    public void registStock(int type) {

    }
}
