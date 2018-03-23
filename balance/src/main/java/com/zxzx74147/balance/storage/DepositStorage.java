package com.zxzx74147.balance.storage;

import com.zxzx74147.balance.data.BankListData;
import com.zxzx74147.balance.data.DepositList;
import com.zxzx74147.balance.data.DepositListData;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by zhengxin on 2018/3/1.
 */

public interface DepositStorage {

    @FormUrlEncoded
    @POST("/deposits/record/getlist")
    Observable<DepositListData> depositList(@Field("page") int page);

}
