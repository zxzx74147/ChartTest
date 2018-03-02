package com.zxzx74147.balance.storage;

import com.zxzx74147.balance.data.PayNewData;
import com.zxzx74147.balance.data.PayResultData;
import com.zxzx74147.balance.data.PayUniData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zhengxin on 2018/3/1.
 */

public interface PayStorage {

    @GET("/pay/wechat")
    Observable<PayNewData> payWechat(@Query("amount") int amount);

    @GET("/pay/alipay")
    Observable<PayNewData> payAli(@Query("amount") int amount);

    @GET("/pay/quickpay")
    Observable<PayUniData> payUn(@Query("amount") int amount);

    @GET("/pay/verify")
    Observable<PayResultData> payVerify(@Query("depositId") long depositId);
}
