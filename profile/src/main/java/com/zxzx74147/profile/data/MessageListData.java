package com.zxzx74147.profile.data;

import com.zxzx74147.devlib.data.BaseListData;
import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.interfaces.IBaseListDataHolder;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhengxin on 2018/2/28.
 */

public class MessageListData extends UniApiData implements Serializable,IBaseListDataHolder<Message> {
    public MessageList msgList;

    @Override
    public BaseListData<Message> getListData() {
        return msgList;
    }
}
