package com.pact.healthapp.view.imageshow;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.pact.healthapp.R;
import com.pact.healthapp.components.community.HCallBack;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 加载图片工具类
 */
public class LoadingImgUtil {

	public static DisplayImageOptions DEFAULT_PIC_OPTIONS = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.mipmap.defalt_pic)
			.showImageForEmptyUri(R.mipmap.defalt_pic)
			.showImageOnFail(R.mipmap.defalt_pic).cacheInMemory(true)
			.cacheOnDisk(true).considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565).build();

	public static void displayAnimate(String url, ImageView imgview) {
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(url, imgview, DEFAULT_PIC_OPTIONS,
				new AnimateFirstDisplayListener(null));
	}

	public static void loadimgAnimate(String url, HCallBack callBack) {
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.loadImage(url, DEFAULT_PIC_OPTIONS,
				new AnimateFirstDisplayListener(callBack));
	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());
		HCallBack callBack;

		public AnimateFirstDisplayListener(HCallBack callBack) {
			// TODO Auto-generated constructor stub
			this.callBack = callBack;
		}

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {

				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (view != null) {
					ImageView imageView = (ImageView) view;
					if (firstDisplay) {
						FadeInBitmapDisplayer.animate(imageView, 500);
						displayedImages.add(imageUri);
					}
				}
				if (callBack != null) {
					callBack.callBack(loadedImage);
				}
			}
		}
	}
}
