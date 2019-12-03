package com.pact.healthapp.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pact.healthapp.R;
import com.pact.healthapp.view.wheelview.OnWheelScrollListener;
import com.pact.healthapp.view.wheelview.WheelView;
import com.pact.healthapp.view.wheelview.adapter.NumericWheelAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatePickerPopWindow extends PopupWindow implements OnWheelScrollListener,OnClickListener{
	private Context context;
	private String startTime = "19000101";
	private int curYear,curMonth;
	private LayoutInflater mInflater;
	private View dateView;
	public TextView btnCancel;
	public TextView btnSubmit;
	private WheelView wheelView;
	private WheelView yearView;
	private WheelView monthView;
	private WheelView dayView;
	private OnTimeSelectListener timeSelectListener;
	private int[] timeInt;
	String choosedTime;
	public DatePickerPopWindow(Context context){
		this.context=context;
		setStartTime();
		initWindow();
	}
	private void setStartTime() {
		// TODO Auto-generated method stub
		timeInt=new int[3];
		timeInt[0]=Integer.valueOf(startTime.substring(0, 4));
		timeInt[1]=Integer.valueOf(startTime.substring(4, 6));
		timeInt[2]=Integer.valueOf(startTime.substring(6, 8));
		curYear = timeInt[0];
		curMonth = timeInt[1];
	}
	private void initWindow() {
		// TODO Auto-generated method stub
		mInflater=LayoutInflater.from(context);
		dateView=mInflater.inflate(R.layout.wheel_date_picker, null);
		btnCancel=(TextView) dateView.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);
		btnSubmit=(TextView) dateView.findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(this);
		yearView=(WheelView) dateView.findViewById(R.id.year);
		monthView=(WheelView) dateView.findViewById(R.id.month);
		dayView=(WheelView) dateView.findViewById(R.id.day);
		initWheel();
	}
	private void initWheel() {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy");  
		String year=sdf.format(new Date()); 
		
		NumericWheelAdapter numericWheelAdapter1=new NumericWheelAdapter(context,curYear, Integer.parseInt(year)); 
		numericWheelAdapter1.setLabel("年");
		yearView.setViewAdapter(numericWheelAdapter1);
		yearView.setCyclic(true);
		yearView.addScrollingListener(this);
		
		NumericWheelAdapter numericWheelAdapter2=new NumericWheelAdapter(context,1, 12, "%02d"); 
		numericWheelAdapter2.setLabel("月");
		monthView.setViewAdapter(numericWheelAdapter2);
		monthView.setCyclic(true);
		monthView.addScrollingListener(this);
		
		NumericWheelAdapter numericWheelAdapter3=new NumericWheelAdapter(context,1, getDay(curYear, curMonth), "%02d");
		numericWheelAdapter3.setLabel("日");
		dayView.setViewAdapter(numericWheelAdapter3);
		dayView.setCyclic(true);
		dayView.addScrollingListener(this);
		
		yearView.setCurrentItem(timeInt[0]-curYear);
		monthView.setCurrentItem(timeInt[1]-1);
		dayView.setCurrentItem(timeInt[2]-1);
		yearView.setVisibleItems(7);//������ʾ����
		monthView.setVisibleItems(7);
		dayView.setVisibleItems(7);
		setContentView(dateView);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		ColorDrawable dw = new ColorDrawable(0xFFFFFFFF);
		setBackgroundDrawable(dw);
		setFocusable(true);
	}
	private void initDay(int arg1, int arg2) {
		NumericWheelAdapter numericWheelAdapter=new NumericWheelAdapter(context,1, getDay(arg1, arg2), "%02d");
		numericWheelAdapter.setLabel("日");
		dayView.setViewAdapter(numericWheelAdapter);
	}
	private int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}
	@Override
	public void onScrollingStarted(WheelView wheel) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onScrollingFinished(WheelView wheel) {
		// TODO Auto-generated method stub
		int n_year = yearView.getCurrentItem() + curYear;//��
		int n_month = monthView.getCurrentItem() + 1;//��
		initDay(n_year,n_month);
		choosedTime=new StringBuilder().append((yearView.getCurrentItem()+curYear)).append("年").append((monthView.getCurrentItem() + 1) < 10 ? "0" + (monthView.getCurrentItem() + 1) : (monthView.getCurrentItem() + 1)).append("月").append(((dayView.getCurrentItem()+1) < 10) ? "0" + (dayView.getCurrentItem()+1) : (dayView.getCurrentItem()+1)).append("日").toString();
//		Toast.makeText(context, choosedTime, Toast.LENGTH_SHORT).show();
		timeSelectListener.onTimeSelect(choosedTime);
	}
	public interface OnTimeSelectListener {
		public void onTimeSelect(String time);
	}
	public void setOnTimeSelectListener(OnTimeSelectListener timeSelectListener) {
		this.timeSelectListener = timeSelectListener;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnSubmit:
			onScrollingFinished(wheelView);
			dismiss();
			break;
		default:
			break;
		}
	}
	public void setCurrentTime(String time){
		if (!time.equals("设置出生日期")) {
			yearView.setCurrentItem(Integer.valueOf(time.substring(0, 4))-1900);
			monthView.setCurrentItem(Integer.valueOf(time.substring(5, 7))-1);
			dayView.setCurrentItem(Integer.valueOf(time.substring(8, 10))-1);
		}
	}
}
