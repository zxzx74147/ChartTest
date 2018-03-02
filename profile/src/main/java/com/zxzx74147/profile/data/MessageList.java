package com.zxzx74147.profile.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhengxin on 2018/2/28.
 */

public class MessageList implements Serializable {
    public int num             ;   //
    public int hasMore         ;   //是否有更多(0:没有，1:有)
    public int nextPage        ;   //
    public List<Message> msg;
}
