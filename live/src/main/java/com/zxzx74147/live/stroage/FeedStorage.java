package com.zxzx74147.live.stroage;

import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.live.data.HomeData;
import com.zxzx74147.live.data.LikeData;
import com.zxzx74147.live.data.LiveMsgListData;
import com.zxzx74147.live.data.ReadData;
import com.zxzx74147.live.data.ReplyData;
import com.zxzx74147.live.data.ReplyListData;
import com.zxzx74147.live.data.ReplyNumData;
import com.zxzx74147.live.data.TeacherLiveListData;
import com.zxzx74147.live.data.TextListData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zhengxin on 2018/2/15.
 */

public interface FeedStorage {

    @GET("/live/feed/getlist")
    Observable<TextListData> feedGetList(@Query("page") int page);

    @GET("/live/feed/love")
    Observable<LikeData> feedLove(@Query("textId") long textId);

    @GET("/live/feed/hate")
    Observable<LikeData> feedHate(@Query("textId") long textId);


    @GET("/live/feed/read")
    Observable<ReadData> feedRead(@Query("textId") long textId);

    @GET("/live/reply/getlist")
    Observable<ReplyListData> replyGetList(@Query("textId") long textId, @Query("page") int page);

    @GET("/live/reply/add")
    Observable<ReplyData> replyAdd(@Query("textId") long textId, @Query("content") String content);


    @GET("/live/reply/del")
    Observable<ReplyNumData> replyDel(@Query("replyId") long replyId);


}
