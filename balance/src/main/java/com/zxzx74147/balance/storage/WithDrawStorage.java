package com.zxzx74147.balance.storage;

import com.zxzx74147.balance.data.DepositListData;
import com.zxzx74147.balance.data.WithdrawData;
import com.zxzx74147.balance.data.WithdrawListData;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by zhengxin on 2018/3/1.
 */

public interface WithDrawStorage {

    @FormUrlEncoded
    @POST("/withdraws/record/getlist")
    Observable<WithdrawListData> withdrawList(@Field("page") int page);

    @FormUrlEncoded
    @POST("/withdraws/bankcard/cash")
    Observable<WithdrawData> withdrawCase(@Field("bankCardId") int bankCardId, @Field("amount") int amount);


}
