package com.qzk.picturehandlersample.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 当前类注释：图片处理类
 * 项目名：PictureSample
 * 包名： com.qzk.picturesample.utils
 * Created by QZK on 2016/5/10.
 */
public class ImageUtils {




    /**
     * 根据Path 获取bitmap
     *
     * @param path
     * @return
     */
    public static Bitmap getBitmapByPath(String path) {
        int w = 768;
        int h = 1280;
//        1920*1080
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ALPHA_8;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            // 缩放
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int) scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
        Bitmap b = weak.get();
        int ww = b.getWidth();
        int hh = b.getHeight();
        return Bitmap.createScaledBitmap(weak.get(), ww/2, hh/2, true);


    }



    /**
     * 质量压缩
     *
     * @param image
     * @return
     */
    public static Bitmap massCompress(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        LogUtils.e("length-->" + baos.toByteArray().length / 1024);
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            options = options - 10;// 每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

        }
        LogUtils.e("option--->" + options);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        AtomicReference<Bitmap> bitmap = new AtomicReference<Bitmap>();// 把ByteArrayInputStream数据生成图片
        bitmap.set(BitmapFactory.decodeStream(isBm, null, null));
        return bitmap.get();
    }


    /**
     * 获取图片角度
     *
     * @param path
     * @return
     */
    public static int getImageAgree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 图片旋转
     *
     * @param bmp    要旋转的图片
     * @param degree 图片旋转的角度，负值为逆时针旋转，正值为顺时针旋转
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bmp, float degree) {
        if (null == bmp) {
            LogUtils.e("旋转图片源为空，");
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
    }

    /**
     * 删除图片
     *
     * @param path
     */
    public static void deletePic(String path) {
        File flie = new File(path);
        if (flie.exists()) {
            flie.delete();
        }
    }

    /**
     * URI 转 Path
     *
     * @param context
     * @param uri
     * @return
     */
    public static String uriToPath(Context context, Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 释放bitmap
     *
     * @param bitmap
     */
    public static void releaseBitmap(Bitmap bitmap) {
        if (null != bitmap) {
            bitmap.recycle();
            bitmap = null;
        }
    }
}
