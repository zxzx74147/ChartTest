package com.zxzx74147.devlib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.data.IntentData;

/**
 * Created by zhengxin on 15/8/31.
 */
public class ZXActivityJumpHelper {
    public static final String INTENT_DATA = "data";

    private ZXActivityJumpHelper() {

    }

    public static void startActivity(Context context, Class<? extends BaseActivity> T) {
        startActivity(context, T, null);
    }

    public static void startActivity(Context context, Class<? extends BaseActivity> T, IntentData data) {
        Intent intent = new Intent(context, T);
        intent.putExtra(INTENT_DATA, data);
        context.startActivity(intent);
    }

    public static void startActivity(Fragment fragment, Class<? extends BaseActivity> T) {
        startActivity(fragment, T, null);
    }

    public static void startActivity(Fragment fragment, Class<? extends BaseActivity> T, IntentData data) {
        Intent intent = new Intent(fragment.getActivity(), T);
        intent.putExtra(INTENT_DATA, data);
        fragment.startActivity(intent);
    }

    public static void startActivityForResult(Activity context, int requestCode, Class<? extends BaseActivity> T) {
        startActivityForResult(context, requestCode, T, null);
    }

    public static void startActivityForResult(Activity context, int requestCode, Class<? extends BaseActivity> T, IntentData data) {
        Intent intent = new Intent(context, T);
        intent.putExtra(INTENT_DATA, data);
        context.startActivityForResult(intent, requestCode);
    }

    public static IntentData getIntentData(Intent intent) {
        return (IntentData) intent.getSerializableExtra(INTENT_DATA);
    }

}
