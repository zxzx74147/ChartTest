package com.zxzx74147.devlib.data;

import android.content.Context;

/**
 * Created by zhengxin on 2018/2/12.
 */

public class MessageEvent<T> {
    public int id;
    public Context context;
    public int requestCode;
    public T data;
    public int type;

    public MessageEvent(int id){
        this.id = id;
    }
    public MessageEvent(int id,Context context,T data){
        this.id = id;
        this.context = context;
        this.data = data;
    }

    public MessageEvent(int id,Context context){
        this.id = id;
        this.context = context;
    }
}
