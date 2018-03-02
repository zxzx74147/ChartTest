package com.zxzx74147.profile.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by zhengxin on 2018/2/28.
 */

public class Message implements Serializable{

    public int msgId;   //消息ID
    @SerializedName("abstract")
    @Expose
    public String abs;     //摘要
    public String content;     //正文
    public int createTime;   //时间
}
