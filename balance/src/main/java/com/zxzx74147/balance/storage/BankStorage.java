package com.zxzx74147.balance.storage;

import com.zxzx74147.balance.data.BankCard;
import com.zxzx74147.balance.data.BankCardData;
import com.zxzx74147.balance.data.BankCardListData;
import com.zxzx74147.balance.data.BankListData;
import com.zxzx74147.devlib.data.UniApiData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zhengxin on 2018/3/1.
 */

public interface BankStorage {


    @GET("/infos/bank/getlist")
    Observable<BankListData> bankList();

    @GET("/user/bankcard/getlist")
    Observable<BankCardListData> myCardList();


    @GET("/user/binding/add")
    Observable<BankCardData> bindAdd(@Query("bank") String bank, @Query("cardNo") String cardNo, @Query("name") String name, @Query("vcode") String vcode);


    @GET("/user/binding/getvcode")
    Observable<UniApiData> bindVCode();

    @GET("/user/binding/getvoicevcode")
    Observable<UniApiData> bindVCodeVoice();


    @GET("/user/unbinding/remove")
    Observable<UniApiData> bindRemove(@Query("bankCardId") int bankCardId,@Query("vcode") String vcode);

    @GET("/user/unbinding/getvcode")
    Observable<UniApiData> unbindVCode();

    @GET("/user/unbinding/getvoicevcode")
    Observable<UniApiData> unbindVCodeVoice();

}
