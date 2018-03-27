package com.zxzx74147.devlib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.zxzx74147.devlib.R;
import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.utils.ViewUtil;

import java.lang.reflect.Field;

/**
 * Created by zhengxin on 2018/2/12.
 */

public class CommonTabLayout extends TabLayout{


    private float mScale = 1;
    private boolean mBold = false;
    public CommonTabLayout(Context context) {
        super(context);
        init(null);
    }



    public CommonTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public View getViewAt(int i){
        Tab tab = getTabAt(i);

        Field privateStringField = null;
        try {
            privateStringField = Tab.class.
                    getDeclaredField("mView");
            privateStringField.setAccessible(true);//允许访问私有字段
            View v = (View) privateStringField.get(tab);
            return v;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;


    }

    private void init(AttributeSet attrs) {
        if(attrs==null){
            return;
        }
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CommonTabLayout);

        mScale = a.getFloat(R.styleable.CommonTabLayout_scaleSize, 1);
        mBold = a.getBoolean(R.styleable.CommonTabLayout_selectBold, false);

        a.recycle();

        addOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                try {
                    Field privateStringField = Tab.class.
                            getDeclaredField("mView");
                    privateStringField.setAccessible(true);//允许访问私有字段
                    View v = (View) privateStringField.get(tab);
                    v.setScaleX(mScale);
                    v.setScaleY(mScale);
                    if(mBold) {
                        ViewUtil.dfsView(v, new CommonCallback<View>() {
                            @Override
                            public void callback(View item) {
                                if (item instanceof TextView) {
                                    ((TextView)item).setTypeface(null, Typeface.BOLD);
                                }
                            }
                        });
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (Exception e) {
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
                    ViewUtil.dfsView(v, new CommonCallback<View>() {
                        @Override
                        public void callback(View item) {
                            if (item instanceof TextView) {
                                ((TextView)item).setTypeface(null, Typeface.NORMAL);
                            }
                        }
                    });
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTabReselected(Tab tab) {

            }
        });

    }


}
