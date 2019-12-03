/**
 *
 */
package com.pact.healthapp.components.news;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.data.Constants;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.universal.WebviewActivity;
import com.pact.healthapp.view.LJListView;
import com.pact.healthapp.view.LJListView.IXListViewListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangyl
 *
 */
@SuppressLint("ValidFragment")
public class PageFragment extends BaseFragment {
	private CategoryBean categoryBean;
	private final String ICICLE_KEY = "stateInfo";

	@ViewInject(R.id.homepage_home_listView)
	private LJListView listview; // 资讯列表
	private ArrayList<NewsBean> newslist = new ArrayList<NewsBean>();
	private NewslistAdapter4ListView listAdapter;

	// 广告栏的变量
	private ViewPager news_banner_viewpager;
	private ArrayList<NewsBean> bannerlist = new ArrayList<NewsBean>();
	private BannerAdapter bannerAdapter;
	private TextView news_banner_title;
	private int currentItem;// 当前页面
	private int oldPostion = 0;// 原先位置
	private LinearLayout dot_layout;// 广告栏图片上的点
	private ArrayList<View> dots;// 广告栏指示点
	private ScheduledExecutorService scheduledExecutorService = Executors
			.newSingleThreadScheduledExecutor();
	protected static boolean pressed = true;

	private boolean addbanner = true;
	private int page = 1;

	public PageFragment(CategoryBean categoryBean) {
		this.categoryBean = categoryBean;
	}

	public PageFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// 界面复用首页界面
		View view = inflater.inflate(R.layout.homepage_home_layout, null);
		ViewUtils.inject(this, view);
		// 如果界面是从分享功能返回的,那么需要将之前保存的categoryBean取出,否则会被系统回收.红米的系统分享拍照返回时都会清空一些变量
		getState(savedInstanceState);
		if (categoryBean == null) {
			categoryBean = new CategoryBean();
		}
		addBanner();
		listAdapter = new NewslistAdapter4ListView(context, newslist);
		listview.setAdapter(listAdapter);
		getNewsList(page, System.currentTimeMillis() + "", "new");
		listview.setPullLoadEnable(true, "");
		listview.setPullRefreshEnable(true);
		listview.setIsAnimation(true);
		listview.setXListViewListener(new IXListViewListener() {
			// 向下滑动刷新
			@Override
			public void onRefresh() {
				listview.setPullLoadEnable(false, "");
				String releaseTime = "";
				if (newslist != null && newslist.size() > 0) {
					releaseTime = newslist.get(0).getReleaseTime();
				} else {
					releaseTime = System.currentTimeMillis() + "";
				}
				getNewsList(0, releaseTime + "", "new");
			}

			// 向上滑动加载更多
			@Override
			public void onLoadMore() {
				listview.setPullRefreshEnable(false);
				String releaseTime = "";
				if (newslist != null && newslist.size() > 0) {
					releaseTime = newslist.get(newslist.size() - 1)
							.getReleaseTime();
				} else {
					releaseTime = System.currentTimeMillis() + "";
				}
				page++;
				getNewsList(page++, releaseTime + "", "old");
			}
		});
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(context, WebviewActivity.class);
				intent.putExtra("url", Constants.HOST + Constants.SHOWNEWS_PORT
						+ newslist.get(position - 1 - 1).getNewsId());
				intent.putExtra("title", newslist.get(position - 1 - 1)
						.getTitle());
				startActivity(intent);
			}
		});
		return view;
	}

	/**
	 * @author zhangyl
	 */
	private void getState(Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			// We were just launched -- set up a new game
		} else {
			// We are being restored
			Bundle map = savedInstanceState.getBundle(ICICLE_KEY);
			if (map != null) {
				categoryBean = (CategoryBean) map
						.getSerializable("categoryBean");
			} else {
				categoryBean = new CategoryBean();
			}
		}
	}

	private void onLoad(int num) {
		listview.setCount(num + "");
		listview.setRefreshTime("刚刚");
	}

	/**
	 * 请求资讯某一类数据
	 *
	 * @author zhangyl
	 */
	private void getNewsList(int page, String refreshTime, final String type) {
		// TODO Auto-generated method stub
		String bizId, serviceName, servicePara;
		bizId = "000";
		serviceName = "queryNewsList";
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("categoryId", categoryBean.getCategoryId());
		map.put("startTime", refreshTime);
		map.put("queryType", type);
		map.put("pageSize", "15");
		map.put("page", page + "");
		servicePara = JSON.toJSONString(map);
		ServiceEngin.Request(context, bizId, serviceName, servicePara,
				new EnginCallback(context) {
					@Override
					public void onSuccess(ResponseInfo arg0) {
						super.onSuccess(arg0);
						String result = "";
						try {
							result = Des3.decode(arg0.result.toString());
							ParseJson(result, type);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	/**
	 * 解析服务器返回Json
	 */
	private void ParseJson(String result, String type) {
		JSONObject obj = JSON.parseObject(result);
		if (obj.get("resultCode") != null
				&& obj.get("resultCode").toString().equals("0")) {// 请求成功
			JSONArray jsonArray = obj.getJSONArray("newsList");
			ArrayList<NewsBean> news = new ArrayList<NewsBean>();
			ArrayList<NewsBean> banner = new ArrayList<NewsBean>();
			NewsBean newsBean;
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				newsBean = JSON.toJavaObject(object, NewsBean.class);
				if (newsBean.getIsBanner().equals("1"))
					banner.add(newsBean);
				else
					news.add(newsBean);
			}

			if (type.equals("new")) {
				if (news.size() < 15) {
					newslist.addAll(0, news);
				} else {
					newslist.clear();
					newslist.addAll(news);
				}
				onLoad(news.size());
				if (banner.size() < 15) {
					bannerlist.addAll(0, banner);
				} else {
					bannerlist.clear();
					bannerlist.addAll(banner);
				}
			} else {
				newslist.addAll(news);
				bannerlist.addAll(banner);
			}
			listAdapter.notifyDataSetChanged();
			listview.stopLoadMore();
			listview.stopRefresh();
			listview.setPullLoadEnable(true, "");
			listview.setPullRefreshEnable(true);
			if (addbanner) {
				addbanner = false;
				initBanner();
			}
		}
	}

	/**
	 * 添加banner
	 *
	 */
	private void addBanner() {
		View head = LayoutInflater.from(context).inflate(
				R.layout.newslist_banner_layout, null);
		news_banner_viewpager = (ViewPager) head
				.findViewById(R.id.news_banner_viewpager);
		news_banner_title = (TextView) head
				.findViewById(R.id.news_banner_title);
		dot_layout = (LinearLayout) head.findViewById(R.id.dot_layout);
		listview.addHeaderView(head);
	}

	/**
	 * 添加指示器
	 */
	private void initBanner() {

		dots = new ArrayList<View>();
		if ((bannerlist.size() > 0)) {
			for (int i = 0; (i < bannerlist.size()); i++) {
				View ad_images_dot_item = View.inflate(getActivity(),
						R.layout.homepage_home_brand_dots, null);
				View ad_dot = ad_images_dot_item.findViewById(R.id.ad_dot);
				dot_layout.addView(ad_images_dot_item);
				dots.add(ad_dot);
			}
			dots.get(0).setBackgroundResource(R.drawable.homepage_dotchecked);
		}
		bannerAdapter = new BannerAdapter(context, bannerlist);
		news_banner_viewpager.setAdapter(bannerAdapter);
		news_banner_viewpager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				pressed = false;
				return false;
			}
		});

		news_banner_title.setText(bannerlist.get(0).getTitle());
		news_banner_viewpager
				.setOnPageChangeListener(new OnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						dots.get(position).setBackgroundResource(
								R.drawable.homepage_dotchecked);
						dots.get(oldPostion).setBackgroundResource(
								R.drawable.homepage_dotdefault);
						currentItem = position;
						oldPostion = position;
						news_banner_title.setText(bannerlist.get(position)
								.getTitle());
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

	/**
	 * 广告条滚动定时器
	 */
	private void autoScroll() {
		scheduledExecutorService.scheduleAtFixedRate(new AdTask(), 5, 5,
				TimeUnit.SECONDS);
	}

	private final class AdTask implements Runnable {
		public void run() {
			if (pressed) {
				currentItem = (currentItem + 1) % bannerlist.size();
				Message msg = new Message();
				msg.what = 0;
				msg.getData().putLong("currentItem", currentItem);
				handler.sendMessage(msg);
			} else {
				pressed = true;
			}
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				news_banner_viewpager.setCurrentItem(currentItem); // 广告条指示器
				break;
			default:
				break;
			}
		}
	};
//	boolean resume = true;
//
//	@Override
//	public void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		if (resume) {
//			resume = false;
//		} else {
//			page = 1;
//			newslist.clear();
//			bannerlist.clear();
//			getNewsList(page, System.currentTimeMillis() + "", "new");
//		}
//	};

	/**
	 * activity结束时保存数据
	 */
	public void onSaveInstanceState(Bundle outState) {
		// Store the game state
		outState.putBundle(ICICLE_KEY, saveState());
	}

	/**
	 * 保存数据
	 *
	 * @author zhangyl
	 * @return
	 */
	public Bundle saveState() {
		Bundle map = new Bundle();
		map.putSerializable("categoryBean", categoryBean);
		return map;
	}
}
