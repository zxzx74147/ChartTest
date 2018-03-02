package com.zxzx74147.live.data;

import java.io.Serializable;

/**
 * Created by zhengxin on 2018/2/26.
 */

public class Msg implements Serializable {

    public int msgId;   //消息ID
    public int type;   //消息类型(1:文字消息，2:盈利文字消息，3:盈利弹幕消息)
    public String nickName;     //用户昵称
    public String content;     //文字内容
    public int profitNum;
}
