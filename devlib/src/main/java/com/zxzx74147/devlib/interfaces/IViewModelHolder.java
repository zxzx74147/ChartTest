package com.zxzx74147.devlib.interfaces;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProvider;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import javax.annotation.Nonnull;

/**
 * Created by zhengxin on 2018/2/25.
 */

public interface IViewModelHolder {
    void setProvider(@Nonnull ViewModelProvider provider);
    void setLifeCircle(@Nonnull LifecycleOwner owner);
}
