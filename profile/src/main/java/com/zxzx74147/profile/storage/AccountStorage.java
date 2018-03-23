package com.zxzx74147.profile.storage;

import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.profile.data.AuthApplyData;
import com.zxzx74147.profile.data.UserUniData;
import com.zxzx74147.profile.data.WeChatData;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    @FormUrlEncoded
    @POST("/user/account/getvcode")
    Observable<UniApiData> accountGetVCode(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("/user/account/getvcode")
    Observable<UniApiData> accountGetVCodeWithCode(@Field("phone") String phone,@Field("code") String code);


    @FormUrlEncoded
    @POST("/user/account/login")
    Observable<UserUniData> acctountLogin(@Field("phone") String phone, @Field("vcode") String vcode,@Field("deviceId") String deviceId, @Field("version") String version,@Field("deviceType") String deviceType,@Field("channel") String channel,@Field("deviceToken") String deviceToken);

    @FormUrlEncoded
    @POST("/user/account/login")
    Observable<UserUniData> acctountLoginWithCode(@Field("phone") String phone,@Field("vcode") String vcode,@Field("code") String code, @Field("deviceId") String deviceId, @Field("version") String version,@Field("deviceType") String deviceType,@Field("channel") String channel,@Field("deviceToken") String deviceToken);


    @FormUrlEncoded
    @POST("/user/trades/password")
    Observable<UserUniData> tradePassword(@Field("passwd") String passwd);

    @FormUrlEncoded
    @POST("/user/trades/modify")
    Observable<UserUniData> tradePasswordModify(@Field("passwd") String passwd,@Field("vcode") String vcode);

    @FormUrlEncoded
    @POST("/user/trades/getvcode")
    Observable<UniApiData> tradePasswordVcode(@Field("a") String a);

    @FormUrlEncoded
    @POST("/user/trades/login")
    Observable<UserUniData> tradeLogin(@Field("passwd") String passwd);

    @GET("/user/account/logout")
    Observable<UniApiData> logout();



    @FormUrlEncoded
    @POST("/user/simauth/apply")
    Observable<AuthApplyData> authApply(@Field("name") String name, @Field("identityNo") String identityNo);

    @FormUrlEncoded
    @POST("/user/wechat/login")
    Observable<WeChatData> wechatLogin(@Field("code") String phone, @Field("deviceId") String deviceId, @Field("version") String version, @Field("deviceType") String deviceType, @Field("channel") String channel, @Field("deviceToken") String deviceToken);





}
