package com.zxzx74147.devlib.kvstore;

import android.content.Context;
import android.view.MotionEvent;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;
import com.umeng.analytics.MobclickAgent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.internal.operators.flowable.FlowableJust;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhengxin on 2018/2/21.
 */

public class KVStore {
    private static DB mSnappydb = null;

    public static void init(Context context) {
        try {
            mSnappydb = DBFactory.open(context);
        } catch (SnappydbException e) {
            MobclickAgent.onEvent(context,"snappy_db_exception",e.getMessage());
            e.printStackTrace();
        }
    }

    public static void put(String key, String value) {
        FlowableJust.just(value).subscribeOn(Schedulers.io())
                .map((Function<String, String>) s -> {
                    try {
                        mSnappydb.put(key, s);
                    } catch (SnappydbException e) {
                        e.printStackTrace();
                    }
                    return "";
                }).subscribeOn(AndroidSchedulers.mainThread()).subscribe(a->{});

    }

    public static void put(String key, Object value) {
        FlowableJust.just(value).subscribeOn(Schedulers.io())
                .map(s -> {
                    try {
                        mSnappydb.put(key, s);
                    } catch (SnappydbException e) {
                        e.printStackTrace();
                    }
                    return "";
                }).subscribeOn(AndroidSchedulers.mainThread()).subscribe(a->{});
    }

    public static <T> T get(String key, Class<T> className) {
        try {
            return mSnappydb.getObject(key, className);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getString(String key) {
        try {
            return mSnappydb.get(key);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        return null;
    }
}
