
package com.zxzx74147.live.data;

import com.zxzx74147.devlib.data.UniApiData;

import java.io.Serializable;
import java.util.List;

public class LiveMsgListData extends UniApiData implements Serializable
{

    public int lastMId;
    public LiveDynamic liveDynamic;
    public MsgList msgList;

}
