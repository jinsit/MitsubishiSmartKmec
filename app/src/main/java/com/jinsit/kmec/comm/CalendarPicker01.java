package com.jinsit.kmec.comm;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.DateUtil;

public class CalendarPicker01 extends Activity {
	private int mYear;
	private int mMonth;
	private int mDay;
	CalendarView calendar;
	Long date;;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);
		
		 calendar = (CalendarView) findViewById(R.id.cdv_calendar);
		 calendar.setShowWeekNumber(false);
		date = calendar.getDate();
		calendar.setOnDateChangeListener(mDateSetListener);
		
	}

	private CalendarView.OnDateChangeListener mDateSetListener = new CalendarView.OnDateChangeListener() {

		public void onSelectedDayChange(CalendarView view, int year,
				int monthOfYear, int dayOfMonth) {
			if(calendar.getDate()!=date){
				
				date = calendar.getDate();
				mYear = year;
				mMonth = monthOfYear;
				mDay = dayOfMonth;
				Calendar c = Calendar.getInstance();
				c.set(year, monthOfYear, dayOfMonth);
				String retSTr = DateUtil.getFormat(0).format(c.getTime());

				Bundle b = new Bundle();
				b.putString("dateSelected", retSTr);

				// Add the set of extended data to the intent and start it
				Intent intent = new Intent();
				intent.putExtras(b);
				setResult(RESULT_OK, intent);
				finish();
			}
			
		}
	};
	
}
