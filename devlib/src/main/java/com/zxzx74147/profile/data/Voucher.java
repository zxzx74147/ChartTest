
package com.zxzx74147.profile.data;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.zxzx74147.devlib.image.ImageLoader;
import com.zxzx74147.stock.data.Good;

import java.io.Serializable;

public class Voucher implements Serializable {

    public String voucherId;
    public String goodsId;
    public String getTime;
    public String validStartDt;
    public String validEndDt;
    public String voucherTypeId;
    public String activeStartDt;     //体验券活动开始日期
    public String activeEndDt;     //体验券活动结束日期
    public int status;
    public int positionId;
    public String title;
    public Good goods;
    private final static long serialVersionUID = 6384799606472812016L;




}
