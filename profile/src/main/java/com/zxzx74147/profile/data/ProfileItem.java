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

public class ProfileItem implements Serializable{

    public int uread;
    public String content;

}
