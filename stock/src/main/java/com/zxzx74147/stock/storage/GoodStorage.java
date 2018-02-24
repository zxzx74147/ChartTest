package com.zxzx74147.stock.storage;

import com.zxzx74147.stock.data.GoodListData;
import com.zxzx74147.stock.data.PriceListData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zhengxin on 2018/2/8.
 */

public interface GoodStorage {

    @GET("/infos/goods/getlist")
    Observable<GoodListData> getGoods(@Query("goodsType") String goodsType);

    @GET("/prices/realtime/getlist")
    Observable<PriceListData> getRealTime(@Query("goodsType") String goodsType);

}
