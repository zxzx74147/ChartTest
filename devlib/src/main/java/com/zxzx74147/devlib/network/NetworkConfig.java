package com.zxzx74147.devlib.network;

import android.content.res.Resources;

import com.zxzx74147.devlib.DevLib;
import com.zxzx74147.devlib.R;

/**
 * Created by zhengxin on 2018/2/8.
 */

public class NetworkConfig {
    public static String HOST_STOCK;
    public static String HOST;
    public static String UA;

    static{
        Resources resource = DevLib.getApp().getResources();;
        HOST_STOCK = resource.getString(R.string.network_host_stock);
        HOST = resource.getString(R.string.network_host);
        UA = resource.getString(R.string.network_UA);
    }
}
