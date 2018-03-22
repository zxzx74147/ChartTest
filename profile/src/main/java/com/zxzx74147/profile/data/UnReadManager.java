package com.zxzx74147.profile.data;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.zxzx74147.devlib.kvstore.KVStore;
import com.zxzx74147.devlib.utils.ColorUtil;
import com.zxzx74147.profile.R;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;

/**
 * Created by zhengxin on 2018/2/28.
 */

public class UnReadManager implements Serializable{
    private static final String READ_TABLE = "READ_TABLE1";
    private HashSet<Integer> mReadTable = new HashSet<>();
    private int mLastMsgId = 0;
    private int mLastUnReadNum = 0;

    private UnReadManager(){
        KVStore.get(READ_TABLE,UnReadManager.class);
    }


    public void setRead(int id){
        mReadTable.add(id);
        KVStore.put(READ_TABLE,this);
    }

    public boolean isRead(int id){
        if(mReadTable.contains(id)){
            return false;
        }
        return true;
    }

    public void markAllRead(List<Message> msgList){
        if(msgList==null){
            return;
        }
        for(Message msg :msgList){
            mReadTable.add(msg.msgId);
        }
        KVStore.put(READ_TABLE,this);
    }

    public void refresh(MessageList msgList){
        if(msgList==null){
            return;
        }
        int count = 0;
        for(Message msg :msgList.msg){
            mLastMsgId = msg.msgId;
            if(!isRead(msg.msgId)){
                count++;
            }
        }
        mLastUnReadNum = count;
        KVStore.put(READ_TABLE,this);
    }




}
