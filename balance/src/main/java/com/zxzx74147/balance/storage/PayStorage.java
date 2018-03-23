package com.zxzx74147.balance.storage;

import com.zxzx74147.balance.data.PayNewData;
import com.zxzx74147.balance.data.PayResultData;
import com.zxzx74147.balance.data.PayUniData;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by zhengxin on 2018/3/1.
 */

public interface PayStorage {

    @FormUrlEncoded
    @POST("/pay/wechat")
    Observable<PayNewData> payWechat(@Field("amount") int amount);

    @FormUrlEncoded
    @POST("/pay/alipay")
    Observable<PayNewData> payAli(@Field("amount") int amount);

    @FormUrlEncoded
    @POST("/pay/quickpay")
    Observable<PayUniData> payUn(@Field("amount") int amount);

    @FormUrlEncoded
    @POST("/pay/verify")
    Observable<PayResultData> payVerify(@Field("depositId") long depositId);
}
