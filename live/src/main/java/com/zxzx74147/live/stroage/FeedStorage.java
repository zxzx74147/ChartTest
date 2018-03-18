package com.zxzx74147.live.stroage;

import com.zxzx74147.live.data.LikeData;
import com.zxzx74147.live.data.ReadData;
import com.zxzx74147.live.data.ReplyData;
import com.zxzx74147.live.data.ReplyListData;
import com.zxzx74147.live.data.ReplyNumData;
import com.zxzx74147.live.data.TextListData;

import io.reactivex.Observable;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Field;

/**
 * Created by zhengxin on 2018/2/15.
 */

public interface FeedStorage {

    @FormUrlEncoded
    @POST("/live/feed/getlist")
    Observable<TextListData> feedGetList(@Field("page") int page);

    @FormUrlEncoded
    @POST("/live/feed/love")
    Observable<LikeData> feedLove(@Field("textId") long textId);

    @FormUrlEncoded
    @POST("/live/feed/hate")
    Observable<LikeData> feedHate(@Field("textId") long textId);


    @FormUrlEncoded
    @POST("/live/feed/read")
    Observable<ReadData> feedRead(@Field("textId") long textId);

    @FormUrlEncoded
    @POST("/live/reply/getlist")
    Observable<ReplyListData> replyGetList(@Field("textId") long textId, @Field("page") int page);

    @FormUrlEncoded
    @POST("/live/reply/add")
    Observable<ReplyData> replyAdd(@Field("textId") long textId, @Field("content") String content);


    @FormUrlEncoded
    @POST("/live/reply/del")
    Observable<ReplyNumData> replyDel(@Field("replyId") long replyId);


}
