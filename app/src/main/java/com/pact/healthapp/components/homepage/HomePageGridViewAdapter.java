package com.pact.healthapp.components.homepage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pact.healthapp.R;

public class HomePageGridViewAdapter extends BaseAdapter {

    private String[] itemName;

    private int[] imageId;

    private LayoutInflater inflater;

    public HomePageGridViewAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public HomePageGridViewAdapter(Context context, String[] itemName, int[] imageId) {
        inflater = LayoutInflater.from(context);
        this.itemName = itemName;
        this.imageId = imageId;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return itemName.length;
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return itemName[position];
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(
                    R.layout.homepage_home_gridview_adapter_layout, null);
            holder = new ViewHolder();
            holder.home_item_image = (ImageView) convertView
                    .findViewById(R.id.homepage_home_adapter_image);
            holder.home_item_text = (TextView) convertView
                    .findViewById(R.id.homepage_home_adapter_text);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.home_item_image.setImageResource(imageId[position]);
        holder.home_item_text.setText(itemName[position]);

        return convertView;
    }

    class ViewHolder {
        ImageView home_item_image;
        TextView home_item_text;
    }

}
