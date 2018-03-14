package com.zxzx74147.devlib.widget;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.lang.reflect.Field;

/**
 * Created by zhengxin on 2018/2/12.
 */

public class CommonTabLayout extends TabLayout{


    public CommonTabLayout(Context context) {
        super(context);
        init();
    }



    public CommonTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        addOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                try {
                    Field privateStringField = Tab.class.
                            getDeclaredField("mView");
                    privateStringField.setAccessible(true);//允许访问私有字段
                    View v = (View) privateStringField.get(tab);
                    v.setScaleX(1.33f);
                    v.setScaleY(1.33f);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onTabUnselected(Tab tab) {
                try {
                    Field privateStringField = Tab.class.
                            getDeclaredField("mView");
                    privateStringField.setAccessible(true);//允许访问私有字段
                    View v = (View) privateStringField.get(tab);
                    v.setScaleX(1f);
                    v.setScaleY(1f);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTabReselected(Tab tab) {

            }
        });

    }


}
