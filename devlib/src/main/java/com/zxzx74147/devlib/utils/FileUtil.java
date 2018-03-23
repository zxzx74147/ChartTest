package com.zxzx74147.devlib.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import com.zxzx74147.devlib.DevLib;

/**
 * Created by zhengxin on 2018/3/1.
 */

public class FileUtil {

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        int writePermission  = ActivityCompat.checkSelfPermission(DevLib.getApp(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (writePermission != PackageManager.PERMISSION_GRANTED){
            return false;
        }
        return true;
    }

    public static boolean isCameraAvaliable() {
        int writePermission  = ActivityCompat.checkSelfPermission(DevLib.getApp(), Manifest.permission.CAMERA);
        if (writePermission != PackageManager.PERMISSION_GRANTED){
            return false;
        }
        return true;
    }


    private static int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.CAMERA",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };


    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            int permission2 = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.CAMERA");
            if (permission != PackageManager.PERMISSION_GRANTED||permission2!= PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
