package com.zxzx74147.balance.storage;

import com.zxzx74147.balance.data.BankListData;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by zhengxin on 2018/3/1.
 */

public interface BankStorage {

    @GET("/infos/bank/getlist")
    Observable<BankListData> bankList();

}
