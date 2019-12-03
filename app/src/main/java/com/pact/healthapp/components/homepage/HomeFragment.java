package com.pact.healthapp.components.homepage;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.components.appointment.AppointmentActivity;
import com.pact.healthapp.components.community.ComTopicBean;
import com.pact.healthapp.components.healthreport.HealthReportActivity;
import com.pact.healthapp.components.im.ChatActivity;
import com.pact.healthapp.components.login.LoginActivity;
import com.pact.healthapp.components.topicdetail.TopicdetailActivity;
import com.pact.healthapp.components.topiclist.TopiclistAdapter4ListView;
import com.pact.healthapp.data.Constants;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.universal.WebviewActivity;
import com.pact.healthapp.utils.CommonUtil;
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.pact.healthapp.view.CommonDialog;
import com.pact.healthapp.view.LJListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends BaseFragment {
    private SharedPreferenceUtils sp = new SharedPreferenceUtils();
    private GridView homepage_home_gridview;
    private ArrayList<ComTopicBean> homePageList = new ArrayList<ComTopicBean>();
    private TopiclistAdapter4ListView homepageListViewAdapter;
    @ViewInject(R.id.homepage_home_listView)
    private LJListView homepage_home_listView;

    // 广告栏的变量
    private int currentItem;// 当前页面
    private ViewPager homepage_vp;
    LinearLayout dot_layout;// 广告栏图片上的点
    private ArrayList<View> dots;// 广告栏指示点
    private AdAdapter adAdapter;// 广告adapter
    private ArrayList<HomePageBannerBean> bannersList = new ArrayList<HomePageBannerBean>();
    private int oldPostion = 0;// 原先位置
    private ScheduledExecutorService scheduledExecutorService = Executors
            .newSingleThreadScheduledExecutor();
    private boolean banner_run = false;// 是否可以轮播banner,初始不可以

    boolean addBanner = true;
    boolean resume = true;

    private String[] itemName = {"体检报告", "评估报告", "健康咨询", "预约"};

    private int[] imageId = {R.mipmap.homepage_healthcheckreport,
            R.mipmap.homepage_healthreport,
            R.mipmap.homepage_healthconsult,
            R.mipmap.homepage_appointmentcheckup};

    private static CommonDialog dialog;
    private static boolean isUpdate = true;//是否更新
    private String downloadAddress;//APP更新包下载地址

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    homepage_vp.setCurrentItem(currentItem); // 广告条指示器
                    break;
                case 1:
                    initBrand(bannersList); // 添加广告栏
                    break;
            }
        }

        ;
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.homepage_home_layout, null);
        ViewUtils.inject(this, view);
        addHead();
        getHomeData();

        homepageListViewAdapter = new TopiclistAdapter4ListView(context,
                homePageList);
        homepage_home_listView.setPullLoadEnable(false, "");
        homepage_home_listView.setPullRefreshEnable(false);
        homepage_home_listView.setIsAnimation(true);
        homepage_home_listView.setAdapter(homepageListViewAdapter);
        homepage_home_listView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent topic = new Intent();
                        topic.setClass(getActivity(), TopicdetailActivity.class);
                        topic.putExtra("topicId", homePageList
                                .get(position - 2).getId());
                        startActivity(topic);
                    }
                });

        homepage_home_gridview.setAdapter(new HomePageGridViewAdapter(
                getActivity(), itemName, imageId));
        homepage_home_gridview
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        if (position == 0) {
                            Intent intent = new Intent(context,
                                    HealthReportActivity.class);
                            intent.putExtra("TYPE", "0"); // 0:体检报告
                            startActivity(intent);

                        } else if (position == 1) {
                            Intent intent = new Intent(context,
                                    HealthReportActivity.class);
                            intent.putExtra("TYPE", "1"); // 1:健康报告
                            startActivity(intent);
                        } else if (position == 2) {
                            if (!sp.getLoginState(getActivity())) {
                                Intent intent = new Intent(getActivity(),
                                        LoginActivity.class);
                                startActivityForResult(intent, 100);
                                return;
                            }
                            // 固定与某人聊天,实现伪咨询
                            Intent intent = new Intent(getActivity(),
                                    ChatActivity.class);
                            intent.putExtra("userId", "im672903");
                            startActivity(intent);
                        } else if (position == 3) {
                            if (!sp.getLoginState(getActivity())) {
                                Intent intent = new Intent(getActivity(),
                                        LoginActivity.class);
                                startActivityForResult(intent, 101);
                                return;
                            }
                            Intent intent = new Intent(getActivity(),
                                    AppointmentActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(intent);
                        }
                    }
                });
        return view;
    }


    /**
     * listview添加header（banner和gridview）
     */
    private void addHead() {
        // TODO Auto-generated method stub
        View head = View.inflate(context, R.layout.homepage_head_layout, null);
        homepage_vp = (ViewPager) head.findViewById(R.id.homepage_vp);
        ViewGroup.LayoutParams param = homepage_vp.getLayoutParams();
        param.height = CommonUtil.getScreenWide(getActivity()) / 2;
        homepage_vp.setLayoutParams(param);
        dot_layout = (LinearLayout) head.findViewById(R.id.dot_layout);
        homepage_home_gridview = (GridView) head
                .findViewById(R.id.homepage_home_gridview);
        homepage_home_listView.addHeaderView(head);

    }

    /**
     * 加载首页数据
     */
    private void getHomeData() {
        // TODO Auto-generated method stub
        String bizId = "000";
        String serviceName = "appHome";
        Map<String, String> map = new HashMap<String, String>();
        map.put("page", "1");
        map.put("pageSize", "5");
        String servicePara = JSON.toJSONString(map);
        ServiceEngin.Request(context, bizId, serviceName, servicePara,
                new EnginCallback(context) {
                    @Override
                    public void onSuccess(ResponseInfo arg0) {
                        super.onSuccess(arg0);
                        String result = "";
                        result = Des3.decode(arg0.result.toString());
                        try {
                            JSONObject obj = new JSONObject(result);
                            // 热门帖子
                            JSONArray array = obj.getJSONArray("informations");
                            homePageList.clear();
                            homePageList.addAll(JSON.parseArray(
                                    array.toString(), ComTopicBean.class));
                            homepageListViewAdapter.notifyDataSetChanged();
                            // 广告栏
                            if (addBanner) {
                                addBanner = false;
                                JSONArray banners = obj.getJSONArray("banners");
                                bannersList.addAll(JSON.parseArray(
                                        banners.toString(),
                                        HomePageBannerBean.class));
                                handler.sendEmptyMessage(1);
                                //只在进入APP后检测一次新版本
                                if (isUpdate) {
                                    detectNewVersion();
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 广告栏
     */
    private void initBrand(ArrayList<HomePageBannerBean> bannersList) {
        // TODO Auto-generated method stub
        dots = new ArrayList<View>();
        if ((bannersList.size() > 0)) {
            for (int i = 0; (i < bannersList.size()); i++) {
                View ad_images_dot_item = View.inflate(getActivity(),
                        R.layout.homepage_home_brand_dots, null);
                View ad_dot = ad_images_dot_item.findViewById(R.id.ad_dot);
                dot_layout.addView(ad_images_dot_item);
                dots.add(ad_dot);
            }
            dots.get(0).setBackgroundResource(R.drawable.homepage_dotchecked);
        }
        adAdapter = new AdAdapter(bannersList);
        homepage_vp.setAdapter(adAdapter);
        homepage_vp.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                banner_run = false;// 手动操作banner时停止轮播
                return false;
            }
        });
        homepage_vp.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                dots.get(position).setBackgroundResource(
                        R.drawable.homepage_dotchecked);
                dots.get(oldPostion).setBackgroundResource(
                        R.drawable.homepage_dotdefault);
                currentItem = position;
                oldPostion = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        autoScroll(); // 广告条自动、手动滚动
    }

    // 广告栏adapter
    public final class AdAdapter extends PagerAdapter {
        private ArrayList<HomePageBannerBean> bannersList;
        private BitmapUtils bitmapUtils;

        public AdAdapter(ArrayList<HomePageBannerBean> bannersList) {
            this.bannersList = bannersList;
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
            return bannersList.size();
        }

        // 判断View和Object是否是同一对象
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (ImageView) object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }

        // 向viewPager添加一张图片
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView mImageView = new ImageView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mImageView.setLayoutParams(layoutParams);
            mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            mImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(context, WebviewActivity.class);
                    intent.putExtra("url",
                            Constants.HOST
                                    + Constants.SHOWNEWS_PORT
                                    + bannersList.get(position)
                                    .getConsultXrefId());
                    intent.putExtra("title", bannersList.get(position)
                            .getTitle());
                    startActivity(intent);
                }
            });
            bitmapUtils.display(mImageView, bannersList.get(position)
                    .getImgPath());
            container.addView(mImageView, 0);
            return mImageView;
        }
    }

    /**
     * 广告条滚动定时器
     */
    private void autoScroll() {
        scheduledExecutorService.scheduleAtFixedRate(new AdTask(), 5, 5,
                TimeUnit.SECONDS);
    }

    private final class AdTask implements Runnable {
        public void run() {
            if (banner_run) {
                currentItem = (currentItem + 1) % bannersList.size();
                Message msg = new Message();
                msg.what = 0;
                msg.getData().putLong("currentItem", currentItem);
                handler.sendMessage(msg);
            }
        }
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        // 页面切换时停止banner轮播
        banner_run = false;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // 页面重启时启动banner轮播
        banner_run = true;
        if (resume) {
            resume = false;
        } else {
            getHomeData();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_CANCELED && requestCode == 100) {
            // 固定与某人聊天,实现伪咨询
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra("userId", "im672903");
            startActivity(intent);
        } else if (resultCode != Activity.RESULT_CANCELED && requestCode == 101) {
            Intent intent = new Intent(getActivity(), AppointmentActivity.class);
            startActivity(intent);
        }
    }


    /**
     * 检测新版本
     */
    private void detectNewVersion() {
        // TODO Auto-generated method stub
        String bizId = "000";
        String serviceName = "findNewVersion";
        Map<String, String> map = new HashMap<String, String>();
        String servicePara = JSON.toJSONString(map);
        ServiceEngin.Request(context, bizId, serviceName, servicePara,
                new EnginCallback(context) {
                    @Override
                    public void onSuccess(ResponseInfo arg0) {
//                        super.onSuccess(arg0);
                        canclDialog();
                        String result= Des3.decode(arg0.result.toString());
                        try {
                            JSONObject obj = new JSONObject(result);
                            if (obj.get("errorCode").toString().equals("1112")) {
                                if (obj.getString("downloadAddress") != null)
                                    downloadAddress = obj.getString("downloadAddress");
                                int btnCounts = 2;
                                if (obj.get("isMustUpdate") != null && obj.getBoolean("isMustUpdate"))
                                    btnCounts = 1;
                                if (dialog == null || !dialog.isShowing()) {
                                    dialog = new CommonDialog(context, btnCounts, "立即升级", "暂不升级",
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(Intent.ACTION_VIEW,
                                                            Uri.parse(downloadAddress));
                                                    context.startActivity(intent);
                                                    dialog.cancel();
                                                }
                                            },
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.cancel();
                                                    isUpdate = false;
                                                }
                                            }, obj.getString("resultMsg"), obj.getString("versionDesc"));
                                    dialog.show();
                                    ((TextView) dialog.findViewById(R.id.common_dialog_title)).setTextSize(19);
                                    ((TextView) dialog.findViewById(R.id.common_dialog_content)).setTextSize(15);
                                    ((TextView) dialog.findViewById(R.id.common_dialog_content)).setGravity(Gravity.LEFT);
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
    }
}
