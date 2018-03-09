package com.zxzx74147.devlib.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import com.zxzx74147.devlib.DevLib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhengxin on 2018/2/20.
 */

public class ImageUtil {
    public static void selectImage(Fragment fragment, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra("return-data", true); //added snippet
        intent = Intent.createChooser(intent, "选择头像");
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void selectImage(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        activity.startActivityForResult(intent, requestCode);
    }

    public static Uri takeCamera(Fragment fragment, int requestCode) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        File file = getImageFile();
        Uri imageUri = Uri.fromFile(file);//The Uri to store the big bitmap
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        fragment.startActivityForResult(intent, requestCode);
        return imageUri;
    }

    public static void takeCamera(Activity activity, int requestCode) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        String IMAGE_FILE_LOCATION = DevLib.getApp().getFilesDir().toString() + "/" + System.currentTimeMillis() + ".jpg";//temp file
        Uri imageUri = Uri.fromFile(new File(IMAGE_FILE_LOCATION));//The Uri to store the big bitmap
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(intent, requestCode);
    }

    public static File getImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        try {
            File image = File.createTempFile(imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    Environment.getExternalStorageDirectory()      /* directory */);
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File saveBitmap(Bitmap bm){

        try {
            File result = getImageFile();
            FileOutputStream output = null;
            output = new FileOutputStream(result);
            bm.compress(Bitmap.CompressFormat.JPEG, 85, output);
            return result;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;


    }

}
