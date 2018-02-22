package com.zxzx74147.devlib.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.zxzx74147.devlib.DevLib;

/**
 * Created by zhengxin on 2018/2/21.
 */

public class ImageLoader {

    public static void init(Context context){

    }

    public static void loadImage(ImageView imageView,String url){
        Picasso.with(imageView.getContext()).load(url).fit().centerCrop().into(imageView);
    }

    public static void loadImage(String url){
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
