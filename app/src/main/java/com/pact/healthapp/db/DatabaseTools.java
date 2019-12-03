package com.pact.healthapp.db;

import android.database.Cursor;

import com.pact.healthapp.universal.Framework;
import com.pact.healthapp.universal.MyApplication;

import java.util.ArrayList;

public class DatabaseTools {
	/**
	 * 底栏查询
	 * 
	 * @param db
	 * @return
	 */
	public static ArrayList<Framework> findHomeTabs() {
		ArrayList<Framework> list = new ArrayList<Framework>();
		Cursor c = MyApplication.db
				.rawQuery(
						"select * from Framework where parentId='0' order by moduleOrderby",
						null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			Framework framework;
			for (int i = 0; i < c.getCount(); i++) {
				c.moveToPosition(i);
				framework = new Framework();
				framework
						.setModuleId(c.getString(c.getColumnIndex("moduleId")));
				framework.setIsLogin(c.getString(c.getColumnIndex("isLogin")));
				framework
						.setParentId(c.getString(c.getColumnIndex("parentId")));
				framework.setIsVisible(c.getString(c
						.getColumnIndex("isVisible")));
				framework.setIsVisibleOrder(c.getString(c
						.getColumnIndex("isVisibleOrder")));
				framework.setFixedPage(c.getString(c
						.getColumnIndex("fixedPage")));
				framework.setPackageName(c.getString(c
						.getColumnIndex("packageName")));
				framework.setModuleType(c.getString(c
						.getColumnIndex("moduleType")));
				framework
						.setIconName(c.getString(c.getColumnIndex("iconName")));
				framework.setThumbnailName(c.getString(c
						.getColumnIndex("thumbnailName")));
				framework.setModuleName(c.getString(c
						.getColumnIndex("moduleName")));
				framework.setIsMenuItem(c.getString(c
						.getColumnIndex("isMenuItem")));
				framework.setIsAddMenuItem(c.getString(c
						.getColumnIndex("isAddMenuItem")));
				framework.setModuleOrderby(c.getString(c
						.getColumnIndex("moduleOrderby")));
				framework
						.setClickUrl(c.getString(c.getColumnIndex("clickUrl")));
				framework.setUpdateDate(c.getString(c
						.getColumnIndex("updateDate")));
				list.add(framework);
			}
		}
		return list;
	}
}
