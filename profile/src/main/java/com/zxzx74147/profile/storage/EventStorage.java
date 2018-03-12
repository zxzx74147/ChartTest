package com.zxzx74147.profile.storage;

import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.profile.data.VocherListData;
import com.zxzx74147.profile.data.VocherUserListData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zhengxin on 2018/2/15.
 */

public interface EventStorage {

    @GET("/events/voucher/getmylist")
    Observable<VocherUserListData> eventsVoucherMyList(@Query("page") int page);

    @GET("/events/voucher/getlist")
    Observable<VocherListData> eventsVoucherList();

    @GET("/events/voucher/pay")
    Observable<UniApiData> eventsVoucherPay(@Query("voucherId") String voucherId);


}
