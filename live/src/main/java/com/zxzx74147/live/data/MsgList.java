package com.zxzx74147.live.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhengxin on 2018/2/26.
 */

public class MsgList implements Serializable {

    public int num;   //消息ID
    public List<Msg> msg;
}
