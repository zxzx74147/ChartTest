package com.zxzx74147.balance.data;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by zhengxin on 2018/3/1.
 */

public class BankCard implements Serializable {
   public int  bankCardId      ;   //银行卡ID
   public String  bank            ;     //银行
   public String  branchBank      ;     //支行
  public String   province        ;     //省份
  public String   city            ;     //城市
   public String  cardNo          ;     //银行卡号
   public String  name            ;     //持卡人姓名

    public String getBankSecu(){
        if(TextUtils.isEmpty(cardNo)||cardNo.length()<4){
            return "*";
        }
        return "*"+cardNo.substring(cardNo.length()-4,cardNo.length());
    }
}
