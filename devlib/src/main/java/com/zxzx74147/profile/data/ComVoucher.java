
package com.zxzx74147.profile.data;

import com.zxzx74147.devlib.DevLib;
import com.zxzx74147.devlib.R;
import com.zxzx74147.devlib.utils.TimeUtil;
import com.zxzx74147.stock.data.Good;

import java.io.Serializable;

public class ComVoucher implements Serializable {

    public String voucherId       ;     //体验券类型ID
    public String title           ;     //体验券主题(新手专享券)
    public String name           ;     //体验券主题(新手专享券)
    public String desc           ;     //体验券主题(新手专享券)
    public String getTime         ;     //领取时间
    public String validStartDt    ;     //有效期开始日
    public String remind           ;     //体验券主题(新手专享券)
    public String validEndDt      ;     //有效期结束日
   public float depositFee      ;      //体验券面额

    public String getEndTime() {
        if (validEndDt != null) {
            return DevLib.getApp().getString(R.string.active_time) + TimeUtil.reformatDefaultTime(validEndDt, DevLib.getApp().getString(R.string.format_YYYYMMDD_HHMM));

        }

        return "";
    }


}
