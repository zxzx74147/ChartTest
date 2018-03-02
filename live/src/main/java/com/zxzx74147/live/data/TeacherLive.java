
package com.zxzx74147.live.data;

import java.io.Serializable;

public class TeacherLive implements Serializable {

    public int liveId;   //直播ID
    public int status;   //直播状态（0:未开始，1:直播中，1:已开始）
    public int startTime;   //开始时间
    public int endTime;   //结束时间

}
