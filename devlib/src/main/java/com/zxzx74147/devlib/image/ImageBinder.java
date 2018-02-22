package com.zxzx74147.devlib.image;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.zxzx74147.devlib.DevLib;

/**
 * Created by zhengxin on 2018/2/21.
 */

public class ImageBinder {

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        ImageLoader.loadImage(view, imageUrl);
    }
}
