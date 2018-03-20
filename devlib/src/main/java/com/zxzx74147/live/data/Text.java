
package com.zxzx74147.live.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Text implements Serializable
{

    public long textId;
    public int type;
    public String content;
    @SerializedName("abstract")
    @Expose
    public String abs;
    public String picUrl;
    public int readNum;
    public int replyNum;
    public int loveNum;
    public int isLove;
    public int createTime;
    public Teacher teacher;
    private final static long serialVersionUID = -7584249107791544605L;

}
