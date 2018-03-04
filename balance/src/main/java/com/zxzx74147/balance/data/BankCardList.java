package com.zxzx74147.balance.data;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhengxin on 2018/3/1.
 */

public class BankCardList implements Serializable {
   public int  num      ;   //银行卡ID
   public List<BankCard> bankCard;
}
