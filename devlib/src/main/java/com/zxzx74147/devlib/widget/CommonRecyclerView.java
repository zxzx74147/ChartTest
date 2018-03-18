package com.zxzx74147.devlib.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.AttributeSet;
/**
 * Created by zhengxin on 2018/2/12.
 */

public class CommonRecyclerView extends RecyclerView{


    public CommonRecyclerView(Context context) {
        super(context);
        init();
    }

    public CommonRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        ((SimpleItemAnimator)getItemAnimator()).setSupportsChangeAnimations(false);
        getItemAnimator().setChangeDuration(0);

    }

    public void scrollToButtom(){

        postDelayed(() -> {
            if(getAdapter()==null){
                return;
            }
            int count = getAdapter().getItemCount();
            smoothScrollToPosition(count);
        },30);

    }
}
