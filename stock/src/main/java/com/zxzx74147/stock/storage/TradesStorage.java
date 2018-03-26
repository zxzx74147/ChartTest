package com.zxzx74147.stock.storage;

import com.zxzx74147.stock.data.MachPositionData;
import com.zxzx74147.stock.data.MachPositionListData;
import com.zxzx74147.stock.data.PositionData;
import com.zxzx74147.stock.data.PositionListData;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by zhengxin on 2018/2/8.
 */

public interface TradesStorage {

    //machposition
    @FormUrlEncoded
    @POST("/trades/machposition/cancel")
    Observable<MachPositionData> machpositionCancel(@Field("machPositionId") long machPositionId);

    @FormUrlEncoded
    @POST("/trades/machposition/modify")
    Observable<MachPositionData> machpositionModify(@Field("machPositionId") long machPositionId,@Field("goodsId") String goodsId, @Field("buySell")
            int buySell, @Field("amount") int amount, @Field("price") String price, @Field("limit") String limit
            , @Field("stop") String stop, @Field("error") String error, @Field("deferred") int deferred);


    @FormUrlEncoded
    @POST("/trades/machposition/open")
    Observable<MachPositionData> machpositionOpen(@Field("goodsId") String goodsId, @Field("buySell")
            int buySell, @Field("amount") int amount, @Field("price") String price, @Field("limit") String limit
            , @Field("stop") String stop, @Field("error") String error, @Field("deferred") int deferred);


    @FormUrlEncoded
    @POST("/trades/machposition/getlist")
    Observable<MachPositionListData> machpositionGetList(@Field("page") int page);

    @FormUrlEncoded
    @POST("/trades/machposition/gethislist")
    Observable<MachPositionListData> machpositionGetHisList(@Field("page") int page);





    @FormUrlEncoded
    @POST("/trades/position/close")
    Observable<PositionData> positionClose(@Field("positionId") long positionId,@Field("price") float price);

    @FormUrlEncoded
    @POST("/trades/position/open")
    Observable<PositionData> positionOpen(@Field("goodsId") String goodsId, @Field("buySell")
            int buySell, @Field("amount") int amount, @Field("price") String price, @Field("limit") String limit
            , @Field("stop") String stop, @Field("error") String error, @Field("deferred") int deferred);

    @FormUrlEncoded
    @POST("/trades/position/voucheropen")
    Observable<PositionData> voucherOpen(@Field("goodsId") String goodsId, @Field("buySell")
            int buySell, @Field("amount") int amount, @Field("price") String price, @Field("limit") String limit
            , @Field("stop") String stop, @Field("error") String error, @Field("deferred") int deferred);



    @FormUrlEncoded
    @POST("/trades/position/comvoucheropen")
    Observable<PositionData> comvoucherOpen(@Field("goodsType") String goodsType, @Field("buySell")
            int buySell, @Field("limit") String limit, @Field("stop") String stop);


    @FormUrlEncoded
    @POST("/trades/position/modify")
    Observable<PositionData> positionModify(@Field("positionId") long positionId, @Field("limit")
            String limit, @Field("stop") String stop, @Field("deferred") int deferred);


    @FormUrlEncoded
    @POST("/trades/position/getlist")
    Observable<PositionListData> positionGetList(@Field("page") int page);

//    @FormUrlEncoded
//    @POST("/user/account/get")
//    Observable<PositionListData> positionGetList(@Field("page") int page);

    @FormUrlEncoded
    @POST("/trades/position/gethislist")
    Observable<PositionListData> positionGetHisList(@Field("page") int page);
}
