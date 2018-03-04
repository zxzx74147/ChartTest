package com.zxzx74147.devlib.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.zxzx74147.devlib.R;
import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.utils.ZXFragmentJumpHelper;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by zhengxin on 2018/2/12.
 */

public class BaseFragment extends SupportFragment {
    protected CommonCallback mCallback = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallback = ZXFragmentJumpHelper.getCallBack();
    }

}
