package com.qzk.picturehandlersample.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;


import com.qzk.picturehandlersample.WBClipPictureActivity;

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
    //相册多选
    public static final int REQUESTCODE_PICTUREMULT = 300;
    //截取返回
    public static final int REQUESTCODE_CLIP = 400;

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

    /**
     * 相册多选
     * @param activity
     */
    public static void toGetMultPicture(Activity activity) {
//        Intent intent = new Intent(activity, CustomGalleryActivity.class);
//        activity.startActivityForResult(intent,REQUESTCODE_PICTUREMULT);
    }

    /**
     * 跳转裁剪页面
     *
     * @param activity
     */
    public static void toCroupPicture(Activity activity, String path) {
        Intent intent = new Intent();
        intent.setClass(activity, WBClipPictureActivity.class);
        intent.putExtra("path", path);
        activity.startActivityForResult(intent, REQUESTCODE_CLIP);
    }

    public static String handleImageBeforeToCroup(String path) {
        String finalPath = "";
        //获取图片
        Bitmap bit = ImageUtils.getBitmapByPath(path);
        if (null == bit) {
            LogUtils.e("根据路径获取拍照图片失败(CaramUtils-->resetCaramPicture)");
            return finalPath;
        }
        //获取图片角度
        int degree = ImageUtils.getImageAgree(path);
        LogUtils.e("degree=========>" + degree);
        //删除原图像
        ImageUtils.deletePic(path);
        //根据图片角度旋转图片
        bit = ImageUtils.rotateBitmap(bit, degree);
        finalPath = ImageUtils.saveImg(bit);
        ImageUtils.releaseBitmap(bit);
        return finalPath;
    }

    /**
     * 处理拍照后获取的图片 需要做的处理包括 分辨力压缩 质量压缩 图片旋转 保存本地
     *
     * @param path
     * @return
     */
    public static String resetCaramPicture(String path) {
        String finalPath = "";
        //获取图片
        Bitmap bit = ImageUtils.getBitmapByPath(path);
        if (null == bit) {
            LogUtils.e("根据路径获取拍照图片失败(CaramUtils-->resetCaramPicture)");
            return finalPath;
        }
        //压缩图片
        bit = ImageUtils.compressImage(bit);
        //删除原图像
        ImageUtils.deletePic(path);
        finalPath = ImageUtils.saveImg(bit);
        ImageUtils.releaseBitmap(bit);
        return finalPath;

    }


}
