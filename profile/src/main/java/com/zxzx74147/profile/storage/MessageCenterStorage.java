package com.zxzx74147.profile.storage;

import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.profile.data.MessageListData;
import com.zxzx74147.profile.data.UserUniData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zhengxin on 2018/2/15.
 */

public interface MessageCenterStorage {

    @GET("/msgcenter/getlist")
    Observable<MessageListData> msgcenterGetList(@Query("page") int page);



}
