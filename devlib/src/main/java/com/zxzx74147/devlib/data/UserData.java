package com.zxzx74147.devlib.data;

import java.io.Serializable;

/**
 * Created by zhengxin on 2018/2/20.
 */

public class UserData implements Serializable {
    public String uId;     //用户标识
    public String phone;      //手机号
    public String nickName;   //昵称（新注册用户 可能为空）
    public String portraitUrl;      //头像（新注册用户 可能为空）
    public float total;       //账户总余额
    public float balance;         //账户现金余额
    public float coupon;          //账户红包余额
    public String couponDeadline;      //红包有效期
    public int hasTradePasswd;     //是否有交易密码
    public int needTradePasswd;    //是否需要输入交易密码（每6小时需要输入一次）
}
