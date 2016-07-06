package com.qzk.picturehandlersample.utils;

//import android.view.View;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.qzk.picturehandlersample.MyApplication;
import com.qzk.picturehandlersample.R;

/**
 * 当前类注释：
 * 项目名：PictureSample
 * 包名： com.qzk.picturesample.utils
 * Created by QZK on 2016/5/4.
 */
public class ImageLoaderUtils {

    /**
     * 加载网络图片
     * @param url 图片路径
     * @param imageView
     */
    public static void displayImageFromNet(String url, ImageView imageView){
        //url http://
        ImageLoader.getInstance().displayImage(url,imageView, MyApplication.options);
    }

    /**
     * 加载本地SD卡图片
     * @param path 路径
     * @param imageView
     */
    public static void displayImageFromSDCard(String path, final ImageView imageView){
        //path "file:///mnt/sdcard/image.png"; // from SD card
        ImageLoader.getInstance().displayImage("file://"+path,imageView,MyApplication.options);
        ImageLoader.getInstance().displayImage("file://" + path,
                imageView,MyApplication.options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        imageView .setImageResource(R.mipmap.default_image);
                        super.onLoadingStarted(imageUri, view);
                    }
                });
    }

    /**
     *加载assets 图片
     * @param imageName
     * @param imageView
     */
    public static void displayImageFromAsset(String imageName, ImageView imageView){
        // String imageUri = "assets://image.png"; // from assets
        ImageLoader.getInstance().displayImage("assets://"+imageName,imageView,MyApplication.options);

    }

    /**
     * 加载本地资源图片
     * @param imageId
     * @param imageView
     */
    public static void displayImageFromDrawable(int imageId,final ImageView imageView){
        // String imageUri = "drawable://" + R.drawable.image; // from drawables
        // (only images, non-9patch)
        ImageLoader.getInstance().displayImage("drawable://" + imageId,
                imageView,MyApplication.options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        imageView .setImageResource(R.mipmap.default_image);
                        super.onLoadingStarted(imageUri, view);
                    }
                });
    }


}
