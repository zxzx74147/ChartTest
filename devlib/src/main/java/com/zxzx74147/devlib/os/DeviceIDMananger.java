package com.zxzx74147.devlib.os;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.zxzx74147.devlib.DevLib;
import com.zxzx74147.devlib.utils.SharedPreferenceHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * DeviceID用于区分设备，要保证绝对唯一，生成之后内外均存储。生成规则待定 ; TODO
 * 对于内外存均需要存储数据，统一提供操作类，并向上层隐藏同步过程 ;
 *
 * @author zhengxin
 */
public class DeviceIDMananger {
    private static DeviceIDMananger mMananger;
    private static String FILE_NAME = "device_id.bak";

    private static final String KEY_DEVICE_ID = "key_device_id";

    private String mDeviceID = null;

    private DeviceIDMananger() {
        // 初始化mDeviceID
        mDeviceID = getDeviceIDFromInnerStorage();
        if (TextUtils.isEmpty(mDeviceID)) {
            mDeviceID = generateDeviceID();
            saveDeviceIDTOInnerStorage(mDeviceID);
        }
    }

    public static DeviceIDMananger sharedInstance() {
        if (mMananger == null) {
            mMananger = new DeviceIDMananger();
        }
        return mMananger;
    }

    public String getDeviceID() {
        if (TextUtils.isEmpty(mDeviceID)) {
            mDeviceID = generateDeviceID();
            saveDeviceIDTOInnerStorage(mDeviceID);
        }
        return mDeviceID;
    }


    // 生成DeviceID
    private String generateDeviceID() {
        Context context = DevLib.getApp().getApplicationContext();
        String androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMEIStr = "";
        if (ActivityCompat.checkSelfPermission(DevLib.getApp(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

        }else{
            IMEIStr = tm.getDeviceId();
        }
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String macStr = info.getMacAddress();
        String DevID = IMEIStr + macStr + androidId;
        return DevID;
    }



    // 从内部存储读取DeviceID
    private String getDeviceIDFromInnerStorage() {
        return SharedPreferenceHelper.getString(KEY_DEVICE_ID);


    }

    // 存储DeviceID到内
    private void saveDeviceIDTOInnerStorage(String deviceId) {
        SharedPreferenceHelper.saveString(KEY_DEVICE_ID,deviceId);
//        BdKVCache<String> cache = DBKVCacheManager.getInstance()
//                .getSecureCache();
//        cache.asyncSet(KEY_DEVICE_ID, deviceId, BdKVCache.MILLS_10Years);
//        File file = FileHelper.FileObject(FILE_NAME);
//        FileWriter writer = null;
//        if (file == null) {
//            return;
//        }
//        try {
//            writer = new FileWriter(file);
//            writer.write(deviceId);
//            writer.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (writer != null) {
//                    writer.close();
//                }
//            } catch (Exception e) {
//
//            }
//        }
    }

}
