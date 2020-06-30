package com.jinsit.kmec.comm;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.UnderLineString;
import com.jinsit.kmec.widget.JActionbar;


/**
 * 그리드뷰를 이용한 달력 예제
 *
 * @blog http://croute.me
 * @link http://croute.me/335
 *
 * @author croute
 * @since 2011.03.08
 */
public class CalendarGridNoDataActivity extends Activity implements OnItemClickListener, OnClickListener
{
	public static int SUNDAY 		= 1;
	public static int MONDAY 		= 2;
	public static int TUESDAY 		= 3;
	public static int WEDNSESDAY 	= 4;
	public static int THURSDAY 		= 5;
	public static int FRIDAY 		= 6;
	public static int SATURDAY 		= 7;

	private TextView mTvCalendarTitle;
	private GridView mGvCalendar;

	private ArrayList<DayInfoNoData> mDayList;
	private CalendarGridNoDataAdapter mCalendarAdapter;

	Calendar mLastMonthCalendar;
	Calendar mThisMonthCalendar;
	Calendar mNextMonthCalendar;


	int reserveYear ;
	int reserveMonth;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */

	Context context;
	String workMm;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_grid);
		context = this;
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("달력");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		Button btn_lastMonth = (Button)findViewById(R.id.gv_calendar_activity_b_last);
		Button btn_nextMonth = (Button)findViewById(R.id.gv_calendar_activity_b_next);

		mTvCalendarTitle = (TextView)findViewById(R.id.gv_calendar_activity_tv_title);
		mTvCalendarTitle.setOnClickListener(this);
		mGvCalendar = (GridView)findViewById(R.id.gv_calendar_activity_gv_calendar);


		btn_lastMonth.setOnClickListener(this);
		btn_nextMonth.setOnClickListener(this);
		mGvCalendar.setOnItemClickListener(this);

		mDayList = new ArrayList<DayInfoNoData>();


	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume()
	{
		super.onResume();

		// 이번달 의 캘린더 인스턴스를 생성한다.
		mThisMonthCalendar = Calendar.getInstance();
		mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
		getCalendar(mThisMonthCalendar);
	}

	/**
	 * 달력을 셋팅한다.
	 *
	 * @param calendar 달력에 보여지는 이번달의 Calendar 객체
	 */
	private void getCalendar(Calendar calendar)
	{
		int lastMonthStartDay;
		int dayOfMonth;
		int thisMonthLastDay;

		mDayList.clear();

		// 이번달 시작일의 요일을 구한다. 시작일이 일요일인 경우 인덱스를 1(일요일)에서 8(다음주 일요일)로 바꾼다.)
		dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
		thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		calendar.add(Calendar.MONTH, -1);
		Log.e("지난달 마지막일", calendar.get(Calendar.DAY_OF_MONTH)+"");

		// 지난달의 마지막 일자를 구한다.
		lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		calendar.add(Calendar.MONTH, 1);
		Log.e("이번달 시작일", calendar.get(Calendar.DAY_OF_MONTH)+"");

		if(dayOfMonth == SUNDAY)
		{
			dayOfMonth += 7;
		}

		lastMonthStartDay -= (dayOfMonth-1)-1;


		// 캘린더 타이틀(년월 표시)을 세팅한다.
//		mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
//				+ (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
		workMm = (DateUtil.getFormat(2).format(calendar.getTime()));
		mTvCalendarTitle.setText(UnderLineString.getUnderLineString(DateUtil.getFormat(2).format(calendar.getTime())));
		DayInfoNoData day;

		Log.e("DayOfMOnth", dayOfMonth+"");

		for(int i=0; i<dayOfMonth-1; i++)
		{
			int date = lastMonthStartDay+i;
			day = new DayInfoNoData();
			day.setDay(Integer.toString(date));
			day.setInMonth(false);

			mDayList.add(day);
		}
		for(int i=1; i <= thisMonthLastDay; i++)
		{
			day = new DayInfoNoData();
			day.setDay(Integer.toString(i));
			day.setInMonth(true);

			mDayList.add(day);
		}
		for(int i=1; i<42-(thisMonthLastDay+dayOfMonth-1)+1; i++)
		{
			day = new DayInfoNoData();
			day.setDay(Integer.toString(i));
			day.setInMonth(false);
			mDayList.add(day);
		}

		initCalendarAdapter();

	}

	/**
	 * 지난달의 Calendar 객체를 반환합니다.
	 *
	 * @param calendar
	 * @return LastMonthCalendar
	 */
	private Calendar getLastMonth(Calendar calendar)
	{
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
		calendar.add(Calendar.MONTH, -1);
		mTvCalendarTitle.setText(UnderLineString.getUnderLineString(DateUtil.getFormat(2).format(calendar.getTime())));
		return calendar;
	}

	/**
	 * 다음달의 Calendar 객체를 반환합니다.
	 *
	 * @param calendar
	 * @return NextMonthCalendar
	 */
	private Calendar getNextMonth(Calendar calendar)
	{
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
		calendar.add(Calendar.MONTH, +1);
		mTvCalendarTitle.setText(UnderLineString.getUnderLineString(DateUtil.getFormat(2).format(calendar.getTime())));
		return calendar;
	}

	private String getNowMonth(Calendar calendar)
	{
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);

		return (DateUtil.getFormat(2).format(calendar.getTime()));
	}


	/**
	 * 그리드뷰의 날짜를 클릭했을 때 선택한 날짜를 반환합니다.
	 * 달력을 호출하는 부분에서 onActivityResult로 받을 수 있습니다.
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long arg3)
	{
		returnDate(position, mDayList.get(position).isInMonth());
	}


	private void returnDate(int position, boolean isMonth) {
		String day = mDayList.get(position).getDay();
		Calendar cal = mThisMonthCalendar;
		if (isMonth) {
			Log.w("retStr", "isMonth");
		} else {
			if (position < 15) {
				cal.add(Calendar.MONTH, -1);
				Log.v("retStr", "not isMonth <15");
			} else {
				cal.add(Calendar.MONTH, +1);
				Log.e("retStr", "not isMonth >15");
			}
		}
		cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(day));
		// add를 먼저하고 셋을 나중에 한다.
		// 안그러니 2월달에서 에러가 발생했다.
		String retStr = DateUtil.getFormat(0).format(cal.getTime());
		Log.e("retStr", "retStr" + retStr);
		Bundle b = new Bundle();
		b.putString("dateSelected", retStr);
		// Add the set of extended data to the intent and start it
		Intent intent = new Intent();
		intent.putExtras(b);
		setResult(RESULT_OK, intent);
		finish();
	}
	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.gv_calendar_activity_b_last:
				mThisMonthCalendar = getLastMonth(mThisMonthCalendar);
				getCalendar(mThisMonthCalendar);
				break;
			case R.id.gv_calendar_activity_b_next:
				mThisMonthCalendar = getNextMonth(mThisMonthCalendar);
				getCalendar(mThisMonthCalendar);
				break;
			case R.id.gv_calendar_activity_tv_title:

				String nowMonth = getNowMonth(mThisMonthCalendar);
				reserveYear = DateUtil.getYear(nowMonth);
				reserveMonth = DateUtil.getMonth(nowMonth);
				DatePicker01 datePicker = new DatePicker01(context, new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
										  int dayOfMonth) {
						// TODO Auto-generated method stub
						mThisMonthCalendar.set(Calendar.YEAR, year);
						mThisMonthCalendar.set(Calendar.MONTH, monthOfYear);
						getCalendar(mThisMonthCalendar);
					}
				}, reserveYear, reserveMonth, 0,1);
				datePicker.show();

				break;

		}
	}
	private void initCalendarAdapter()
	{
		mCalendarAdapter = new CalendarGridNoDataAdapter(this, R.layout.griditem_dayinfo_nodata, mDayList);
		mGvCalendar.setAdapter(mCalendarAdapter);
	}

}