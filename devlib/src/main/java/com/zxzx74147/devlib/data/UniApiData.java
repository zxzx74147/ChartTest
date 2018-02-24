package com.zxzx74147.devlib.data;

import java.io.Serializable;

import okhttp3.internal.http2.ErrorCode;

/**
 * Created by zhengxin on 2018/2/8.
 */

public class UniApiData implements Serializable {
    public ErrorData error = new ErrorData();

    public boolean hasError(){
        return error.errno!=200;
    }

//    public static<T> UniApiData<T> create(T data){
//        UniApiData<T> uniData = new UniApiData<>();
//        uniData.data = data;
//        return uniData;
//    }

    public static<T extends UniApiData> T createError(ErrorData error,Class<T> mClass){
        try {
            T result = mClass.newInstance();
            result.error = error;
            return result;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static<T extends UniApiData> T createError(Throwable t,Class<T> mClass){
        try {
            T result = mClass.newInstance();
            result.error = ErrorData.createError(t);
            return result;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
