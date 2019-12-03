package com.pact.healthapp.view.imagepick.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.pact.healthapp.R;
import com.pact.healthapp.universal.MyApplication;

import java.util.ArrayList;

/**
 * 主页面中GridView的适配器
 *
 * @author hanj
 */

public class PictrueGVAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> imagePathList = null;

//    private SDCardImageLoader loader;

    public PictrueGVAdapter(Context context, ArrayList<String> imagePathList) {
        this.context = context;
        this.imagePathList = imagePathList;

//        loader = new SDCardImageLoader(ScreenUtils.getScreenW(), ScreenUtils.getScreenH());
    }

    @Override
    public int getCount() {
        return imagePathList == null ? 0 : imagePathList.size();
    }

    @Override
    public Object getItem(int position) {
        return imagePathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String filePath = (String) getItem(position);

        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.view_imagepick_gridview_item, null);
            holder = new ViewHolder();

            holder.imageView = (ImageView) convertView.findViewById(R.id.view_imagepick_item_photo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageView.setTag(filePath);
//        loader.loadImage(3, filePath, holder.imageView);
        MyApplication.bitmapUtils.display(holder.imageView, filePath);
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
    }

}
