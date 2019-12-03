/**
 *
 */
package com.pact.healthapp.components.news;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.pact.healthapp.R;
import com.pact.healthapp.data.Constants;
import com.pact.healthapp.universal.WebviewActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author zhangyl
 */
public class BannerAdapter extends PagerAdapter {
    private ArrayList<NewsBean> list;
    private Context context;
    private HashMap<Integer, View> views = new HashMap<Integer, View>();
    private BitmapUtils bitmapUtils;

    public BannerAdapter(Context context, ArrayList<NewsBean> list) {
        this.list = list;
        this.context = context;
        bitmapUtils = new BitmapUtils(context);
        BitmapDisplayConfig bitmapDisplayConfig = new BitmapDisplayConfig();
        bitmapDisplayConfig.setLoadFailedDrawable(context.getResources()
                .getDrawable(R.mipmap.defalt_pic_width));
        bitmapDisplayConfig.setLoadingDrawable(context.getResources()
                .getDrawable(R.mipmap.defalt_pic_width));
        bitmapUtils.configDefaultDisplayConfig(bitmapDisplayConfig);
    }

    @Override
    public int getCount() {
        if (list != null && list.size() == 1) {
            return 1;
        } else if (list != null && list.size() > 1) {
            return list.size();
        }
        return 1;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    /**
     * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
     */
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView view = new ImageView(context);
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        if (list != null && list.size() > 0) {
            bitmapUtils.display(view, list.get(position % list.size())
                    .getImage());
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(context, WebviewActivity.class);
                    intent.putExtra("url",
                            Constants.HOST
                                    + Constants.SHOWNEWS_PORT
                                    + list.get(position % list.size())
                                    .getNewsId());
                    intent.putExtra("title", list.get(position % list.size())
                            .getTitle());
                    context.startActivity(intent);
                }
            });
            views.put(position, view);
        } else {
            bitmapUtils.display(view, null);
            views.put(position, view);
        }
        container.addView(view);
        return view;
    }
}