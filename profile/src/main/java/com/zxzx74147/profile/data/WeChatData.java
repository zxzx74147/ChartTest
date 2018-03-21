package com.zxzx74147.profile.data;

import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.data.UserData;

import java.io.Serializable;

/**
 * Created by zhengxin on 2018/2/28.
 */

public class WeChatData extends UniApiData implements Serializable{

   public String portraitUrl;
   public String nickName;
   public String code;
   public UserData user;
}
