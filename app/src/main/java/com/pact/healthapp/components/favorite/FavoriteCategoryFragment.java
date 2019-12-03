package com.pact.healthapp.components.favorite;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.universal.BaseFragment;

/**
 * Created by wangdong on 2015/11/2.
 */
public class FavoriteCategoryFragment extends BaseFragment {

	@ViewInject(R.id.favorite_listView)
	private ListView listView;

	public FavoriteCategoryFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.favorite_fragment_layout, null);
		((MyFavoriteActivity) getActivity()).setTitle("我的收藏");
		((MyFavoriteActivity) getActivity()).btn_right.setVisibility(View.GONE);
		ViewUtils.inject(this, view);

		listView.setAdapter(new CategoryListViewAdapter(context));
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {// 活动
					mFragmentTransaction = getActivity()
							.getSupportFragmentManager().beginTransaction();
					mFragmentTransaction
							.replace(R.id.login_fl_continer,
									new MyFavoriteFragment("event"))
							.addToBackStack("MyFavoriteFragment").commit();
				} else if (position == 1) {// 版块
					mFragmentTransaction = getActivity()
							.getSupportFragmentManager().beginTransaction();
					mFragmentTransaction
							.replace(R.id.login_fl_continer,
									new MyFavoriteFragment("board"))
							.addToBackStack("MyFavoriteFragment").commit();
				} else if (position == 2) {// 话题
					mFragmentTransaction = getActivity()
							.getSupportFragmentManager().beginTransaction();
					mFragmentTransaction
							.replace(R.id.login_fl_continer,
									new MyFavoriteFragment("topic"))
							.addToBackStack("MyFavoriteFragment").commit();
				}
			}
		});

		return view;
	}

	public class CategoryListViewAdapter extends BaseAdapter {

		private String[] itemName = { "我收藏的活动", "我收藏的板块", "我收藏的话题" };

		private LayoutInflater inflater;

		public CategoryListViewAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return itemName.length;
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.favorite_category_listview_item_layout, null);
				holder = new ViewHolder();
				holder.category_name = (TextView) convertView
						.findViewById(R.id.category_name);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.category_name.setText(itemName[position]);

			return convertView;
		}

		class ViewHolder {
			TextView category_name;
		}

	}
}
