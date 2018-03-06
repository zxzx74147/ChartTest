
package com.zxzx74147.live.data;

import com.zxzx74147.devlib.data.BaseListData;

import java.io.Serializable;
import java.util.List;

public class ReplyList extends BaseListData<Reply> implements Serializable
{

//    public int num;
//    public int hasMore;
//    public int nextReplyId;
//    public int nextPage;
    public List<Reply> reply = null;
    private final static long serialVersionUID = 6635063709304316160L;

    @Override
    public List<Reply> getListItems() {
        return reply;
    }
}
