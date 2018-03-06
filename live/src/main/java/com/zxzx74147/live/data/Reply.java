
package com.zxzx74147.live.data;

import com.zxzx74147.devlib.data.UserData;

import java.io.Serializable;

public class Reply implements Serializable
{

    public long replyId;
    public int isSelf;
    public String content;
    public int createTime;
    public UserData user;
    private final static long serialVersionUID = -7942854245857739526L;

}
