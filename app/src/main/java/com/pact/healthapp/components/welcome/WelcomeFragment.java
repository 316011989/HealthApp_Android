package com.pact.healthapp.components.welcome;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.data.Constants;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.universal.FrameworkManager;
import com.pact.healthapp.utils.CommonUtil;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WelcomeFragment extends BaseFragment {
    @ViewInject(R.id.welcome_bt_jump)
    private Button jump;
    @ViewInject(R.id.jumpButton)
    private Button jumpButton;// 跳过按钮
    @ViewInject(R.id.vp)
    private ViewPager viewPager;

    private List<ViewHolder> imageViews = null;
    private AlphaAnimation alphaAnimation;
    private TranslateAnimation alphaAnimation1;
    private TranslateAnimation alphaAnimation11;
    private TranslateAnimation alphaAnimation2;
    private TranslateAnimation alphaAnimation22;
    private boolean left = false;
    private boolean right = false;
    private boolean isScrolling = false;
    private int lastValue = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.login_welcome_layout, null);
        ViewUtils.inject(this, view);
        init();
        return view;
    }

    /**
     *
     */
    private void init() {
        SharedPreferences sp = context.getSharedPreferences("userprefs",
                Context.MODE_PRIVATE);
        initView();
        jump.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                FrameworkManager.branch(getActivity(),
                        Constants.moduleList.get(0));
                getActivity().finish();
                // if (!scheduledExecutorService.isShutdown()) {
                // scheduledExecutorService.shutdown();
                // }
            }
        });

        jumpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                FrameworkManager.branch(getActivity(),
                        Constants.moduleList.get(0));
                getActivity().finish();
                // if (!scheduledExecutorService.isShutdown()) {
                // scheduledExecutorService.shutdown();
                // }
            }
        });
        viewPager.setAdapter(new MyAdapter());

        viewPager.setOnPageChangeListener(new MyPageChangeListener());

        // scheduledExecutorService =
        // Executors.newSingleThreadScheduledExecutor();
        sp.edit().putBoolean("hasRun", true).commit();
    }

    private void initView() {
        alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation1 = new TranslateAnimation(-1200, 0, 0.0f, 0.0f);
        alphaAnimation2 = new TranslateAnimation(-1200, 0, 0.0f, 0.0f);
        alphaAnimation11 = new TranslateAnimation(1200, 0, 0.0f, 0.0f);
        alphaAnimation22 = new TranslateAnimation(1200, 0, 0.0f, 0.0f);
        // 中间大图
        HashMap<Integer, SoftReference<Bitmap>> imageCacheMid = new HashMap<Integer, SoftReference<Bitmap>>();
        imageCacheMid.put(0, getSoftBitmap(R.mipmap.framework_splash_1));
        imageCacheMid.put(1, getSoftBitmap(R.mipmap.framework_splash_2));
        imageCacheMid.put(2, getSoftBitmap(R.mipmap.framework_splash_3));
        imageViews = new ArrayList<ViewHolder>();// adapter 用的 list
        ViewHolder viewHolder;
        for (int i = 0; i < imageCacheMid.size(); i++) {
            viewHolder = new ViewHolder();
            viewHolder.view = LayoutInflater.from(context).inflate(
                    R.layout.framework_welcome_view, null);
            viewHolder.imageView = (ImageView) viewHolder.view
                    .findViewById(R.id.welcome_imageview);
            viewHolder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            if (imageCacheMid.get(i).get() != null) {
                viewHolder.imageView.setImageBitmap(imageCacheMid.get(i).get());
            } else {
                imageCacheMid.put(0,
                        getSoftBitmap2(R.mipmap.framework_splash_1));
                imageCacheMid.put(1,
                        getSoftBitmap2(R.mipmap.framework_splash_2));
                imageCacheMid.put(2,
                        getSoftBitmap2(R.mipmap.framework_splash_3));
                viewHolder.imageView.setImageBitmap(imageCacheMid.get(i).get());
            }
            // 中间大图
            if (i != 0) {
                viewHolder.imageView.getDrawable().setAlpha(10);
                // 初始化默认透明度255
            }
            if (i == 0) {
                // 初始大图化动画
                alphaAnimation.setDuration(500);
                viewHolder.imageView.startAnimation(alphaAnimation);
            }
            imageViews.add(viewHolder);
        }
    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup arg0, int arg1) {
            arg0.addView(imageViews.get(arg1).view);
            return imageViews.get(arg1).view;
        }

        @Override
        public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
            arg0.removeView(imageViews.get(arg1).view);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(ViewGroup arg0) {

        }

        @Override
        public void finishUpdate(ViewGroup arg0) {

        }
    }

    private class ViewHolder {
        private ImageView imageView;
        private View view;
    }

    private class MyPageChangeListener implements OnPageChangeListener {
        private int oldPosition = 0;

        /**
         * This method will be invoked when a new page becomes selected.
         * position: Position index of the new selected page.
         */
        @Override
        public void onPageSelected(int position) {
            // 点击进入
            if (position == imageViews.size() - 1) {
                jump.setVisibility(View.INVISIBLE);
                jumpButton.setVisibility(View.VISIBLE);
            } else {
                jump.setVisibility(View.VISIBLE);
                jumpButton.setVisibility(View.INVISIBLE);
            }
            imageViews.get(position).imageView.getDrawable().setAlpha(255);
            imageViews.get(oldPosition).imageView.getDrawable().setAlpha(10);

            oldPosition = position;

            // 从完全透明到不透明
            alphaAnimation.setDuration(500);
            imageViews.get(position).imageView.startAnimation(alphaAnimation);

            if (left == true) {
                alphaAnimation1.setDuration(500);
                alphaAnimation1.setInterpolator(context,
                        android.R.anim.accelerate_decelerate_interpolator);

                alphaAnimation1.setFillAfter(true);

                // 除第二张图
                if (position != 1) {
                    alphaAnimation2.setDuration(500);
                    alphaAnimation2.setStartOffset(600);
                    alphaAnimation2.setInterpolator(context,
                            android.R.anim.accelerate_decelerate_interpolator);
                    alphaAnimation2.setFillAfter(true);
                }

            } else if (right == true) {
                alphaAnimation11.setDuration(500);
                alphaAnimation11.setInterpolator(context,
                        android.R.anim.accelerate_decelerate_interpolator);
                alphaAnimation11.setFillAfter(true);
                // 除第二张图
                if (position != 1) {
                    alphaAnimation22.setDuration(500);
                    alphaAnimation22.setStartOffset(600);
                    alphaAnimation22.setInterpolator(context,
                            android.R.anim.accelerate_decelerate_interpolator);
                    alphaAnimation22.setFillAfter(true);

                }
                if (position == imageViews.size() - 1) {
                    FrameworkManager.branch(getActivity(),
                            Constants.moduleList.get(0));
                    getActivity().finish();
                }
            }

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

            if (arg0 == 1) {
                isScrolling = true;
            } else {
                isScrolling = false;
            }

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

            if (isScrolling) {
                if (lastValue > arg2) {
                    // 递减，向右滑动
                    right = true;
                    left = false;
                } else if (lastValue < arg2) {
                    // 递减，向右滑动
                    right = false;
                    left = true;
                } else if (lastValue == arg2) {
                    right = left = false;
                }
            }
            lastValue = arg2;

        }

    }

    private SoftReference<Bitmap> getSoftBitmap(int source) {
        Bitmap bitMap = CommonUtil.getBitMap(context, source);
        SoftReference<Bitmap> softBitmap = new SoftReference<Bitmap>(bitMap);
        return softBitmap;
    }

    private SoftReference<Bitmap> getSoftBitmap2(int source) {
        Bitmap bitMap = CommonUtil.getBitMap2(context, source);
        SoftReference<Bitmap> softBitmap = new SoftReference<Bitmap>(bitMap);
        return softBitmap;
    }

}
