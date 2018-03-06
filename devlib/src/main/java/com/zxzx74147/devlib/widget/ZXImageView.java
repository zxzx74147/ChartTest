package com.zxzx74147.devlib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.makeramen.roundedimageview.RoundedImageView;
import com.zxzx74147.devlib.R;

/**
 * Created by zhengxin on 2018/2/21.
 */

public class ZXImageView extends RoundedImageView {

    private int mAspectRatioWidth;
    private int mAspectRatioHeight;

    public ZXImageView(Context context) {
        super(context);
    }

    public ZXImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ZXImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }


    private void init(AttributeSet attrs){
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ZXImageView);

        mAspectRatioWidth = a.getInt(R.styleable.ZXImageView_aspectRatioWidth, 0);
        mAspectRatioHeight = a.getInt(R.styleable.ZXImageView_aspectRatioHeight, 0);

        a.recycle();
    }


    @Override protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
    {
        int originalWidth = MeasureSpec.getSize(widthMeasureSpec);

        int originalHeight = MeasureSpec.getSize(heightMeasureSpec);
        if(mAspectRatioHeight==0||mAspectRatioWidth==0){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        int calculatedHeight = originalWidth * mAspectRatioHeight / mAspectRatioWidth;

        int finalWidth, finalHeight;

        finalWidth = originalWidth;
        finalHeight = calculatedHeight;
//        if (calculatedHeight > originalHeight)
//        {
//            finalWidth = originalHeight * mAspectRatioWidth / mAspectRatioHeight;
//            finalHeight = originalHeight;
//        }
//        else
//        {
//            finalWidth = originalWidth;
//            finalHeight = calculatedHeight;
//        }

        super.onMeasure(
                MeasureSpec.makeMeasureSpec(finalWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(finalHeight, MeasureSpec.EXACTLY));
    }
}
