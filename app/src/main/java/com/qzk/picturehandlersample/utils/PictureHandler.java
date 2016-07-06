package com.qzk.picturehandlersample.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.Image;

import com.qzk.picturehandlersample.callback.HandlerPicture;

import net.bither.util.NativeUtil;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 类名：PictureHandler
 * 描述：
 * 包名： com.qzk.picturehandlersample.utils
 * 项目名：PictureHandlerSample
 * Created by qinzongke on 7/6/16.
 */
public class PictureHandler {

    private static final String SUCCESS = "1";

    public static void pictureHandler(final Activity activity, final String path, final HandlerPicture callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Result result = handler(path);
                final String message = result.getMessage();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (SUCCESS.equals(message)) {
                            callback.success(result.getPath());
                        } else {
                            callback.error(message);
                        }
                    }
                });


            }
        }).start();


    }

    private static Result handler(String path) {
        Result result = new Result();
        Bitmap bitmap = ImageUtils.getBitmapByPath(path);
        bitmap = ImageUtils.massCompress(bitmap);
        int angle = ImageUtils.getImageAgree(path);
        bitmap = ImageUtils.rotateBitmap(bitmap, angle);
        try {
            String p = SDCardUtils.getSDCardPath() + "/WB";
            File dirFile = new File(p);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            File mediaFile = new File(dirFile.getAbsolutePath() + File.separator + (ConstantUtils.PICTUREPREFIX + System.currentTimeMillis()) + ".jpg");
            if (mediaFile.exists()) {
                mediaFile.delete();
            }
            mediaFile.createNewFile();
            String finalPath = mediaFile.getAbsolutePath();
            String message = NativeUtil.compressBitmap(bitmap, 100, finalPath, true);
            //释放bitmap
            ImageUtils.releaseBitmap(bitmap);
            //删除原文件
            ImageUtils.deletePic(path);
            result.setMessage(message);
            if (SUCCESS.equals(message)) {
                result.setPath(finalPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    static class Result {
        private String path;
        private String message;

        public Result() {

        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}


