package com.zxzx74147.devlib.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.zxzx74147.devlib.R;

/**
 * Created by zhengxin on 2018/3/14.
 */

public class CommonLoading extends Dialog {
    public CommonLoading(@NonNull Context context) {
        super(context,R.style.Loading);
        setContentView(R.layout.common_progressbar);
        // 设置Dialog参数
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);

    }
}
