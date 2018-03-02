package com.zxzx74147.devlib.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhengxin on 2018/2/20.
 */

public class TimeUtil {
    private static SimpleDateFormat HHMM = new SimpleDateFormat("HH:mm");

    public static String getHHMMTime(int time){
        Date nowTime=new Date(time*1000);

        return HHMM.format(nowTime);
    }
}
