package com.zxzx74147.devlib.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.zxzx74147.devlib.DevLib;
import com.zxzx74147.devlib.callback.CommonCallback;

import java.io.IOException;

/**
 * Created by zhengxin on 2018/2/21.
 */

public class ImageLoader {

    public static void init(Context context){

    }

    public static void loadImage(ImageView imageView,String url){
        if(TextUtils.isEmpty(url)){
            return;
        }
        Picasso.with(imageView.getContext()).load(url).fit().centerCrop().into(imageView);
    }

    public static Bitmap loadImageSync(String url,int width,int height){
        try {
            Bitmap bitmap =  Picasso.with(DevLib.getApp()).load(url).resize(width,height).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void loadImageAync(String url, int width, int height, CommonCallback callback){
         Picasso.with(DevLib.getApp()).load(url).resize(width,height).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    callback.callback(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    callback.callback(errorDrawable);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });


    }
    public static void loadImage(String url){
        if(TextUtils.isEmpty(url)){
            return;
        }
        Picasso.with(DevLib.getApp()).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }
}
