package com.jinsit.kmec.CM;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.DateUtil;

public class CM_SDateTime extends Dialog implements OnClickListener,OnDismissListener {

	public CM_SDateTime(Context context, String title,  int hour, int minute ) {
		super(context);
		// TODO Auto-generated constructor stub

		this.context = context;

		String value = DateUtil.nowDateTime();
		
		year = DateUtil.getYear(value);
		month = DateUtil.getMonth(value);
		day = DateUtil.getDay(value);
		
		value = DateUtil.tomorrowDateTime();
		tomYear = DateUtil.getYear(value);
		tomMonth  = DateUtil.getMonth(value);
		tomDay = DateUtil.getDay(value);
		
		this.hour = hour;
		this.minute = minute;
		this.title = title;
	}

	private String title;
	
	private TimePicker tp_timePicker;
	
	private TextView tv_cm_firstDate;
	private TextView tv_cm_secondDate;
	

	private TextView tv01_popTitle;
	private TextView btn_popClose;
	
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cm_sdatetime);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		tv01_popTitle.setText(title);
	
		tv_cm_firstDate.setText(String.format("%02d-%02d-%02d", year, month, day));
		tv_cm_secondDate.setText(String.format("%02d-%02d-%02d", tomYear, tomMonth, tomDay));
		
		tp_timePicker.setCurrentHour(hour);
		tp_timePicker.setCurrentMinute(minute);
		this.setCancelable(false);
	}

	protected void getInstances() {
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		tv01_popTitle = (TextView)findViewById(R.id.tv01_popTitle);
		
		tp_timePicker = (TimePicker)findViewById(R.id.tp_timePicker);
		
		tv_cm_firstDate = (TextView) findViewById(R.id.tv_cm_firstDate);
		tv_cm_secondDate = (TextView) findViewById(R.id.tv_cm_secondDate);
		
		setEvents();
	}

	protected void setEvents() {
		tv_cm_firstDate.setOnClickListener(this);
		tv_cm_secondDate.setOnClickListener(this);
		btn_popClose.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_cm_firstDate:
			hour = tp_timePicker.getCurrentHour();
			minute = tp_timePicker.getCurrentMinute();
			dateTimeString = String.format("%02d-%02d-%02d", year, month, day) + " " + String.format("%02d:%02d", hour, minute);
			dismiss();
			break;
		case R.id.tv_cm_secondDate:
			hour = tp_timePicker.getCurrentHour();
			minute = tp_timePicker.getCurrentMinute();
			dateTimeString = String.format("%02d-%02d-%02d", tomYear, tomMonth, tomDay) + " " + String.format("%02d:%02d", hour, minute);
			dismiss();
			break;
		case R.id.btn_popClose:
			dateTimeString = "";
			dismiss();
			break;
		}
	}


	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		
	}
	
	private int hour;
	private int minute;
	private int year;
	private int month;
	private int day;
	
	private int tomYear;
	private int tomMonth;
	private int tomDay;
	
	private String dateTimeString;
	public String getDateTimeString(){
		return dateTimeString;
	}
	
	public int getHour(){
		return hour;
	}
	public int getMinute(){
		return minute;
	}public int getYear(){
		return year;
	}public int getMonth(){
		return month;
	}public int getDay(){
		return day;
	}


}
