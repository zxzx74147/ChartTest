
package com.zxzx74147.live.data;

import com.zxzx74147.devlib.data.BaseListData;
import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.interfaces.IBaseListDataHolder;

import java.io.Serializable;

public class ReplyListData extends UniApiData implements Serializable,IBaseListDataHolder<Reply>
{

    public ReplyList replyList;
    private final static long serialVersionUID = 5548113798378299552L;

    @Override
    public BaseListData<Reply> getListData() {
        return replyList;
    }
}
