package com.zxzx74147.devlib.font;

import android.app.Application;
import android.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.widget.TextView;

import com.zxzx74147.devlib.DevLib;
import com.zxzx74147.devlib.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by zhengxin on 2018/3/21.
 */

public class FontBinder {

    public static Typeface DIN;
    public static Typeface DIN_BLOD;
    public static Typeface DIN_LIGHT;
    static {
        DIN = Typeface.createFromAsset(DevLib.getApp().getAssets(),"fonts/DINPro-Medium.ttf");
        DIN_LIGHT = Typeface.createFromAsset(DevLib.getApp().getAssets(),"fonts/DINPro-Light.ttf");
        DIN_BLOD= Typeface.createFromAsset(DevLib.getApp().getAssets(),"fonts/DIN-Bold.ttf");
    }

    @BindingAdapter({"font"})
    public static void setFont(TextView view, String font) {
        if(font.equals("din")){
            view.setTypeface(DIN);
        }else if(font.equals("din_bold")){
            view.setTypeface(DIN_BLOD);
        }else if(font.equals("din_light")){
            view.setTypeface(DIN_LIGHT);
        }

    }

    public static void init(Application app){
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/DINPro-Medium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

}
