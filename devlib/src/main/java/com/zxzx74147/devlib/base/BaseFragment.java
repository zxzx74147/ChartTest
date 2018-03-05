package com.zxzx74147.devlib.base;


import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.utils.ZXFragmentJumpHelper;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by zhengxin on 2018/2/12.
 */

public class BaseFragment extends SupportFragment {
    protected CommonCallback mCallback = null;

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallback = ZXFragmentJumpHelper.getCallBack();
    }

}
