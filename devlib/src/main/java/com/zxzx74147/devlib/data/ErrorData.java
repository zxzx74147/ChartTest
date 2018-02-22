package com.zxzx74147.devlib.data;

import java.io.Serializable;

/**
 * Created by zhengxin on 2018/2/12.
 */

public class ErrorData implements Serializable {
    public int errno = 200;
    public String errmsg;
    public String usermsg;

    public static ErrorData createError(Throwable e){
        ErrorData error = new ErrorData();
        error.errno = 100;
        error.errmsg = e.getMessage();
        return error;
    }
}
