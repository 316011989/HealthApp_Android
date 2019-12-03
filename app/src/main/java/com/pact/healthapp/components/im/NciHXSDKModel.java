/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pact.healthapp.components.im;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.pact.healthapp.components.im.applib.DbOpenHelper;
import com.pact.healthapp.components.im.applib.HXPreferenceUtils;
import com.pact.healthapp.components.im.applib.HXSDKModel;
import com.pact.healthapp.components.im.applib.User;
import com.pact.healthapp.components.im.applib.UserDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NciHXSDKModel extends HXSDKModel {

	public NciHXSDKModel(Context ctx) {
		context = ctx;
		HXPreferenceUtils.init(context);
	}

	// demo will not use HuanXin roster
	public boolean getUseHXRoster() {
		return false;
	}

	// demo will switch on debug mode
	public boolean isDebugMode() {
		return true;
	}

	public boolean saveContactList(List<User> contactList) {
		UserDao dao = new UserDao(context);
		dao.saveContactList(contactList);
		return true;
	}

	public Map<String, User> getContactList() {
		UserDao dao = new UserDao(context);
		return dao.getContactList();
	}

	public void closeDB() {
		DbOpenHelper.getInstance(context).closeDB();
	}

	@Override
	public String getAppProcessName() {
		return context.getPackageName();
	}

	private static final String PREF_USERNAME = "username";
	private static final String PREF_PWD = "pwd";
	UserDao dao = null;
	protected Context context = null;
	protected Map<Key, Object> valueCache = new HashMap<Key, Object>();

	@Override
	public void setSettingMsgNotification(boolean paramBoolean) {
		HXPreferenceUtils.getInstance().setSettingMsgNotification(paramBoolean);
		valueCache.put(Key.VibrateAndPlayToneOn, paramBoolean);
	}

	@Override
	public boolean getSettingMsgNotification() {
		Object val = valueCache.get(Key.VibrateAndPlayToneOn);

		if (val == null) {
			val = HXPreferenceUtils.getInstance().getSettingMsgNotification();
			valueCache.put(Key.VibrateAndPlayToneOn, val);
		}

		return (Boolean) (val != null ? val : true);
	}

	@Override
	public void setSettingMsgSound(boolean paramBoolean) {
		HXPreferenceUtils.getInstance().setSettingMsgSound(paramBoolean);
		valueCache.put(Key.PlayToneOn, paramBoolean);
	}

	@Override
	public boolean getSettingMsgSound() {
		Object val = valueCache.get(Key.PlayToneOn);

		if (val == null) {
			val = HXPreferenceUtils.getInstance().getSettingMsgSound();
			valueCache.put(Key.PlayToneOn, val);
		}

		return (Boolean) (val != null ? val : true);
	}

	@Override
	public void setSettingMsgVibrate(boolean paramBoolean) {
		HXPreferenceUtils.getInstance().setSettingMsgVibrate(paramBoolean);
		valueCache.put(Key.VibrateOn, paramBoolean);
	}

	@Override
	public boolean getSettingMsgVibrate() {
		Object val = valueCache.get(Key.VibrateOn);

		if (val == null) {
			val = HXPreferenceUtils.getInstance().getSettingMsgVibrate();
			valueCache.put(Key.VibrateOn, val);
		}

		return (Boolean) (val != null ? val : true);
	}

	@Override
	public void setSettingMsgSpeaker(boolean paramBoolean) {
		HXPreferenceUtils.getInstance().setSettingMsgSpeaker(paramBoolean);
		valueCache.put(Key.SpakerOn, paramBoolean);
	}

	@Override
	public boolean getSettingMsgSpeaker() {
		Object val = valueCache.get(Key.SpakerOn);
		if (val == null) {
			val = HXPreferenceUtils.getInstance().getSettingMsgSpeaker();
			valueCache.put(Key.SpakerOn, val);
		}

		return (Boolean) (val != null ? val : true);
	}

	@Override
	public boolean saveHXId(String hxId) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.edit().putString(PREF_USERNAME, hxId).commit();
	}

	@Override
	public String getHXId() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString(PREF_USERNAME, null);
	}

	@Override
	public boolean savePassword(String pwd) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.edit().putString(PREF_PWD, pwd).commit();
	}

	@Override
	public String getPwd() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString(PREF_PWD, null);
	}

	public void setDisabledGroups(List<String> groups) {
		if (dao == null) {
			dao = new UserDao(context);
		}

		dao.setDisabledGroups(groups);
		valueCache.put(Key.DisabledGroups, groups);
	}

	public List<String> getDisabledGroups() {
		Object val = valueCache.get(Key.DisabledGroups);

		if (dao == null) {
			dao = new UserDao(context);
		}

		if (val == null) {
			val = dao.getDisabledGroups();
			valueCache.put(Key.DisabledGroups, val);
		}

		return (List<String>) val;
	}

	public void setDisabledIds(List<String> ids) {
		if (dao == null) {
			dao = new UserDao(context);
		}

		dao.setDisabledIds(ids);
		valueCache.put(Key.DisabledIds, ids);
	}

	public List<String> getDisabledIds() {
		Object val = valueCache.get(Key.DisabledIds);

		if (dao == null) {
			dao = new UserDao(context);
		}

		if (val == null) {
			val = dao.getDisabledIds();
			valueCache.put(Key.DisabledIds, val);
		}

		return (List<String>) val;
	}

	enum Key {
		VibrateAndPlayToneOn, VibrateOn, PlayToneOn, SpakerOn, DisabledGroups, DisabledIds
	}
}
