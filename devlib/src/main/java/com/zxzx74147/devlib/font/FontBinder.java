package com.zxzx74147.devlib.font;

import android.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.widget.TextView;

import com.zxzx74147.devlib.DevLib;

/**
 * Created by zhengxin on 2018/3/21.
 */

public class FontBinder {

    private static Typeface DIN;
    private static Typeface DIN_BLOD;
    private static Typeface DIN_LIGHT;
    static {
        DIN = Typeface.createFromAsset(DevLib.getApp().getAssets(),"fonts/DINPro-Medium.ttf");
        DIN_LIGHT = Typeface.createFromAsset(DevLib.getApp().getAssets(),"fonts/DINPro-Light.ttf");
        DIN_BLOD= Typeface.createFromAsset(DevLib.getApp().getAssets(),"fonts/DIN-Bold.ttf");
    }

    @BindingAdapter({"font"})
    public static void setFont(TextView view, String font) {
        if(font.equals("din")){
            view.setTypeface(DIN);
        }else if(font.equals("din_blod")){
            view.setTypeface(DIN_BLOD);
        }else if(font.equals("din_light")){
            view.setTypeface(DIN_LIGHT);
        }

    }

}
