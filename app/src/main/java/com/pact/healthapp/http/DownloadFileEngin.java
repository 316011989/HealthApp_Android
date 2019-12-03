package com.pact.healthapp.http;

import android.content.Context;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.pact.healthapp.data.Constants;

import java.io.File;

/**
 * 公共请求类
 * 
 * @author 一龙
 * 
 */
public class DownloadFileEngin {

	private static HttpUtils httputil = new HttpUtils();
	private static HttpHandler handler;
	private static String reportUrl = Constants.HOST + Constants.DOWNLOAD_PORT;
	private static String reportPgUrl = Constants.HOST
			+ Constants.DOWNLOADPG_PORT;

	/**
	 * 下载体检报告
	 * 
	 * @author zhangyl
	 * @param context
	 * @param targetPath
	 *            下载文件目标路径 ,Example:save to
	 *            Environment.getExternalStorageDirectory() +
	 *            "/ncihealth/yasuobao.zip"
	 * @param params
	 * @param callback
	 */
	public static void downloadReportFile(Context context, String targetPath,
			RequestParams params, RequestCallBack<File> callback) {
		// 请求超时
		httputil.configSoTimeout(30 * 1000);
		httputil.configTimeout(1000 * 10);
		handler = httputil.download(HttpRequest.HttpMethod.POST, reportUrl,
				targetPath, params, true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
				false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
				callback);
	}

	/**
	 * 取消下载
	 */
	public static void cancel() {
		handler.cancel();
	}

	/**
	 * 下载健康报告
	 * 
	 * @param context
	 * @param targetPath
	 * @param params
	 * @param callback
	 */
	public static void downloadReportPgFile(Context context, String targetPath,
			RequestParams params, RequestCallBack<File> callback) {
		// 请求超时
		httputil.configSoTimeout(30 * 1000);
		httputil.configTimeout(1000 * 10);
		handler = httputil.download(HttpRequest.HttpMethod.POST, reportPgUrl,
				targetPath, params, true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
				false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
				callback);
	}

}
