package com.zxzx74147.stock.storage;

import com.zxzx74147.stock.data.KLineData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zhengxin on 2018/2/8.
 */

public interface StockStorage {


    @GET("/prices/kchart/getlist")
    Observable<KLineData> getKLine(@Query("goodsType") String goodsType, @Query("chartType") int chartType);

    @GET("/prices/timeline/getlist")
    Observable<KLineData> getLiveLine(@Query("goodsType") String goodsType);
}
