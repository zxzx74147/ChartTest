package com.zxzx74147.profile.data;

import com.zxzx74147.devlib.kvstore.KVStore;
import com.zxzx74147.devlib.modules.account.AccountManager;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;

/**
 * Created by zhengxin on 2018/2/28.
 */

public class UnReadManager implements Serializable {
    private static UnReadManager mInstance =null;
    private static final String READ_TABLE = "READ_TABLE12";
    private HashSet<Integer> mReadTable = new HashSet<>();
    private int mLastMsgId = 0;
    private int mLastUnReadNum = 0;
    static{
        mInstance = KVStore.get(READ_TABLE,UnReadManager.class);
        if(mInstance==null){
            mInstance = new UnReadManager();
        }
    }

    private UnReadManager() {
    }


    public static UnReadManager sharedInstance() {

        return mInstance;
    }


    public  void clearUserInfo(){

        mLastMsgId = 0;
        mLastUnReadNum = 0;
        mReadTable.clear();
        KVStore.put(READ_TABLE,this);

    }

    public boolean isRead(int id) {
        if (mReadTable.contains(id)) {
            return true;
        }
        return false;
    }

    public void markAllRead(List<Message> msgList) {
        if (msgList == null||msgList.size()==0) {
            return;
        }
        mLastMsgId = msgList.get(0).msgId;
        for (Message msg : msgList) {
            mReadTable.add(msg.msgId);
        }
        mLastUnReadNum=0;

        KVStore.put(READ_TABLE, this);
    }

    public void refresh(List<Message> msgList) {
        if (msgList == null||msgList.size()==0) {
            return;
        }
        int count = 0;
        mLastMsgId = msgList.get(0).msgId;

        for (Message msg : msgList) {

            if (!isRead(msg.msgId)) {
                count++;
            }
        }
        mLastUnReadNum = count;
        KVStore.put(READ_TABLE, this);
    }

    public int getUnReadNum() {
        if(AccountManager.sharedInstance().getUserUni()==null){
            return mLastUnReadNum;
        }
        int lastId = AccountManager.sharedInstance().getUserUni().lastMsgCenterId-mLastMsgId;
        lastId = Math.max(0,lastId);
        return mLastUnReadNum + lastId;
    }


    public void markRead(int msgId) {
        if(mReadTable.contains(msgId)){
            return;
        }
        mReadTable.add(msgId);
        mLastUnReadNum-=1;
        mLastUnReadNum = Math.max(0,mLastUnReadNum);
        KVStore.put(READ_TABLE, this);
    }
}
