
package com.zxzx74147.devlib.data;

import java.io.Serializable;

public class Config implements Serializable {

    public String userGuideUrl;     //新手课堂Url
    public String userProtocolUrl;     //用户协议Url
    public String activityJumpUrl;     //直播活动跳转Url（如果为空，则代表活动关闭）
    public String activityIconUrl;     //直播活动IconUrl
    public String activityPicUrl;     //直播活动弹层图片Url

}
