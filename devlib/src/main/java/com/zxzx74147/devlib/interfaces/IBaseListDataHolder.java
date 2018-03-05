package com.zxzx74147.devlib.interfaces;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProvider;

import com.zxzx74147.devlib.data.BaseListData;

import javax.annotation.Nonnull;

/**
 * Created by zhengxin on 2018/2/25.
 */

public interface IBaseListDataHolder<T> {
    BaseListData<T> getListData();
}
