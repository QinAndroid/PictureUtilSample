package com.qzk.picturehandlersample.callback;

/**
 * 类名：HandlerPicture
 * 描述：
 * 包名： com.qzk.picturehandlersample.callback
 * 项目名：PictureHandlerSample
 * Created by qinzongke on 7/6/16.
 */
public abstract class HandlerPicture {

    public abstract void success(String path);
    public abstract void error(String message);
}
