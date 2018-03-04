package com.zxzx74147.devlib.data;

import java.io.Serializable;

/**
 * Created by zhengxin on 16/8/21.
 */

public class IntentData<T extends Serializable> implements Serializable {
    public IntentData(){

    }

    public IntentData(T data){
        this.data = data;
    }
    public int type = 0;
    public T data = null;
}
