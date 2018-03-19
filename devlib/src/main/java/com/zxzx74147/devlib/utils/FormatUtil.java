package com.zxzx74147.devlib.utils;

import com.zxzx74147.devlib.base.BaseFragment;

import java.util.ArrayList;
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
    static {
        POINT_LIST = new ArrayList<>(50);
        POINT_LIST.add("无");
        for(int i=1;i<100;i++){
            POINT_LIST.add(i+"点");
        }

        POINT_LIST = new ArrayList<>(50);
        POINT_LIST.add("无");
        for(int i=1;i<100;i++){
            POINT_LIST.add(i+"点");
        }
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

    public static float getPureFloatNum(String input){
        try {
            Matcher m = P_PURE_NUMBER_FLOAT.matcher(input);
            String pure = m.replaceAll("").trim();
            return Float.valueOf(pure);
        }catch (Exception e){
            return 0f;
        }
    }
}
