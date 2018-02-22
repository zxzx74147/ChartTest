package com.zxzx74147.devlib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by hujiaojiao on 16/4/20.
 */
public class SharedPreferenceHelper {
    public static final String SHARED_PREFERENCE = "baobao_shared_preference.xml";
    private static Context mContext;


    public static void init(Context context){
        mContext = context;
        Log.e("SharedPref__","init");
    }


    public static void saveInt(String key, int value){
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(String key, int defaultValue) {
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

    public static void saveString(String key, String value) {
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(String key) {
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public static String getString(String key,String defaultValue) {
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    public static void saveBoolean(String key, boolean value){
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    public static void removeKey(String key){
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().remove(key).apply();
    }
}
