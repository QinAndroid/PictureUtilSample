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
        int w = 480;
        int h = 800;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, null));
        return Bitmap.createScaledBitmap(weak.get(), w, h, true);

    }


    /**
     * 压缩图片
     * @param sourceBitmap
     * @return
     */
    public static Bitmap compressImage(Bitmap sourceBitmap) {
        if (null == sourceBitmap) {
            LogUtils.e("压缩源Bitmap为null值");
            return null;
        }
        Bitmap bitmap1 = resolutionCompress(sourceBitmap);
        if (null == bitmap1) {
            LogUtils.e("分辨率压缩失败");
            return null;
        }
        //资源回收
        releaseBitmap(sourceBitmap);
        Bitmap bitmap2 = massCompress(bitmap1);
        //回收
        releaseBitmap(bitmap1);
        if (null == bitmap2) {
            LogUtils.e("质量压缩失败");
            return null;
        }
        return bitmap2;
    }

    /**
     * 分辨率压缩
     *
     * @param image
     * @return
     */
    public static Bitmap resolutionCompress(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 500) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 80, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        releaseBitmap(image);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;//降低图片从ARGB888到RGB565
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());

        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        if (null == bitmap) {
            ToastUtils.showLongToast("图片压缩失败");
            return null;
        }
        return bitmap;//压缩好比例大小后再进行质量压缩
    }

    /**
     * 质量压缩
     *
     * @param image
     * @return
     */
    public static Bitmap massCompress(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 80, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 90) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            options = options - 10;// 每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        AtomicReference<Bitmap> bitmap = new AtomicReference<Bitmap>();// 把ByteArrayInputStream数据生成图片
        bitmap.set(BitmapFactory.decodeStream(isBm, null, null));
        return bitmap.get();
    }

    /**
     * 将bitmap保存至本地并返回文件路径
     *
     * @param b
     * @return
     * @throws Exception
     */
    public static String saveImg(Bitmap b) {
        if (null == b) {
            LogUtils.e("保存至本地的bimap为空");
            return "";
        }
        File imageFolder = new File(SDCardUtils.getSDCardPath() + File.separator + "WB");
        if (!imageFolder.exists()) {
            imageFolder.mkdirs();
        }
        File mediaFile = new File(imageFolder.getAbsolutePath() + File.separator + (ConstantUtils.PICTUREPREFIX + System.currentTimeMillis()) + ".jpg");
        if (mediaFile.exists()) {
            mediaFile.delete();
        }
        try {
            mediaFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(mediaFile);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            System.gc();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mediaFile.getPath();
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
