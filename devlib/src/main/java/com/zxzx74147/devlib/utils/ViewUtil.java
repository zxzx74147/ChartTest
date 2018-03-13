package com.zxzx74147.devlib.utils;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.live.data.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zhengxin on 2018/2/20.
 */

public class ViewUtil {

    public static FragmentActivity getFragmentActivity(Context context) {
        FragmentActivity temp = null;
        if (context instanceof FragmentActivity) {
            temp = (FragmentActivity) context;
        } else if (context instanceof ContextThemeWrapper) {
            temp = (FragmentActivity) ((ContextThemeWrapper) context).getBaseContext();
        } else if (context instanceof android.view.ContextThemeWrapper) {
            temp = (FragmentActivity) ((android.view.ContextThemeWrapper) context).getBaseContext();
        }
        return temp;
    }

    public static FragmentActivity getFragmentActivity(View v) {
        FragmentActivity temp = null;
        if (v.getContext() instanceof FragmentActivity) {
            temp = (FragmentActivity) v.getContext();
        } else if (v.getContext() instanceof ContextThemeWrapper) {
            temp = (FragmentActivity) ((ContextThemeWrapper) v.getContext()).getBaseContext();
        } else if (v.getContext() instanceof android.view.ContextThemeWrapper) {
            temp = (FragmentActivity) ((android.view.ContextThemeWrapper) v.getContext()).getBaseContext();
        }
        return temp;
    }


    public static void mergeRadioButton(CompoundButton... buttons) {
        for (CompoundButton button : buttons) {
            RxCompoundButton.checkedChanges(button).subscribe(aBoolean -> {
                if (aBoolean) {
                    for (CompoundButton buttonInner : buttons) {
                        if (button != buttonInner) {
                            buttonInner.setChecked(false);
                        }
                    }
                }
            });
        }
    }

    public static void mergeRadioView(View... views) {
        for (View view : views) {
            RxView.clicks(view).subscribe(o -> {
                for (View innerView : views) {
                    if (innerView != view) {
                        setSelect(view, false);
                    } else {
                        setSelect(view, true);
                    }
                }
            });
        }
    }

    public static Bitmap generateBitmapFromView(View mView, int width, int height) {
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

    public static void setSelect(View root, boolean isSelected) {
        dfsView(root, new CommonCallback<View>() {
            @Override
            public void callback(View item) {
                item.setSelected(isSelected);

            }
        });
    }

    public static void dfsView(View root, CommonCallback<View> callback) {
        if (root == null || callback == null) {
            return;
        }
        callback.callback(root);
        if (root instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) root).getChildCount(); i++) {
                dfsView(((ViewGroup) root).getChildAt(i), callback);
            }
        }
    }

    public static void hideSoftPad(TextView text){
        InputMethodManager imm = (InputMethodManager) text.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//隐藏软键盘 //
        imm.hideSoftInputFromWindow(text.getWindowToken(), 0);

    }

    public static void showSoftPad(TextView text){
        text.requestFocus();
        InputMethodManager imm = (InputMethodManager) text.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//隐藏软键盘 //

        text.postDelayed(new Runnable() {
            @Override
            public void run() {
                imm.showSoftInput(text, InputMethodManager.SHOW_IMPLICIT);
            }
        },150);


    }

    public static void locateEditCursor(EditText editText){
        editText.setSelection(editText.getText().length());
    }

    public static Lifecycle getLivecircle(View v){
        BaseActivity activity = (BaseActivity) v.getContext();
        return activity.getLifecycle();
    }

    public static Context getContext(Object obj){
        if(obj instanceof Activity){
            return (Context) obj;
        }
        if(obj instanceof Fragment){
            return ((Fragment) obj).getContext();
        }
        return null;

    }

    public static void changeTabs(View tabLayout,String text) {
        if(tabLayout==null){
            return;
        }
        ViewGroup vg = (ViewGroup) tabLayout;
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            if(!(vg.getChildAt(j) instanceof ViewGroup)){
                continue;
            }
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setText(text);
                }
            }
        }
    }

    public static void startAnimition(ImageView imageView){
        Drawable drawable = imageView.getDrawable();
        if(drawable instanceof AnimationDrawable){
            ((AnimationDrawable) drawable).start();
        }else if(drawable instanceof LayerDrawable){
            for(int i=0;i<((LayerDrawable) drawable).getNumberOfLayers();i++) {
                Drawable drawablel = ((LayerDrawable) drawable).getDrawable(i);
                if(drawablel instanceof AnimationDrawable){
                    ((AnimationDrawable) drawablel).start();
                }
            }
        }
    }


}
