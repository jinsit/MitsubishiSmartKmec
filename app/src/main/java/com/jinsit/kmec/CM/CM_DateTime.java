package com.jinsit.kmec.CM;


import com.jinsit.kmec.R;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class CM_DateTime extends Dialog implements OnClickListener,OnDismissListener {

	public CM_DateTime(Context context, String title, int year, int month, int day, int hour, int minute ) {
		super(context);
		// TODO Auto-generated constructor stub

		this.context = context;
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.title = title;
	}

	private String title;
	
	private DatePicker dp_datePicker;
	private TimePicker tp_timePicker;
	private Button btn_cm_ok;

	private TextView tv01_popTitle;
	private TextView btn_popClose;
	
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cm_datetime);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		tv01_popTitle.setText(title);
		dp_datePicker.init(year, month-1, day, null);
	
		tp_timePicker.setCurrentHour(hour);
		tp_timePicker.setCurrentMinute(minute);
		
	}

	protected void getInstances() {
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		tv01_popTitle = (TextView)findViewById(R.id.tv01_popTitle);
		
		dp_datePicker = (DatePicker)findViewById(R.id.dp_datePicker);
		tp_timePicker = (TimePicker)findViewById(R.id.tp_timePicker);
		btn_cm_ok = (Button) findViewById(R.id.btn_cm_ok);
		
		setEvents();
	}

	protected void setEvents() {
		btn_cm_ok.setOnClickListener(this);
		btn_popClose.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_cm_ok:
			year = dp_datePicker.getYear();
			month = dp_datePicker.getMonth()+1;
			day = dp_datePicker.getDayOfMonth();
			hour = tp_timePicker.getCurrentHour();
			minute = tp_timePicker.getCurrentMinute();
			dateTimeString = String.format("%02d-%02d-%02d", year, month, day) +" "+ String.format("%02d:%02d", hour, minute);
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
