package com.qzk.picturehandlersample.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;


import java.io.File;
import java.io.IOException;

/**
 * 拍照工具类
 * Created by QZK on 2015/12/16.
 */
public class CaramUtils {

    //拍照
    public static final int REQUESTCODE_CARAM = 100;
    //相册选取
    public static final int REQUESTCODE_PICTURE = 200;

    /**
     * 拍照
     *
     * @param activity
     */
    public static File toCaram(Activity activity) {
        if (!SDCardUtils.sdCardIsExist()) {
            ToastUtils.showLongToast("SD卡不可用");
            return null;
        }
        File caramFolder = new File(SDCardUtils.getSDCardPath() + "/WB");
        if (!caramFolder.exists()) {
            caramFolder.mkdirs();
        }
        File mediaFile = new File(caramFolder.getAbsolutePath() + File.separator + (ConstantUtils.PICTUREPREFIX + System.currentTimeMillis()) + ".jpg");
        try {
            if (mediaFile.exists()) {
                mediaFile.delete();
            }
            mediaFile.createNewFile();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaFile));
            activity.startActivityForResult(intent, REQUESTCODE_CARAM);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaFile;
    }

    /**
     * 相册选取
     *
     * @param activity
     */
    public static void toGetPicture(Activity activity) {
        if (!SDCardUtils.sdCardIsExist()) {
            ToastUtils.showLongToast("SD卡不可用");
            return;
        }
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        activity.startActivityForResult(intent, REQUESTCODE_PICTURE);
    }









}
