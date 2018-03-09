package com.zxzx74147.devlib.utils;

import com.zxzx74147.devlib.DevLib;
import com.zxzx74147.devlib.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by zhengxin on 2018/2/20.
 */

public class TimeUtil {
    private static SimpleDateFormat HHMM = new SimpleDateFormat("HH:mm");

    private static HashMap<String, SimpleDateFormat> mTimeHash = new HashMap<>();

    public static String getHHMMTime(int time) {
        Date nowTime = new Date(time * 1000);

        return HHMM.format(nowTime);
    }

    public static SimpleDateFormat getSampleFormat(String format) {
        if (!mTimeHash.containsKey(format)) {
            mTimeHash.put(format, new SimpleDateFormat(format));
        }
        return mTimeHash.get(format);
    }

    public static String reformatTime(String input, String formatInput, String formatOutput) {
        SimpleDateFormat inputDateFormat = getSampleFormat(formatInput);
        SimpleDateFormat outDateFormat = getSampleFormat(formatOutput);
        try {
            Date date = inputDateFormat.parse(input);
            return outDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String reformatDefaultTime(String input, String formatOutput) {
        SimpleDateFormat inputDateFormat = getSampleFormat(DevLib.getApp().getResources().getString(R.string.format_input));
        SimpleDateFormat outDateFormat = getSampleFormat(formatOutput);
        try {
            Date date = inputDateFormat.parse(input);
            return outDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";

    }

    public static String parserIntTime(long time, String formatOutput) {
        SimpleDateFormat outDateFormat = getSampleFormat(formatOutput);
        time*=1000;
        Date date = new Date(time);
        long now = new Date().getTime();
        if(now-time<60*1000){
            return DevLib.getApp().getString(R.string.just_now);
        }
        return outDateFormat.format(date);

    }

}
