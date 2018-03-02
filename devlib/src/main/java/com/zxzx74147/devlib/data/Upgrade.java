package com.zxzx74147.devlib.data;

/**
 * Created by zhengxin on 2018/2/28.
 */

public class Upgrade extends UniApiData {
    public int show;   //是否提示版本更新（1:是，0:否）
    public int force;   //是否强制版本更新（1:是，0:否）
    public String msg;     //版本更新文案
    public String url;              //下载链接
}
