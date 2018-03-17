
package com.zxzx74147.live.data;

import android.databinding.BindingAdapter;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Live implements Serializable {
    private static DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm");
    private static DateFormat DATE_FORMAT_FULL = new SimpleDateFormat("MM-dd HH:mm");
    public long liveId;
    public int status;
    public String timeTitle;
    public RtmpList rtmpList;
    public TeacherList teacherList;
    public long startTime;
    public long endTime;
    private final static long serialVersionUID = -813403258936861414L;


    @BindingAdapter({"timeLive"})
    public static void loadImage(TextView view, long time) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        Date date = new Date(time);
        Date now = calendar.getTime();
        long l = now.getTime() - date.getTime();
        int day = (int) (l / (24 * 60 * 60 * 1000));
        int hour = (int) (l / (60 * 60 * 1000) - day * 24);
        int min = (int) ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        int s = (int) (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        String HM = DATE_FORMAT.format(date);
        String temp = "";
        switch (day) {
            case 0:
                temp += "今天"+HM;
                break;
            case 1:
                temp += "明天"+HM;
                break;
            case 2:
                temp += "后天"+HM;
                break;
            default:
                temp = DATE_FORMAT_FULL.format(date);
        }
        view.setText(temp);
    }

}
