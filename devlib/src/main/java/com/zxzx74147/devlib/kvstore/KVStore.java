package com.zxzx74147.devlib.kvstore;

import android.content.Context;

import com.zxzx74147.devlib.json.JsonHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.internal.operators.flowable.FlowableJust;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhengxin on 2018/2/21.
 */

public class KVStore {
    private static final String DB_NAME = "KV_STORE";
//    private static DB mSnappydb = null;

    public static void init(Context context) {
        com.lusfold.androidkeyvaluestore.KVStore.init(context, DB_NAME);
//        try {
//            mSnappydb = DBFactory.open(context);
//        } catch (SnappydbException e) {
//            MobclickAgent.onEvent(context,"snappy_db_exception",e.getMessage());
//            e.printStackTrace();
//        }
    }

    public static void put(String key, String value) {
        FlowableJust.just(value).subscribeOn(Schedulers.io())
                .map((Function<String, String>) s -> {
                    com.lusfold.androidkeyvaluestore.KVStore.getInstance().insertOrUpdate(key, value);
//                    try {
//                        mSnappydb.put(key, s);
//                    } catch (SnappydbException e) {
//                        e.printStackTrace();
//                    }
                    return "";
                }).subscribeOn(AndroidSchedulers.mainThread()).subscribe(a -> {
        });

    }

    public static void put(String key, Object value) {
        FlowableJust.just(value).subscribeOn(Schedulers.io())
                .map(s -> {
                    com.lusfold.androidkeyvaluestore.KVStore.getInstance().insertOrUpdate(key, JsonHelper.toJson(value));
//                    try {
//                        mSnappydb.put(key, s);
//                    } catch (SnappydbException e) {
//                        e.printStackTrace();
//                    }
                    return "";
                }).subscribeOn(AndroidSchedulers.mainThread()).subscribe(a -> {
        });
    }

    public static <T> T get(String key, Class<T> className) {
        try {
            if (!com.lusfold.androidkeyvaluestore.KVStore.getInstance().keyExists(key)) {
                return null;
            }
            String v = com.lusfold.androidkeyvaluestore.KVStore.getInstance().get(key);
            return JsonHelper.convertJson(v, className);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getString(String key) {
        try {
            if (!com.lusfold.androidkeyvaluestore.KVStore.getInstance().keyExists(key)) {
                return null;
            }
            String v = com.lusfold.androidkeyvaluestore.KVStore.getInstance().get(key);
            return v;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void clearAll() {
        com.lusfold.androidkeyvaluestore.KVStore.getInstance().clearTable();
    }
}
