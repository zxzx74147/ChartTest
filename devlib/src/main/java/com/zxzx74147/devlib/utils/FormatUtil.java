package com.zxzx74147.devlib.utils;

import com.zxzx74147.devlib.DevLib;
import com.zxzx74147.devlib.R;
import com.zxzx74147.devlib.base.BaseFragment;
import com.zxzx74147.stock.data.Good;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhengxin on 2018/2/20.
 */

public class FormatUtil {
    private static Pattern P_PURE_NUMBER = Pattern.compile("[^0-9]");
    private static Pattern P_PURE_NUMBER_FLOAT = Pattern.compile("[^0-9.]");

    public  static List<String> POINT_LIST = null;
    public  static List<String> ERROR_LIST = null;
    private static Calendar CALENDER_NOW = Calendar.getInstance();
    private static SimpleDateFormat TIME_HHMM = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat TIME_MMDD_HHMM = new SimpleDateFormat("MM/dd HH:mm");

    static {
        POINT_LIST = new ArrayList<>(50);
        POINT_LIST.add("无");
        for(int i=7;i<100;i++){
            POINT_LIST.add(i+"点");
        }

        POINT_LIST = new ArrayList<>(50);
        POINT_LIST.add("无");
        for(int i=7;i<100;i++){
            POINT_LIST.add(i+"点");
        }
    }

    public static List<String> getPointCal(Good good,int amount){
        List<String> pointList = new LinkedList<>();
        if(good==null){
            return pointList;
        }
        pointList.add("无");
        String format = DevLib.getApp().getString(R.string.format_02);
        for(int i=7;i<100;i++){
            String money = "("+String.format(format,good.profitPerUnit*i*amount)+"元)";
            pointList.add(i+"点"+money);
        }
        return pointList;

    }

    public static int getPureNum(String input){
        try {
            Matcher m = P_PURE_NUMBER.matcher(input);
            String pure = m.replaceAll("").trim();
            return Integer.valueOf(pure);
        }catch (Exception e){
            return 0;
        }
    }

    public static String formatWithDay(Date input){
        CALENDER_NOW.setTimeInMillis(System.currentTimeMillis());
        Calendar tempCalender = Calendar.getInstance();

            tempCalender.setTime(input);
            if (tempCalender.get(Calendar.DAY_OF_YEAR) == CALENDER_NOW.get(Calendar.DAY_OF_YEAR)) {
                return TIME_HHMM.format(input);
            }
            return TIME_MMDD_HHMM.format(input);

    }


    public static float getPureFloatNum(String input){
        try {
            Matcher m = P_PURE_NUMBER_FLOAT.matcher(input);
            String pure = m.replaceAll("").trim();
            return Float.valueOf(pure);
        }catch (Exception e){
            return 0f;
        }
    }

    public static double getPureDoubleNum(String input){
        try {
            Matcher m = P_PURE_NUMBER_FLOAT.matcher(input);
            String pure = m.replaceAll("").trim();
            return Double.valueOf(pure);
        }catch (Exception e){
            return 0f;
        }
    }

    public static String getFormat(float num,String format){
        DecimalFormat   df   =   new DecimalFormat( "#,##0.0");
        String   value   =   df.format(num);
        return value;
    }

    public static String formatWithoutZero(double num,String format){
        String ouput = String.format(format,num);
        while (ouput.length()>0){
            if(ouput.endsWith("0")&&ouput.contains(".")){
                ouput = ouput.substring(0,ouput.length()-1);
            }else
            if(ouput.endsWith(".")){
                ouput = ouput.substring(0,ouput.length()-1);
            }else{
                break;
            }
        }
        return ouput;
    }
}
