package com.zxzx74147.stock.storage;

import com.zxzx74147.stock.data.MachPositionData;
import com.zxzx74147.stock.data.MachPositionListData;
import com.zxzx74147.stock.data.PositionData;
import com.zxzx74147.stock.data.PositionListData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zhengxin on 2018/2/8.
 */

public interface TradesStorage {

    //machposition
    @GET("/trades/machposition/cancel")
    Observable<MachPositionData> machpositionCancel(@Query("machPositionId") long machPositionId);

    @GET("/trades/machposition/modify")
    Observable<MachPositionData> machpositionModify(@Query("machPositionId") long machPositionId,@Query("goodsId") String goodsId, @Query("buySell")
            int buySell, @Query("amount") int amount, @Query("price") float price, @Query("limit") String limit
            , @Query("stop") String stop, @Query("error") String error, @Query("deferred") int deferred);


    @GET("/trades/machposition/open")
    Observable<MachPositionData> machpositionOpen(@Query("goodsId") String goodsId, @Query("buySell")
            int buySell, @Query("amount") int amount, @Query("price") float price, @Query("limit") String limit
            , @Query("stop") String stop, @Query("error") String error, @Query("deferred") int deferred);


    @GET("/trades/machposition/getlist")
    Observable<MachPositionListData> machpositionGetList(@Query("page") int page);

    @GET("/trades/machposition/gethislist")
    Observable<MachPositionListData> machpositionGetHisList(@Query("page") int page);





    @GET("/trades/position/close")
    Observable<PositionData> positionClose(@Query("positionId") long positionId,@Query("price") float price);

    @GET("/trades/position/open")
    Observable<PositionData> positionOpen(@Query("goodsId") String goodsId, @Query("buySell")
            int buySell, @Query("amount") int amount, @Query("price") String price, @Query("limit") String limit
            , @Query("stop") String stop, @Query("error") String error, @Query("deferred") int deferred);

    @GET("/trades/position/voucheropen")
    Observable<PositionData> voucherOpen(@Query("goodsId") String goodsId, @Query("buySell")
            int buySell, @Query("amount") int amount, @Query("price") String price, @Query("limit") String limit
            , @Query("stop") String stop, @Query("error") String error, @Query("deferred") int deferred);


    @GET("/trades/position/modify")
    Observable<PositionData> positionModify(@Query("positionId") long positionId, @Query("limit")
            String limit, @Query("stop") String stop, @Query("deferred") int deferred);


    @GET("/trades/position/getlist")
    Observable<PositionListData> positionGetList(@Query("page") int page);

    @GET("/trades/position/gethislist")
    Observable<PositionListData> positionGetHisList(@Query("page") int page);
}
