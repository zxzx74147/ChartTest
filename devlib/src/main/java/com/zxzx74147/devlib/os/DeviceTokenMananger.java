package com.zxzx74147.devlib.os;


import com.zxzx74147.devlib.utils.SharedPreferenceHelper;

public class DeviceTokenMananger {

    private static int mTokenType = 0;
    private static String mTokenKey = null;
    private static String mTokenMi = null;
    private static String mTokenGetui = null;
    private static String mToken = null;
    public final static int DEVICE_TOKEN_TYPE_MI = 2;
    public static final int DEVICE_TOKEN_TYPE_GETUI = 3;
    public final static String DEVICE_TOKEN_KEY_TYPE = "key_token_type";
    public final static String DEVICE_TOKEN_KEY_MI = "key_token_mi";
    private final static String DEVICE_TOKEN_KEY_GETUI = "key_token_getui";
    public final static String DEVICE_TOKEN_KEY = "key_token";

    // private constuct
    private DeviceTokenMananger() {
    }


    public static String getDeviceToken() {

        if (mToken == null) {
            mToken = SharedPreferenceHelper.getString(DEVICE_TOKEN_KEY);
        }
        return mToken;
    }

    // 根据类型设置token
    public static void setDeviceToken(String token) {

        mToken = token;
        mTokenKey = DEVICE_TOKEN_KEY;
        SharedPreferenceHelper.saveString(mTokenKey, token);
    }
}
