package com.zxzx74147.devlib.widget;

import android.util.SparseIntArray;

import com.chad.library.adapter.base.util.MultiTypeDelegate;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

/**
 * Created by zhengxin on 2018/2/12.
 */

public class CommonMultiTypeDelegate extends MultiTypeDelegate {

    private static HashMap<Class, Integer> DEFAULTVIEWTABLE = new HashMap<>();
    private static SparseIntArray LAYOUTS = new SparseIntArray();

    private Map<Class, Integer> mDefaultViewTable = new HashMap<>();

    private static CommonMultiTypeDelegate mInstance = new CommonMultiTypeDelegate();

    public static void registDefaultViewType(@Nonnull Class mClass, int value) {
        DEFAULTVIEWTABLE.put(mClass, value);
        LAYOUTS.put(value, value);
    }

    public void registViewType(@Nonnull Class mClass, int value) {
        mDefaultViewTable.put(mClass, value);
        registerItemType(value,value);
    }


    public CommonMultiTypeDelegate() {
        super(LAYOUTS.clone());
    }

    @Override
    protected int getItemType(Object o) {
        Integer type = mDefaultViewTable.get(o.getClass());
        if (type != null) {
            return type;
        }
        type = DEFAULTVIEWTABLE.get(o.getClass());
        if (type != null) {
            return type;
        }
        return 0;
    }
}
