/**
 * 
 */
package com.pact.healthapp.universal;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pact.healthapp.view.CommonDialog;
import com.pact.healthapp.view.CommonPrograssDialog;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author zhangyl
 * 
 */
@SuppressLint("ValidFragment")
public class WebviewFragment extends BaseFragment {
	private WebView webview;
	private String url;
	private CommonPrograssDialog prograssDialog;
	private CommonDialog dialog;
	private long timeout = 15 * 1000;
	private Timer timer;

	public WebviewFragment() {
	}
	
	public WebviewFragment(String url) {
		this.url = url;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// webview = inflater.inflate(R.layout.universal_webview, null);
		webview = new WebView(context);
		prograssDialog = CommonPrograssDialog.getInstance(context);
		prograssDialog.show();
		webview.loadUrl(url);
		webview.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress)// 设置 加载进程
			{
				if (progress >= 100) {
					Message msg = new Message();
					msg.what = 0;
					handler.sendMessage(msg);
					timer.cancel();
					timer.purge();
				}
			}

		});
		webview.setWebViewClient(new WebViewClient() {
			/*
			 * 创建一个WebViewClient,重写onPageStarted和onPageFinished
			 * 
			 * 
			 * onPageStarted中启动一个计时器,到达设置时间后利用handle发送消息给activity执行超时后的动作.
			 */
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				timer = new Timer();
				TimerTask tt = new TimerTask() {
					@Override
					public void run() {
						/*
						 * 超时后,首先判断页面加载进度,超时并且进度小于100,就执行超时后的动作
						 */
						if (webview.getProgress() < 100) {
							Message msg = new Message();
							msg.what = 2;
							handler.sendMessage(msg);
							timer.cancel();
							timer.purge();
						}
					}
				};
				timer.schedule(tt, timeout, 1);
			}

			/**
			 * onPageFinished指页面加载完成,完成后取消计时器
			 */
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				timer.cancel();
			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				view.stopLoading();
				view.clearView();
				Message msg = handler.obtainMessage();// 发送通知，加入线程
				msg.what = 1;// 通知加载自定义404页面
				handler.sendMessage(msg);// 通知发送！
			}

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});

		return webview;
	}

	/**
	 * handler处理消息机制
	 */
	protected  Handler handler = new Handler() {
		public void handleMessage(Message message) {
			switch (message.what) {
			case 0:// 成功
				prograssDialog.dismiss();
				break;
			case 1:// 失败
				dialog = new CommonDialog(context, 1, "确定", "",
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								((WebviewActivity) context).finish();
							}
						}, null, null, "加载失败");
				dialog.show();
				// webView.loadUrl(URL404);
				break;
			case 2:
				dialog = new CommonDialog(context, 1, "确定", "",
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								((WebviewActivity) context).finish();
							}
						}, null, null, "加载超时");
				dialog.show();
				// myDialog.dismiss();
				break;
			}
		}
	};
}
