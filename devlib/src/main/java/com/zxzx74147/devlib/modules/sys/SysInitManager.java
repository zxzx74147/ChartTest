package com.zxzx74147.devlib.modules.sys;

import com.zxzx74147.devlib.data.SysInitData;

/**
 * Created by zhengxin on 2018/3/4.
 */

public class SysInitManager {
    private static SysInitManager mSysInitManager = new SysInitManager();
    private SysInitData mSysInitData = new SysInitData();

    private SysInitManager() {

    }

    public static SysInitManager sharedInstance() {
        return mSysInitManager;
    }

    public void setSysInitData(SysInitData data) {
        mSysInitData = data;
    }

    public SysInitData getSysInitData() {
        return mSysInitData;
    }

}
