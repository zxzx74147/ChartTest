package com.zxzx74147.balance.data;

import com.zxzx74147.devlib.data.UniApiData;

import java.io.Serializable;

/**
 * Created by zhengxin on 2018/3/1.
 */

public class PayNewData extends UniApiData implements Serializable {
    public long depositId;   //订单号
    public String codeUrl;     //二维码跳转（weixin://wxpay/bizpayurl?pr=re3tCuK）
    public String codeImg;     //二维码二进制（Base64 Encode）
}
