package com.zxzx74147.devlib.kvstore;

import android.content.Context;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

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
                    return null;
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
                    return null;
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
}
