package com.pact.healthapp.view.imageshow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.easemob.util.NetUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.components.healthreport.HealthReportActivity;
import com.pact.healthapp.http.DownloadFileEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.utils.CommonUtil;
import com.pact.healthapp.view.CommonDialog;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class ImageFramgent extends BaseFragment implements OnPageChangeListener {
	@ViewInject(R.id.view_progress_bar)
	private ProgressBar view_progress_bar;// 下载进度条,下载完成后隐藏
	@ViewInject(R.id.view_photosname)
	private TextView view_photosname;// 体检报告名称
	@ViewInject(R.id.view_photosdate)
	private TextView view_photosdate;// 体检报告日期
	@ViewInject(R.id.view_photodownloading)
	private TextView view_photodownloading;// 下载中提示,下载完成后隐藏
	@ViewInject(R.id.page_text)
	private TextView pageText;// 页码
	@ViewInject(R.id.view_pager)
	private ViewPagerFixed viewPager;// 用于管理图片的滑动

	private int type;// 1为URL 2为本地图片3为本地绝对路径
	private int currentPosition = 0;
	private ArrayList<String> paths = new ArrayList<String>();;// 传入的图片路径
	private String healthCheckNumber;// 下载体检报告的参数
	private String date;// 显示体检报告的参数
	private String reportname;// 显示体检报告的参数
	private ViewPagerAdapter adapter;
	private CommonDialog commonDialog;// 网络状态提示框

	private String fileUrl;// 下载健康报告的参数
	private String customerId;// 下载健康报告的参数
	private String fileName;// 健康报告压缩包的名字
	private boolean isReLoad;// 是否是重新下载

	private CommonDialog commonDialog2;// 取消下载提示框
	private CommonDialog commonDialog3;// 下载失败提示框

	/**
	 * 显示当前图片的页数
	 */

	public ImageFramgent() {
	}

	/**
	 * 
	 * @param paths
	 * @param type
	 *            1为URL 2为本地路径
	 * @param currentPosition
	 */
	public ImageFramgent(ArrayList<String> paths, int type, int currentPosition) {
		this.paths = paths;
		this.type = type;
		this.currentPosition = currentPosition;
	}

	/**
	 * 体检报告
	 * 
	 * @param healthCheckNumber
	 * @param date
	 * @param reportname
	 * @param type
	 * @param isReLoad
	 */
	public ImageFramgent(String healthCheckNumber, String date,
			String reportname, int type, boolean isReLoad) {
		this.date = date;
		this.reportname = reportname;
		this.healthCheckNumber = healthCheckNumber;
		this.type = type;
		this.isReLoad = isReLoad;
	}

	/**
	 * 健康报告
	 * 
	 * @param fileUrl
	 * @param customerId
	 * @param date
	 * @param reportname
	 * @param type
	 * @param isReLoad
	 */
	public ImageFramgent(String fileUrl, String customerId, String date,
			String reportname, int type, boolean isReLoad) {
		this.fileUrl = fileUrl;
		this.customerId = customerId;
		this.date = date;
		this.reportname = reportname;
		this.type = type;
		this.isReLoad = isReLoad;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.photo_show, null);
		ViewUtils.inject(this, view);

		if (healthCheckNumber != null && !healthCheckNumber.equals("")) {// 体检报告
			getReportData();
		} else if (fileUrl != null && !fileUrl.equals("")) {// 健康报告
			fileName = fileUrl.substring(fileUrl.length() - 19,
					fileUrl.length() - 4);
			getReportPgData();
		} else {
			setData();
		}

		((ImageShowActivity) getActivity()).btn_left
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (healthCheckNumber != null
								&& !healthCheckNumber.equals("")) {// 体检报告
							if (!new File(Environment
									.getExternalStorageDirectory()
									+ "/ncihealth/" + healthCheckNumber)
									.exists()) {// 文件夹不存在
								commonDialog2 = new CommonDialog(context, 2,
										"停止下载", "继续下载", new OnClickListener() {

											@Override
											public void onClick(View v) {
												commonDialog2.dismiss();
												if (new File(
														Environment
																.getExternalStorageDirectory()
																+ "/ncihealth/"
																+ healthCheckNumber)
														.exists()) {
												} else {// 文件夹不存在,取消下载
													DownloadFileEngin.cancel();
												}
												// 返回报告列表
												Intent intent = new Intent(
														context,
														HealthReportActivity.class);
												intent.putExtra("TYPE", "0");
												startActivity(intent);
												getActivity().finish();
											}
										}, new OnClickListener() {

											@Override
											public void onClick(View v) {
												commonDialog2.dismiss();
											}
										}, null, "是否取消下载;");
								commonDialog2.show();
							} else {// 返回报告列表
								Intent intent = new Intent(context,
										HealthReportActivity.class);
								intent.putExtra("TYPE", "0");
								startActivity(intent);
								getActivity().finish();
							}
						} else if (fileUrl != null && !fileUrl.equals("")) {// 健康报告
							if (!new File(Environment
									.getExternalStorageDirectory()
									+ "/ncihealth/" + fileName).exists()) {

								commonDialog2 = new CommonDialog(context, 2,
										"停止下载", "继续下载", new OnClickListener() {

											@Override
											public void onClick(View v) {
												commonDialog2.dismiss();
												if (new File(
														Environment
																.getExternalStorageDirectory()
																+ "/ncihealth/"
																+ fileName)
														.exists()) {
												} else {
													DownloadFileEngin.cancel();
												}// 返回报告列表
												Intent intent = new Intent(
														context,
														HealthReportActivity.class);
												intent.putExtra("TYPE", "1");
												startActivity(intent);
												getActivity().finish();
											}
										}, new OnClickListener() {

											@Override
											public void onClick(View v) {
												commonDialog2.dismiss();
											}
										}, null, "是否取消下载;");
								commonDialog2.show();
							} else {// 返回报告列表
								Intent intent = new Intent(context,
										HealthReportActivity.class);
								intent.putExtra("TYPE", "1");
								startActivity(intent);
								getActivity().finish();
							}
						} else {
							getActivity().finish();
						}
					}
				});

		return view;
	}

	/**
	 * 下载体检报告
	 * 
	 * @author zhangyl
	 */
	private void getReportData() {
		// viewpager&页码隐藏,体检报告名称&日期&下载中提示&进度条显示
		viewPager.setVisibility(View.GONE);
		pageText.setVisibility(View.GONE);
		view_photosname.setVisibility(View.VISIBLE);
		view_photosdate.setVisibility(View.VISIBLE);
		view_photodownloading.setVisibility(View.VISIBLE);
		view_progress_bar.setVisibility(View.VISIBLE);
		view_photosname.setText(reportname);
		view_photosdate.setText(date);
		if (isReLoad) {// 重新下载
			final File file = new File(
					Environment.getExternalStorageDirectory() + "/ncihealth/"
							+ healthCheckNumber + ".zip");
			final File folder = new File(
					Environment.getExternalStorageDirectory() + "/ncihealth/"
							+ healthCheckNumber);
			// 移动网络状态下提示
			if (NetUtils.getNetworkType(context).equals("2G")
					|| NetUtils.getNetworkType(context).equals("3G")
					|| NetUtils.getNetworkType(context).equals("4G")) {
				commonDialog = new CommonDialog(context, 2, "停止下载", "继续下载",
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								commonDialog.dismiss();
								Intent intent = new Intent(context,
										HealthReportActivity.class);
								intent.putExtra("TYPE", "0");
								startActivity(intent);
								getActivity().finish();
							}
						}, new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								commonDialog.dismiss();
								CommonUtil.delete(file);
								CommonUtil.delete(folder);
								downloadReport();
							}
						}, null, "移动网络下载报告将消耗您的流量，是否下载？");
				commonDialog.show();
			} else if (NetUtils.isWifiConnection(context)) {
				// WIFI状态下直接下载
				CommonUtil.delete(file);
				CommonUtil.delete(folder);
				downloadReport();
			}
		} else {// 第一次下载
			if (new File(Environment.getExternalStorageDirectory()
					+ "/ncihealth/" + healthCheckNumber).exists()) {
				showHealthReport(healthCheckNumber, "0");
			} else if (new File(Environment.getExternalStorageDirectory()
					+ "/ncihealth/" + healthCheckNumber + ".zip").exists()) {
				unzipHealthReport(healthCheckNumber, "0");
			} else {
				// 移动网络状态下提示
				if (NetUtils.getNetworkType(context).equals("2G")
						|| NetUtils.getNetworkType(context).equals("3G")
						|| NetUtils.getNetworkType(context).equals("4G")) {
					commonDialog = new CommonDialog(context, 2, "停止下载", "继续下载",
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									commonDialog.dismiss();
									Intent intent = new Intent(context,
											HealthReportActivity.class);
									intent.putExtra("TYPE", "0");
									startActivity(intent);
									getActivity().finish();
								}
							}, new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									commonDialog.dismiss();
									downloadReport();
								}
							}, null, "移动网络下载报告将消耗您的流量，是否下载？");
					commonDialog.show();
				} else if (NetUtils.isWifiConnection(context)) {
					// WIFI状态下直接下载
					downloadReport();
				}
			}
		}
	}

	/**
	 * 下载健康报告
	 * 
	 */
	private void getReportPgData() {
		// viewpager&页码隐藏,体检报告名称&日期&下载中提示&进度条显示
		viewPager.setVisibility(View.GONE);
		pageText.setVisibility(View.GONE);
		view_photosname.setVisibility(View.VISIBLE);
		view_photosdate.setVisibility(View.VISIBLE);
		view_photodownloading.setVisibility(View.VISIBLE);
		view_progress_bar.setVisibility(View.VISIBLE);
		view_photosname.setText(reportname);
		view_photosdate.setText(date);
		if (isReLoad) {// 重新下载
			final File file = new File(
					Environment.getExternalStorageDirectory() + "/ncihealth/"
							+ fileName + ".zip");
			final File folder = new File(
					Environment.getExternalStorageDirectory() + "/ncihealth/"
							+ fileName);
			// 移动网络状态下提示
			if (NetUtils.getNetworkType(context).equals("2G")
					|| NetUtils.getNetworkType(context).equals("3G")
					|| NetUtils.getNetworkType(context).equals("4G")) {
				commonDialog = new CommonDialog(context, 2, "停止下载", "继续下载",
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								commonDialog.dismiss();
								Intent intent = new Intent(context,
										HealthReportActivity.class);
								intent.putExtra("TYPE", "1");
								startActivity(intent);
								getActivity().finish();
							}
						}, new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								commonDialog.dismiss();
								CommonUtil.delete(file);
								CommonUtil.delete(folder);
								downloadReportPg();
							}
						}, null, "移动网络下载报告将消耗您的流量，是否下载？");
				commonDialog.show();
			} else if (NetUtils.isWifiConnection(context)) {
				// WIFI状态下直接下载
				CommonUtil.delete(file);
				CommonUtil.delete(folder);
				downloadReportPg();
			}
		} else {// 第一次下载
			if (new File(Environment.getExternalStorageDirectory()
					+ "/ncihealth/" + fileName).exists()) {
				showHealthReport(fileName, "1");
			} else if (new File(Environment.getExternalStorageDirectory()
					+ "/ncihealth/" + fileName + ".zip").exists()) {
				unzipHealthReport(fileName, "1");
			} else {
				// 移动网络状态下提示
				if (NetUtils.getNetworkType(context).equals("2G")
						|| NetUtils.getNetworkType(context).equals("3G")
						|| NetUtils.getNetworkType(context).equals("4G")) {
					commonDialog = new CommonDialog(context, 2, "停止下载", "继续下载",
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									commonDialog.dismiss();
									Intent intent = new Intent(context,
											HealthReportActivity.class);
									intent.putExtra("TYPE", "1");
									startActivity(intent);
									getActivity().finish();
								}
							}, new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									commonDialog.dismiss();
									downloadReportPg();
								}
							}, null, "移动网络下载报告将消耗您的流量，是否下载？");
					commonDialog.show();
				} else if (NetUtils.isWifiConnection(context)) {
					// WIFI状态下直接下载
					downloadReportPg();
				}
			}
		}
	}

	/**
	 * 已有报告压缩包,解压
	 * 
	 * @author zhangyl
	 * @param zipfilename
	 * @param type
	 *            0：体检报告 1：健康报告
	 */
	private void unzipHealthReport(String zipfilename, String type) {
		CommonUtil.unzipFile(Environment.getExternalStorageDirectory()
				+ "/ncihealth/" + zipfilename + ".zip",
				Environment.getExternalStorageDirectory() + "/ncihealth/"
						+ zipfilename + "/");
		if (type.equals("0")) {
			showHealthReport(zipfilename, "0");
		} else if (type.equals("1")) {
			showHealthReport(zipfilename, "1");
		}

	}

	/**
	 * 已有该报告文件夹
	 * 
	 * @author zhangyl
	 * @param zipfilename
	 * @param type
	 *            0：体检报告 1：健康报告
	 */
	private void showHealthReport(String filesname, String type) {
		File allfile = new File(Environment.getExternalStorageDirectory()
				+ "/ncihealth/" + filesname);
		int filenum = allfile.list().length;
		paths.clear();
		if (type.equals("0")) {
			for (int i = 1; i <= filenum; i++) {
				paths.add(Environment.getExternalStorageDirectory()
						+ "/ncihealth/" + filesname + "/" + filesname + "_" + i
						+ ".png");
			}
		} else if (type.equals("1")) {
			for (int i = 1; i <= filenum; i++) {
				paths.add(Environment.getExternalStorageDirectory()
						+ "/ncihealth/" + filesname + "/" + filesname + "_" + i
						+ ".jpg");
			}
		}

		currentPosition = 0;
		setData();
	}

	/**
	 * 展示图片
	 * 
	 * @author zhangyl
	 */
	private void setData() {
		view_progress_bar.setVisibility(View.GONE);
		view_photodownloading.setVisibility(View.GONE);
		viewPager.setVisibility(View.VISIBLE);
		pageText.setVisibility(View.VISIBLE);
		pageText.setText((currentPosition + 1) + "/" + paths.size() + "页");
		adapter = new ViewPagerAdapter();
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(currentPosition);
		viewPager.setOnPageChangeListener(this);
		viewPager.setEnabled(false);
	}

	/**
	 * 下载体检报告压缩包
	 */
	private void downloadReport() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("type", "android");
		params.addBodyParameter("healthCheckNumber", healthCheckNumber);
		DownloadFileEngin.downloadReportFile(context,
				Environment.getExternalStorageDirectory() + "/ncihealth/"
						+ healthCheckNumber + ".zip", params,
				new RequestCallBack<File>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						commonDialog3 = new CommonDialog(context, 1, "返回", "",
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										commonDialog3.dismiss();
										Intent intent = new Intent(context,
												HealthReportActivity.class);
										intent.putExtra("TYPE", "0");
										startActivity(intent);
										getActivity().finish();
									}
								}, null, null, "下载失败");
						commonDialog3.show();
					}

					@Override
					public void onSuccess(ResponseInfo<File> arg0) {
						File folder = new File(Environment
								.getExternalStorageDirectory()
								+ "/ncihealth/"
								+ healthCheckNumber);
						CommonUtil.delete(folder);
						unzipHealthReport(healthCheckNumber, "0");
					}

					public void onLoading(long total, long current,
							boolean isUploading) {
						if (total != 0) {
							int progress = (int) ((double) current
									/ (double) total * 100);
							view_progress_bar.setProgress(progress);
						}
					};

					public void onCancelled() {
						File file = new File(Environment
								.getExternalStorageDirectory()
								+ "/ncihealth/"
								+ healthCheckNumber + ".zip");
						CommonUtil.delete(file);
					};

				});
	}

	/**
	 * 下载健康报告压缩包
	 */
	private void downloadReportPg() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("type", "android");
		params.addBodyParameter("fileUrl", fileUrl);
		params.addBodyParameter("customerId", customerId);
		final String fileName = fileUrl.substring(fileUrl.length() - 19,
				fileUrl.length() - 4);
		DownloadFileEngin.downloadReportPgFile(context,
				Environment.getExternalStorageDirectory() + "/ncihealth/"
						+ fileName + ".zip", params,
				new RequestCallBack<File>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						commonDialog3 = new CommonDialog(context, 1, "返回", "",
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										commonDialog3.dismiss();
										Intent intent = new Intent(context,
												HealthReportActivity.class);
										intent.putExtra("TYPE", "1");
										startActivity(intent);
										getActivity().finish();
									}
								}, null, null, "下载失败");
						commonDialog3.show();
					}

					@Override
					public void onSuccess(ResponseInfo<File> arg0) {
						File folder = new File(Environment
								.getExternalStorageDirectory()
								+ "/ncihealth/"
								+ fileName);
						CommonUtil.delete(folder);
						unzipHealthReport(fileName, "1");
					}

					public void onLoading(long total, long current,
							boolean isUploading) {
						if (total != 0) {
							int progress = (int) ((double) current
									/ (double) total * 100);
							view_progress_bar.setProgress(progress);
						}
					};

					public void onCancelled() {
						File file = new File(Environment
								.getExternalStorageDirectory()
								+ "/ncihealth/"
								+ fileName + ".zip");
						CommonUtil.delete(file);
					};

				});
	}

	/**
	 * ViewPager的适配器
	 * 
	 * @author guolin
	 */
	class ViewPagerAdapter extends PagerAdapter {
		private BitmapUtils bitmapUtils = new BitmapUtils(context);

		ViewPagerAdapter() {
			BitmapDisplayConfig bitmapDisplayConfig = new BitmapDisplayConfig();
			bitmapDisplayConfig.setLoadFailedDrawable(context.getResources()
					.getDrawable(R.mipmap.defalt_pic));
			bitmapUtils.configDefaultDisplayConfig(bitmapDisplayConfig);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Bitmap bitmap = null;
			View view = LayoutInflater.from(context).inflate(
					R.layout.zoom_image_layout, null);
			final PhotoView zoomImageView = (PhotoView) view
					.findViewById(R.id.zoom_image_view);
			if (type == 1) {
				bitmapUtils.display(zoomImageView, paths.get(position));
			} else if (type == 2) {
				bitmapUtils.display(zoomImageView,
						Environment.getExternalStorageDirectory()
								+ "/ncihealth/Image/" + paths.get(position));
			} else if (type == 3) {
				bitmapUtils.display(zoomImageView, paths.get(position));
			}
			container.addView(view);
			return view;
		}

		@Override
		public int getCount() {
			return paths == null ? 0 : paths.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View view = (View) object;
			container.removeView(view);
		}

	}

	/**
	 * 解析本地图片
	 * 
	 * @param imgFile
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	public static Bitmap getLocalBitmap(String imgFile) {
		if (imgFile == null || imgFile.length() == 0)
			return null;

		try {
			FileDescriptor fd = new FileInputStream(imgFile).getFD();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			// BitmapFactory.decodeFile(imgFile, options);
			BitmapFactory.decodeFileDescriptor(fd, null, options);
			options.inSampleSize = 1;
			options.inJustDecodeBounds = false;
			Bitmap bmp = BitmapFactory.decodeFile(imgFile, options);
			return bmp == null ? null : bmp;
		} catch (OutOfMemoryError e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int currentPage) {
		// TODO Auto-generated method stub
		pageText.setText((currentPage + 1) + "/" + paths.size() + "页");
	}

}
