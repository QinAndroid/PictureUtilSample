package com.qzk.picturehandlersample.entitys;

/**
 * 类名：Model
 * 描述：
 * 包名： com.qzk.picturehandlersample.entitys
 * 项目名：PictureHandlerSample
 * Created by qinzongke on 7/1/16.
 */
public class Model {

    private String path;
    private Long originalSize;
    private Long compressSize;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getOriginalSize() {
        return originalSize;
    }

    public void setOriginalSize(Long originalSize) {
        this.originalSize = originalSize;
    }

    public Long getCompressSize() {
        return compressSize;
    }

    public void setCompressSize(Long compressSize) {
        this.compressSize = compressSize;
    }
}
