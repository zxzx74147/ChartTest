package com.zxzx74147.live.stroage;

import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.live.data.HomeData;
import com.zxzx74147.live.data.LiveMsgListData;
import com.zxzx74147.live.data.TeacherLiveListData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zhengxin on 2018/2/15.
 */

public interface LiveStorage {

    @GET("/live/room/getlist ")
    Observable<HomeData> roomGetList();

    @GET("/live/room/join")
    Observable<UniApiData> roomJoin(@Query("liveId") long liveId);

    @GET("/live/room/love")
    Observable<UniApiData> roomLove(@Query("liveId") long liveId);

    @GET("/live/room/hate")
    Observable<UniApiData> roomHate(@Query("liveId") long liveId);

    @GET("/live/room/quit")
    Observable<UniApiData> roomQuite(@Query("liveId") long liveId);


    //TODO
    @GET("/live/msg/getlist")
    Observable<LiveMsgListData> msgList(@Query("liveId") long liveId, @Query("lastMId") long lastMId);


    @GET("/live/teacher/livelist")
    Observable<TeacherLiveListData> teacherLiveList(@Query("teacherId") int teacherId);

    @GET("/live/teacher/hate")
    Observable<UniApiData> teacherHate(@Query("teacherId") int teacherId);

    @GET("/live/teacher/love")
    Observable<UniApiData> teacherLove(@Query("teacherId") int teacherId);





}
