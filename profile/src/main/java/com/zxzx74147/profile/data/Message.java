package com.zxzx74147.profile.data;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.zxzx74147.devlib.DevLib;
import com.zxzx74147.profile.R;

import java.io.Serializable;

/**
 * Created by zhengxin on 2018/2/28.
 */

public class Message implements Serializable{

    public int msgId;   //消息ID
    @SerializedName("abstract")
    @Expose
    public String abs;     //摘要
    public String title;
    public String content;     //正文
    public String createTime;   //时间
    public String bottom;

    public Spannable getUnreadTitle(){

        ImageSpan imageSpan = new ImageSpan(DevLib.getApp(), R.drawable.rect_round_red_point, DynamicDrawableSpan.ALIGN_BASELINE);
//        imageSpan.
        SpannableString spannableString = new SpannableString("  "+title);
        spannableString.setSpan(imageSpan,0,1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

}
