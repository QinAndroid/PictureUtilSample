package com.qzk.picturehandlersample;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.WindowManager;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.L;
import com.qzk.picturehandlersample.utils.LogUtils;

/**
 * 当前类注释：
 * 项目名：PictureSample
 * 包名： com.qzk.picturesample
 * Created by QZK on 2016/5/4.
 */
public class MyApplication extends Application {
    public static DisplayImageOptions options, optionRound, optionWelcome;
    public static Context mApplicaitonContext;
    public static int PhoneWidth = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        boolean isLog = true;
        LogUtils utils = new LogUtils(isLog);
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        PhoneWidth = wm.getDefaultDisplay().getWidth();
        mApplicaitonContext = getApplicationContext();
        imageLoaderConfig();
    }



    /**
     * imageLoader配置
     */
    @SuppressWarnings("deprecation")
    private void imageLoaderConfig() {
        L.disableLogging();
        // 创建默认的ImageLoader配置参数
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCacheExtraOptions(480, 800) // 即保存的每个缓存文件的最大长宽
                .threadPriority(Thread.NORM_PRIORITY - 2) // 线程池中线程的个数
                .denyCacheImageMultipleSizesInMemory() // 禁止缓存多张图片
                .memoryCache(new LRULimitedMemoryCache(50 * 1024 * 1024)) // 缓存策略6
                .memoryCacheSize(50 * 1024 * 1024) // 设置内存缓存的大小
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) // 缓存文件名的保存方式
                .diskCacheSize(200 * 1024 * 1024) // 磁盘缓存大小
                .tasksProcessingOrder(QueueProcessingType.LIFO) // 工作队列
                .diskCacheFileCount(200) // 缓存的文件数量
                // .writeDebugLogs() // Remove for release app
                .build();

        ImageLoader.getInstance().init(config);
        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        optionWelcome = new DisplayImageOptions.Builder().showStubImage(R.mipmap.default_image) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.default_image) //
                // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.default_image) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                .bitmapConfig(Bitmap.Config.RGB_565)
                // .displayer(new RoundedBitmapDisplayer(10)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象
        options = new DisplayImageOptions.Builder().showStubImage(R.mipmap.default_image) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.default_image) //
                // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.default_image) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                .bitmapConfig(Bitmap.Config.RGB_565)
                // .displayer(new RoundedBitmapDisplayer(10)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象
        optionRound = new DisplayImageOptions.Builder().showStubImage(R.mipmap.default_image)
                // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.default_image)
                //
                // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.default_image)
                // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)
                // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)
                // 设置下载的图片是否缓存在SD卡中
                // 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .bitmapConfig(Bitmap.Config.RGB_565).displayer(new RoundedBitmapDisplayer(15)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象
    }
}
