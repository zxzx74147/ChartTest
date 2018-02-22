package com.zxzx74147.devlib.widget;

import android.util.SparseIntArray;

import com.chad.library.adapter.base.util.MultiTypeDelegate;

import java.util.HashMap;

import javax.annotation.Nonnull;

/**
 * Created by zhengxin on 2018/2/12.
 */

public class CommonMultiTypeDelegate extends MultiTypeDelegate {

    private static HashMap<Class, Integer> mDefaultViewTable = new HashMap<>();
    private static SparseIntArray mLayouts = new SparseIntArray();

    private static CommonMultiTypeDelegate mInstance = new CommonMultiTypeDelegate();

    public static void registDefaultViewType(@Nonnull Class mClass, int value) {
        mDefaultViewTable.put(mClass, value);
        mLayouts.put(value, value);
    }


    public CommonMultiTypeDelegate() {
        super(mLayouts);
    }

    @Override
    protected int getItemType(Object o) {
        Integer type = mDefaultViewTable.get(o.getClass());
        if (type != null) {
            return type;
        }
        return 0;
    }
}
