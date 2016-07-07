package com.qzk.picturehandlersample;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.qzk.picturehandlersample.adapters.MyImageAdapter;
import com.qzk.picturehandlersample.callback.HandlerPicture;
import com.qzk.picturehandlersample.utils.CaramUtils;
import com.qzk.picturehandlersample.utils.ImageUtils;
import com.qzk.picturehandlersample.utils.LogUtils;
import com.qzk.picturehandlersample.utils.PermissionUtils;
import com.qzk.picturehandlersample.utils.PictureHandler;
import com.qzk.picturehandlersample.utils.SDCardUtils;
import com.qzk.picturehandlersample.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private Activity mActivity = MainActivity.this;
    private List<String> mDatas = new ArrayList<>();
    private MyImageAdapter mAdapter;
    private String path = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        PermissionUtils.getCameraPermissions(mActivity);
    }

    private void initView() {
        RecyclerView pics = (RecyclerView) findViewById(R.id.pics);
        pics.setLayoutManager(new GridLayoutManager(mActivity, 3));
        mAdapter = new MyImageAdapter(mDatas);
        pics.setAdapter(mAdapter);
        Button take = (Button) findViewById(R.id.take);
        Button getSignal = (Button) findViewById(R.id.getSignal);
        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePic();
            }
        });
        getSignal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPic();
            }
        });
    }

    private void takePic() {
        Intent intent = new Intent(mActivity, CommonCaramActivity.class);
        startActivityForResult(intent, CaramUtils.REQUESTCODE_CARAM);
    }

    private void getPic() {
        CaramUtils.toGetPicture(mActivity);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String path = "";
            if (requestCode == CaramUtils.REQUESTCODE_CARAM) {
                path = data.getStringExtra("path");

            } else if (requestCode == CaramUtils.REQUESTCODE_PICTURE) {
                path = ImageUtils.uriToPath(mActivity, data.getData());
            }
            if (!path.equals("")) {
                handlerImage(path);
            }
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        LogUtils.e("orientationChanged==========>" + newConfig.orientation);
        newConfig.orientation = Configuration.ORIENTATION_PORTRAIT;
        LogUtils.e("orientationSet==========>" + newConfig.orientation);
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onResume() {
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onResume();

    }

    private void handlerImage(String path) {
        PictureHandler.pictureHandler(mActivity, path, new HandlerPicture() {
            @Override
            public void success(String path) {
                LogUtils.e("success");
                mDatas.add(path);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void error(String message) {
                LogUtils.e("error");
                ToastUtils.showLongToast(message);
            }
        });
    }

}
