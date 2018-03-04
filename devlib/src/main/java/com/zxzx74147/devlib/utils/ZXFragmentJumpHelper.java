package com.zxzx74147.devlib.utils;

import android.content.Context;
import android.os.Bundle;

import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.data.IntentData;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.security.auth.callback.Callback;

/**
 * Created by zhengxin on 15/8/31.
 */
public class ZXFragmentJumpHelper {
    private static final String TAG = ZXFragmentJumpHelper.class.getSimpleName();
    public static final String INTENT_DATA = "data";

    private static CommonCallback CALL_BACK = null;

    private ZXFragmentJumpHelper() {

    }

    public static void startFragment(Context context, BaseDialogFragment fragment, CommonCallback callback) {
        CALL_BACK = callback;
        fragment.show((ViewUtil.getFragmentActivity(context)).getSupportFragmentManager(), fragment.getTag());
    }

    public static void startFragment(Context context, Class<? extends BaseDialogFragment> fragmentClass, IntentData data) {

        BaseDialogFragment fragment = null;
        try {
            fragment = fragmentClass.newInstance();
            Bundle args = new Bundle();
            args.putSerializable(ZXActivityJumpHelper.INTENT_DATA, data);
            fragment.setArguments(args);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        fragment.show((ViewUtil.getFragmentActivity(context)).getSupportFragmentManager(), fragment.getTag());
    }

    public static CommonCallback getCallBack() {
        CommonCallback callback = CALL_BACK;
        CALL_BACK = null;
        return callback;
    }
}
