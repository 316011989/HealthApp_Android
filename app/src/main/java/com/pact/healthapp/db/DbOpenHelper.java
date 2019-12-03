package com.pact.healthapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.pact.healthapp.R;
import com.pact.healthapp.utils.CommonUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DbOpenHelper {
	public static final String DB_NAME = "ncihealth.db"; // 保存的数据库文件名
	// 注意这里的PACKAGE_NAME改成你的项目的包名字，如果你不用你的包名那么下面的FileOutputStream fos = new
	// FileOutputStream(dbfile);
	// 会抛出异常，当初本人就是在这卡了好久，因为没认真看代码
	public static final String PACKAGE_NAME = "com.pact.healthapp";
	private SQLiteDatabase sQLiteDatabase;
	private Context context;

	public DbOpenHelper(Context context) {
		this.context = context;

	}

	public SQLiteDatabase openDatabase() {
		String DB_PATH = "/data"
				+ Environment.getDataDirectory().getAbsolutePath() + "/"
				+ context.getPackageName() + "/";
		this.sQLiteDatabase = this.openDatabase(DB_PATH + DB_NAME);
		return sQLiteDatabase;
	}

	private SQLiteDatabase openDatabase(String dbfile) {
		try {
			String versionName = CommonUtil.getVersionName(context);
			String oldVersionName = context.getSharedPreferences("userprefs",
					Context.MODE_PRIVATE).getString("versionName", "");
			if (!(new File(dbfile).exists())
					|| !oldVersionName.equals(versionName)) {// 判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
				// 如果更新版本，需要拷贝新版本数据库和最后更新时间
				// copy();// 拷贝assets里的图片到sd卡里
				InputStream is = this.context.getResources().openRawResource(
						R.raw.healthapp); // 欲导入的数据库
				FileOutputStream fos = new FileOutputStream(dbfile);
				byte[] buffer = new byte[1024 * 100];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
			context.getSharedPreferences("userprefs", Context.MODE_PRIVATE)
					.edit().putString("versionName", versionName).commit();
			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,
					null);
			return db;
		} catch (FileNotFoundException e) {
			Log.e("Database", "File not found");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("Database", "IO exception");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
