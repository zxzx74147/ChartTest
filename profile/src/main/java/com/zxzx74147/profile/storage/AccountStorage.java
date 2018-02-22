package com.zxzx74147.profile.storage;

import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.profile.data.UserUniData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zhengxin on 2018/2/15.
 */

public interface AccountStorage {

    @GET("/user/regist/add")
    Observable<UniApiData> registAdd(@Query("phone") String phone, @Query("passwd") String passwd, @Query("vcode") String vcode);

    @GET("/user/regist/getvcode")
    Observable<UniApiData> registGetVCode(@Query("phone") String phone);

    @GET("/user/regist/getvoicevcode")
    Observable<UniApiData> registGetVoiceVCode(@Query("phone") String phone);

    @GET("/user/password/getvcode")
    Observable<UniApiData> passwordGetVCode(@Query("phone") String phone);

    @GET("/user/password/getvoicevcode")
    Observable<UniApiData> passwordGetVoiceVCode(@Query("phone") String phone);

    @GET("/user/password/reset")
    Observable<UniApiData> passwordReset(@Query("phone") String phone,@Query("passwd") String passwd,@Query("vcode") String vcode);

    @GET("/user/account/getvcode")
    Observable<UniApiData> accountGetVCode(@Query("phone") String phone);

    @GET("/user/account/login?deviceOs=Android")
    Observable<UserUniData> acctountLogin(@Query("phone") String phone, @Query("vcode") String vcode, @Query("deviceId") String deviceId, @Query("version") String version);





}
