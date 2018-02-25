package com.zxzx74147.stock.binder;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.zxzx74147.devlib.image.ImageLoader;
import com.zxzx74147.stock.data.GoodType;
import com.zxzx74147.stock.widget.GoodListWidget;
import com.zxzx74147.stock.widget.StockWidget;

/**
 * Created by zhengxin on 2018/2/21.
 */

public class GoodBinder {

    @BindingAdapter({"good"})
    public static void loadImage(GoodListWidget view, GoodType good) {
       view.setGood(good);
    }

    @BindingAdapter({"good"})
    public static void loadImage(StockWidget view, GoodType good) {
        view.setGood(good);
    }
}
