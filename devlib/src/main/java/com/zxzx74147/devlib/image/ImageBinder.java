package com.zxzx74147.devlib.image;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;

import com.zxzx74147.devlib.utils.BitmapUtil;

/**
 * Created by zhengxin on 2018/2/21.
 */

public class ImageBinder {

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        ImageLoader.loadImage(view, imageUrl);
    }

    @BindingAdapter({"imageAnim"})
    public static void loadImage(ImageView view, Drawable resource) {
        view.setImageDrawable(resource);
        if (view.getDrawable() instanceof AnimationDrawable) {
            final AnimationDrawable animationDrawable = (AnimationDrawable) view.getDrawable();
            view.post(() -> animationDrawable.start());
        }
    }

    @BindingAdapter({"imageBase64"})
    public static void loadImageFromBase64(ImageView view, String content) {
        if(TextUtils.isEmpty(content)){
            return;
        }
        Bitmap bm = BitmapUtil.stringToBitmap(content);
        view.setImageBitmap(bm);
    }
}
