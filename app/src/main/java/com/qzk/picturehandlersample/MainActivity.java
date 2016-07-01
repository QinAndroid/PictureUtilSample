package com.qzk.picturehandlersample;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.qzk.picturehandlersample.adapters.MyImageAdapter;
import com.qzk.picturehandlersample.entitys.Model;
import com.qzk.picturehandlersample.utils.CaramUtils;
import com.qzk.picturehandlersample.utils.ImageUtils;
import com.qzk.picturehandlersample.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity{
    private Activity mActivity = MainActivity.this;
    private List<Model> mDatas = new ArrayList<>();
    private MyImageAdapter mAdapter;
    private String path = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        RecyclerView pics = (RecyclerView) findViewById(R.id.pics);
        pics.setLayoutManager(new GridLayoutManager(mActivity, 3));
        mAdapter = new MyImageAdapter(mDatas);
        pics.setAdapter(mAdapter);
        Button take = (Button) findViewById(R.id.take);
        Button getSignal = (Button) findViewById(R.id.getSignal);
        Button getMut = (Button) findViewById(R.id.getMut);
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
        getMut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMutPicture();

            }
        });
    }

    private void takePic() {
        Intent intent = new Intent(mActivity,CommonCaramActivity.class);
        startActivityForResult(intent, CaramUtils.REQUESTCODE_CARAM);
    }

    private void getPic() {
        CaramUtils.toGetPicture(mActivity);
    }

    private void getMutPicture() {
        CaramUtils.toGetMultPicture(mActivity);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CaramUtils.REQUESTCODE_CARAM) {
                String pa = data.getStringExtra("path");
                new HandleCroupPictureThread(pa).start();
            } else if (requestCode == CaramUtils.REQUESTCODE_PICTURE) {
                String p = ImageUtils.uriToPath(mActivity, data.getData());
                new HandleCroupPictureThread(p).start();
            } else if(requestCode == CaramUtils.REQUESTCODE_PICTUREMULT){
//                String[] all_path = data.getStringArrayExtra("all_path");
//                int length = all_path.length;
//                if (length != 0) {
//                    for (int i = 0; i < length; i++) {
//                        mDatas.add(all_path[i]);
//                    }
//                    mAdapter.notifyDataSetChanged();
//                }
            }
            else if (requestCode == CaramUtils.REQUESTCODE_CLIP) {
                LogUtils.e("===========>C++++++++++" + data.getStringExtra("path"));
                new HandleCaramPictureThread(data.getStringExtra("path")).start();
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String path = msg.getData().get("path").toString();
            switch (msg.what) {
                case 1: {
                    Model model = new Model();
                    model.setPath(path);
                    model.setOriginalSize(100l);
                    model.setCompressSize(10l);
                    mDatas.add(model);
                    mAdapter.notifyDataSetChanged();
                }
                break;
                case 2: {
                    CaramUtils.toCroupPicture(mActivity, path);
                }
                break;
            }

        }
    };

    public class HandleCroupPictureThread extends Thread {
        private String path;

        public HandleCroupPictureThread(String path) {
            this.path = path;
        }

        @Override
        public void run() {
            super.run();
            String path = CaramUtils.handleImageBeforeToCroup(this.path);
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("path", path);
            message.setData(bundle);
            message.what = 2;
            handler.sendMessage(message);
        }
    }

    public class HandleCaramPictureThread extends Thread {

        private String path;

        public HandleCaramPictureThread(String path) {
            this.path = path;
        }

        @Override
        public void run() {
            super.run();
            String path = CaramUtils.resetCaramPicture(this.path);
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("path", path);
            message.setData(bundle);
            message.what = 1;
            handler.sendMessage(message);

        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        LogUtils.e("orientationChanged==========>"+newConfig.orientation);
        newConfig.orientation = Configuration.ORIENTATION_PORTRAIT;
        LogUtils.e("orientationSet==========>"+newConfig.orientation);
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onResume() {
        if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onResume();

    }
}
