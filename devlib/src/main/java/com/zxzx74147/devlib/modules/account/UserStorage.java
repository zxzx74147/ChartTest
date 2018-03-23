package com.zxzx74147.devlib.modules.account;

import com.zxzx74147.profile.data.UserUniData;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by zhengxin on 2018/3/1.
 */

public interface UserStorage {



    @FormUrlEncoded
    @POST("/user/account/get")
    Observable<UserUniData> accountGet(@Field("a") String a);


    @FormUrlEncoded
    @POST("/user/account/update")
    Observable<UserUniData> accountUpdate(@Field("nickName") String nickName,@Field("portraitUrl") String portraitUrl);
}
