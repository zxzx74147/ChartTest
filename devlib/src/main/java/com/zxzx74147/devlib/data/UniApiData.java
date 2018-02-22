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

    public static UniApiData createError(ErrorData error){
        UniApiData uniData = new UniApiData();
        uniData.error = error;
        return uniData;
    }

}
