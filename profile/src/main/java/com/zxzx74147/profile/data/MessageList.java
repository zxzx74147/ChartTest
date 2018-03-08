package com.zxzx74147.profile.data;

import com.zxzx74147.devlib.data.BaseListData;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhengxin on 2018/2/28.
 */

public class MessageList extends BaseListData<Message> implements Serializable {

    public List<Message> msg;

    @Override
    public List<Message> getListItems() {
        return msg;
    }
}
