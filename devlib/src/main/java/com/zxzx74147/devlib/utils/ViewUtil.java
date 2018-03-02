package com.zxzx74147.devlib.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.zxzx74147.devlib.DevLib;
import com.zxzx74147.devlib.callback.CommonCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.functions.Consumer;

/**
 * Created by zhengxin on 2018/2/20.
 */

public class ViewUtil {

    public static FragmentActivity  getFragmentActivity(Context context){
        FragmentActivity temp = null;
        if(context instanceof FragmentActivity){
            temp = (FragmentActivity) context;
        }else if(context instanceof ContextThemeWrapper){
            temp = (FragmentActivity) ((ContextThemeWrapper) context).getBaseContext();
        }else if(context instanceof android.view.ContextThemeWrapper){
            temp = (FragmentActivity) ((android.view.ContextThemeWrapper) context).getBaseContext();
        }
        return temp;
    }
    public static FragmentActivity  getFragmentActivity(View v){
        FragmentActivity temp = null;
        if(v.getContext() instanceof FragmentActivity){
            temp = (FragmentActivity) v.getContext();
        }else if(v.getContext() instanceof ContextThemeWrapper){
            temp = (FragmentActivity) ((ContextThemeWrapper) v.getContext()).getBaseContext();
        }else if(v.getContext() instanceof android.view.ContextThemeWrapper){
            temp = (FragmentActivity) ((android.view.ContextThemeWrapper) v.getContext()).getBaseContext();
        }
        return temp;
    }


    public static void mergeRadioButton(CompoundButton ... buttons){
        for(CompoundButton button:buttons){
            RxCompoundButton.checkedChanges(button).subscribe(aBoolean -> {
                if(aBoolean){
                    for(CompoundButton buttonInner:buttons){
                        if(button!=buttonInner){
                            buttonInner.setChecked(false);
                        }
                    }
                }
            });
        }
    }

    public static Bitmap generateBitmapFromView(View mView,int width,int height) {
        mView.setDrawingCacheEnabled(true);
        //图片的宽度为屏幕宽度，高度为wrap_content
        mView.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
        //放置mView
        mView.layout(0, 0, mView.getMeasuredWidth(), mView.getMeasuredHeight());
        mView.buildDrawingCache();
        Bitmap bitmap = mView.getDrawingCache();
        return bitmap;
    }

    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        String storePath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getAbsolutePath();
        File appDir = new File(storePath);
//        if (!appDir.exists()) {
//            appDir.mkdir();
//        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void setSelect( View root,boolean isSelected) {
        dfsView(root, new CommonCallback<View>() {
            @Override
            public void callback(View item) {
                item.setSelected(isSelected);

            }
        });
    }

    public static void dfsView(View root, CommonCallback<View> callback){
        if(root==null||callback==null){
            return;
        }
        callback.callback(root);
        if(root instanceof ViewGroup){
            for(int i=0;i<((ViewGroup) root).getChildCount();i++){
                dfsView(((ViewGroup) root).getChildAt(i),callback);
            }
        }
    }


}
