package com.zxzx74147.profile.storage;


import com.zxzx74147.devlib.data.SysInitData;
import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.data.UpPicData;
import com.zxzx74147.devlib.data.Upgrade;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by zhengxin on 2018/2/26.
 */

public interface SysStorage {
    @FormUrlEncoded
    @POST("/sys/init?deviceOs=Android")
    Observable<SysInitData> sysInit(@Field("channel") String channel, @Field("deviceId") String deviceId, @Field("deviceToken") String deviceToken, @Field("deviceType") String deviceType, @Field("version") String version);

    @Multipart
    @POST("/sys/uppic")
    Observable<UpPicData> uploadPic(@Part MultipartBody.Part file);

    @GET("/sys/resume")
    Observable<UniApiData> resume();

}
