package com.qzk.picturehandlersample;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import net.bither.util.NativeUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 类名：SecondActivity
 * 描述：
 * 包名： com.qzk.picturehandlersample
 * 项目名：PictureHandlerSample
 * Created by qinzongke on 7/6/16.
 */
public class SecondActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testJpeg();
    }

    private void testJpeg() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    int quality = 90;
                    InputStream in = getResources().getAssets()
                            .open("test.jpg");
                    Bitmap bit = BitmapFactory.decodeStream(in);
                    File dirFile = getExternalCacheDir();
                    if (!dirFile.exists()) {
                        dirFile.mkdirs();
                    }
                    File originalFile = new File(dirFile, "original.jpg");
                    FileOutputStream fileOutputStream = new FileOutputStream(
                            originalFile);
                    bit.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream);
                    File jpegTrueFile = new File(dirFile, "jpegtrue.jpg");
                    File jpegFalseFile = new File(dirFile, "jpegfalse.jpg");
                    NativeUtil.compressBitmap(bit, quality,
                            jpegTrueFile.getAbsolutePath(), true);
                    NativeUtil.compressBitmap(bit, quality,
                            jpegFalseFile.getAbsolutePath(), false);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }).start();
    }

}
