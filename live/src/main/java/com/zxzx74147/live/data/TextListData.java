
package com.zxzx74147.live.data;

import com.zxzx74147.devlib.data.BaseListData;
import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.interfaces.IBaseListDataHolder;

import java.io.Serializable;

public class TextListData extends UniApiData implements Serializable,IBaseListDataHolder<Text>
{

    public TextList textList;
    private final static long serialVersionUID = 9036012639496222845L;

    @Override
    public BaseListData<Text> getListData() {
        return textList;
    }
}
