package com.pact.healthapp.http;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.pact.healthapp.data.Constants;
import com.pact.healthapp.utils.CommonUtil;


import java.io.File;
import java.io.IOException;

/**
 * 公共请求类
 * 
 * @author 一龙
 * 
 */
public class UploadFileEngin {

	private static HttpUtils httputil = new HttpUtils();
	private static String urlPic = Constants.HOST + Constants.UPLOAD_PORT;
	private static String urlLog = Constants.HOST + Constants.UPLOADLOG_PORT;

	/**
	 * 上传图片
	 * 
	 * @param filename
	 * @param imagePath
	 * @param context
	 * @param callback
	 * @throws IOException
	 */
	public static void uploadFile(String filename, String imagePath,
			Context context, UploadCallback callback) {
		String dir = Environment.getExternalStorageDirectory() + "/ncihealth/";
		if (!new File(dir).exists())
			new File(dir).mkdirs();
		String tempPath = dir + filename;
		File uploadfile = new File(tempPath);
		CommonUtil.compressBmpToFile(imagePath, tempPath);
		RequestParams requestParams = new RequestParams(); // 默认编码UTF-8
		requestParams.addBodyParameter("file", uploadfile);
		requestParams.addBodyParameter("filename", filename);
		// 请求超时
		httputil.configSoTimeout(15 * 1000);
		httputil.configTimeout(1000 * 15);
		// 发送请求
		Log.e("上传图片", "上传" + filename + "大小" + uploadfile.length());
		httputil.send(HttpRequest.HttpMethod.POST, urlPic, requestParams,
				callback);

	}

	/**
	 * 上传crash日志
	 * 
	 * @param fileName
	 * @param context
	 * @param callback
	 */
	public static void uploadLog(String fileName, Context context,
			UploadCallback callback) {
		String dir = Environment.getExternalStorageDirectory()
				+ "/ncihealth/crash/";
		if (!new File(dir).exists())
			new File(dir).mkdirs();
		String tempPath = dir + fileName;
		File uploadfile = new File(tempPath);
		RequestParams requestParams = new RequestParams(); // 默认编码UTF-8
		requestParams.addBodyParameter("file", uploadfile);
		requestParams.addBodyParameter("filename", fileName);
		// 请求超时
		httputil.configSoTimeout(15 * 1000);
		httputil.configTimeout(1000 * 15);
		// 发送请求
		Log.e("crash日志", "上传" + fileName + "大小" + uploadfile.length());
		httputil.send(HttpRequest.HttpMethod.POST, urlLog, requestParams,
				callback);

	}
}
