package com.zxzx74147.devlib.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Created by zhengxin on 2018/2/12.
 */

public class JsonHelper {
    private static  Gson mGson = null;

    static {
        mGson = new GsonBuilder()
                .create();
    }

    public static String toJson(Object obj){
        return mGson.toJson(obj);
    }

    public static <T> T convertJson(String json, Class<? extends T> mClass) {
        try {
            return mGson.fromJson(json, mClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T convertJson(String json, Type mType) {
        try {
            return mGson.fromJson(json, mType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
