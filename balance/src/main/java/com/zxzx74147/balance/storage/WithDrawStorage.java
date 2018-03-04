package com.zxzx74147.balance.storage;

import com.zxzx74147.balance.data.DepositListData;
import com.zxzx74147.balance.data.WithdrawData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zhengxin on 2018/3/1.
 */

public interface WithDrawStorage {

    @GET("/withdraws/record/getlist")
    Observable<DepositListData> withdrawList(@Query("page") int page);

    @GET("/withdraws/bankcard/cash")
    Observable<WithdrawData> withdrawCase(@Query("bankCardId") int bankCardId, @Query("amount") int amount);


}
