package com.zxzx74147.devlib.modules.account;

import com.zxzx74147.profile.data.UserUniData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zhengxin on 2018/3/1.
 */

public interface UserStorage {



    @GET("/user/account/get")
    Observable<UserUniData> accountGet();


    @GET("/user/account/update")
    Observable<UserUniData> accountUpdate(@Query("nickName") String nickName,@Query("portraitUrl") String portraitUrl);
}
