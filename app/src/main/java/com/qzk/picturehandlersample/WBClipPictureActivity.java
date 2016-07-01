package com.qzk.picturehandlersample;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.qzk.picturehandlersample.clippic.ClipImageLayout;
import com.qzk.picturehandlersample.utils.ImageUtils;


/**
 * 当前类注释：裁剪
 * 包名： com.withball.android.activitys
 * Created by QYang on 2016/1/7
 */

public class WBClipPictureActivity extends Activity implements View.OnClickListener {

    private ClipImageLayout srcPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clippicture);
        initView();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                srcPic.setImageDrawable(null);
                this.finish();
                break;
            case R.id.complete:
               new GetBitmapThread(srcPic.clip()).start();
                break;
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent();
            intent.putExtra("path",msg.getData().get("path").toString());
            setResult(RESULT_OK,intent);
            finish();
        }
    };
    class GetBitmapThread extends Thread {
        private Bitmap bitmap;
        public GetBitmapThread(Bitmap bitmap){
            this.bitmap = bitmap;
        }
        @Override
        public void run() {
            super.run();
            String path = ImageUtils.saveImg(this.bitmap);
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("path",path);
            message.setData(bundle);
            handler.sendMessage(message);
        }
    }

    @Override
    protected void onDestroy() {
        srcPic.setImageDrawable(null);
        super.onDestroy();
    }

    private void initView() {
        Button back = (Button) findViewById(R.id.back);
        Button complete = (Button) findViewById(R.id.complete);
        srcPic = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
        Intent intent = getIntent();
        if(intent.hasExtra("path")){
            srcPic.setImageDrawable(ImageUtils.getBitmapByPath(intent.getStringExtra("path")));
        }
        back.setOnClickListener(this);
        complete.setOnClickListener(this);
    }

}