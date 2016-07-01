package com.qzk.picturehandlersample.utils;

import android.widget.Toast;

import com.qzk.picturehandlersample.MyApplication;


/**
 * 当前类注释：Toast
 * 项目名：PictureSample
 * 包名： com.qzk.picturesample.utils
 * Created by QZK on 2016/5/10.
 */
public class ToastUtils {

    public static void showLongToast(String msg) {
        Toast.makeText(MyApplication.mApplicaitonContext, msg, Toast.LENGTH_LONG).show();
    }

    public static void showLongToast(int msgId) {
        Toast.makeText(MyApplication.mApplicaitonContext, msgId, Toast.LENGTH_LONG).show();
    }

    public static void showShortToast(String msg) {
        Toast.makeText(MyApplication.mApplicaitonContext, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showShortToast(int msgId) {
        Toast.makeText(MyApplication.mApplicaitonContext, msgId, Toast.LENGTH_SHORT).show();
    }
}
