/**
 *
 */
package com.pact.healthapp.components.news;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.pact.healthapp.R;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;

import java.util.ArrayList;

/**
 * @author zhangyl
 */
public class NewsFragment extends BaseFragment {
    private View view;// 当前fragment的整体布局
    private CategoryTabStrip newstabs;
    private ViewPager news_viewpager;
    private ArrayList<CategoryBean> categories;
    private FragmentStatePagerAdapter adapter;

    public NewsFragment() {
    }

    @SuppressLint("ValidFragment")
    public NewsFragment(ArrayList<CategoryBean> categories) {
        this.categories = categories;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.news_layout, null);
        ViewUtils.inject(this, view);
        newstabs = (CategoryTabStrip) view
                .findViewById(R.id.news_category_strip);
        if (categories == null) {
            // 请求资讯分类数据
            getCategoryList();
        }
        news_viewpager = (ViewPager) view.findViewById(R.id.news_viewpager);
        adapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                if (categories != null && categories.size() > 0) {
                    return categories.size();
                }
                return 0;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if (categories != null && categories.size() > 0) {
                    return categories.get(position).getCategoryName();
                }
                return "";
            }

            @Override
            public Fragment getItem(final int arg0) {
                if (categories != null && categories.size() > 0) {
                    return new PageFragment(categories.get(arg0));
                }
                return new PageFragment();
            }
        };
        news_viewpager.setAdapter(adapter);
        newstabs.setViewPager(news_viewpager);
        return view;
    }

    /**
     * 请求资讯分类数据
     *
     * @author zhangyl
     */
    private void getCategoryList() {
        // TODO Auto-generated method stub
        String result;
        String bizId, serviceName, servicePara;
        bizId = "000";
        serviceName = "queryNewCategories";
        servicePara = "";
        result = ServiceEngin.Request(context, bizId, serviceName, servicePara);
        if (result != null) {
            JSONObject obj = JSON.parseObject(result);
            if (obj != null && obj.get("resultCode") != null
                    && obj.get("resultCode").toString().equals("0")) {// 请求成功
                JSONArray jsonArray = obj.getJSONArray("categories");
                categories = new ArrayList<CategoryBean>();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject object = (JSONObject) jsonArray.get(i);
                    CategoryBean bean = JSON.toJavaObject(object,
                            CategoryBean.class);
                    categories.add(bean);
                }
            }
        }
    }
}
