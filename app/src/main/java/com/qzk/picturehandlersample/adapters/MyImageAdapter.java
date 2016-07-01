package com.qzk.picturehandlersample.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.qzk.picturehandlersample.R;
import com.qzk.picturehandlersample.entitys.Model;
import com.qzk.picturehandlersample.utils.ImageLoaderUtils;

import java.util.List;

/**
 * 当前类注释：
 * 项目名：PictureSample
 * 包名： com.qzk.picturesample.adapter
 * Created by QZK on 2016/5/11.
 */
public class MyImageAdapter extends RecyclerView.Adapter<MyImageAdapter.ViewHolder>{
    private List<Model> mDatas;
    public MyImageAdapter(List<Model> data){
        this.mDatas = data;
    }
    @Override
    public MyImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contanier = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image,null);
        return new ViewHolder(contanier);
    }

    @Override
    public void onBindViewHolder(MyImageAdapter.ViewHolder holder, int position) {
        Model model = this.mDatas.get(position);
        String path = model.getPath();
        Long original = model.getOriginalSize();
        Long compress = model.getCompressSize();
        holder.item_original.setText("原始大小:"+original);
        holder.item_compress.setText("压缩后大小:"+compress);
        ImageLoaderUtils.displayImageFromSDCard(path,holder.item_image);
//        ImageLoaderUtils.displayImageFromDrawable(R.mipmap.ic_launcher,holder.item_image);

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView item_image;
        public TextView item_original;
        public TextView item_compress;
        public ViewHolder(View itemView) {
            super(itemView);
            this.item_image = (ImageView) itemView.findViewById(R.id.item_img);
            this.item_compress = (TextView)itemView.findViewById(R.id.item_compress);
            this.item_original = (TextView)itemView.findViewById(R.id.item_original);
        }
    }
}
