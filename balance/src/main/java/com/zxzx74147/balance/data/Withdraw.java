package com.zxzx74147.balance.data;

import java.io.Serializable;

/**
 * Created by zhengxin on 2018/3/12.
 */

public class Withdraw implements Serializable {
    public long withdrawId      ;   //提现ID
   public float amount          ;      //提现金额，单位（元）
    public float  charge          ;      //提现手续费，单位（元）
  public String  withdrawTime    ;     //提现时间
    public String  handleTime      ;     //处理时间
    public String bank            ;     //银行
    public String cardNo          ;     //银行卡号
    public String name            ;     //持卡人姓名
    public String comment         ;     //备注
    public int  status          ;   //提现状态：1-处理中，2-成功，3-失败
}
