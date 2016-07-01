package com.qzk.picturehandlersample.utils;

import android.os.Environment;

import java.io.File;

/**
 * 当前类注释：SD工具类
 * 项目名：PictureSample
 * 包名： com.qzk.picturesample.utils
 * Created by QZK on 2016/5/10.
 */
public class SDCardUtils {

    /**
     * 获取SD卡路径
     * @return
     */
    public static String getSDCardPath() {
        File sdDir = null;
        boolean sdCardExist = sdCardIsExist();
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();

    }

    /**
     * 判断SD卡是否存在
     * @return
     */
    public static boolean sdCardIsExist() {
        return Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);
    }
}
