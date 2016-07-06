package com.qzk.picturehandlersample;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;


import com.qzk.picturehandlersample.utils.CaramUtils;
import com.qzk.picturehandlersample.utils.LogUtils;

import java.io.File;

/**
 * 当前类注释：
 * 项目名：PictureSample
 * 包名： com.qzk.picturesample
 * Created by QZK on 2016/5/12.
 */
public class CommonCaramActivity extends Activity {

    private String mImagePath;
    private Activity mActivity = CommonCaramActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != savedInstanceState){
            mImagePath = savedInstanceState.getString("path");
            LogUtils.e("=============>"+mImagePath);
            if(!mImagePath.equals("")){
                Intent intent = new Intent();
                intent.putExtra("path",mImagePath);
                setResult(RESULT_OK,intent);
                finish();
            }

        }else{
            File file = CaramUtils.toCaram(mActivity);
            mImagePath = file.getPath();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (CaramUtils.REQUESTCODE_CARAM == requestCode && resultCode == RESULT_OK) {
            Intent data = new Intent();
            data.putExtra("path",mImagePath);
            setResult(RESULT_OK,data);
            finish();
        } else {
            finish();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("path", mImagePath);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        newConfig.orientation = Configuration.ORIENTATION_PORTRAIT;
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onResume();
    }
}
