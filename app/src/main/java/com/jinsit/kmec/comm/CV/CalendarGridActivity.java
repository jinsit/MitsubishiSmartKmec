package com.jinsit.kmec.comm.CV;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;


/**
 * 그리드뷰를 이용한 달력 예제
 *
 * @blog http://croute.me
 * @link http://croute.me/335
 *
 * @author croute
 * @since 2011.03.08
 *
 * @customize 달력을 셋팅하고 mitsubishi 월 데이터 표시
 *
 */
public class CalendarGridActivity extends Activity implements OnItemClickListener, OnClickListener
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

	private ArrayList<DayInfo> mDayList;
	private CalendarGridAdapter mCalendarAdapter;

	Calendar mLastMonthCalendar;
	Calendar mThisMonthCalendar;
	Calendar mNextMonthCalendar;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	private ArrayList<InspectionPlanHistoryData> iphData;
	Context context;
	GetHttp getHttp;
	private EasyJsonList ejl;
	private ProgressDialog ProgressDialog;
	String workMm;
	private CommonSession cs;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_grid);
		context = this;
		cs = new CommonSession(context);
		Button btn_lastMonth = (Button)findViewById(R.id.gv_calendar_activity_b_last);
		Button btn_nextMonth = (Button)findViewById(R.id.gv_calendar_activity_b_next);

		mTvCalendarTitle = (TextView)findViewById(R.id.gv_calendar_activity_tv_title);
		mGvCalendar = (GridView)findViewById(R.id.gv_calendar_activity_gv_calendar);


		btn_lastMonth.setOnClickListener(this);
		btn_nextMonth.setOnClickListener(this);
		mGvCalendar.setOnItemClickListener(this);

		mDayList = new ArrayList<DayInfo>();


	}

	public class InspectionPlanHistoryAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			CalendarGridActivity.this.ProgressDialog = android.app.ProgressDialog.show(
					context, "조회중", "작업내역불러오는중");
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				iphData = new ArrayList<InspectionPlanHistoryData>();
				/*ApiService pgApiService = new ApiService();
				IR_CD00_R00ReqData req = new IR_CD00_R00ReqData(SEL_TP, deptCode,recevDtFr,
						recevDtTo,ORDER_CD,CHK_ORDER);

				plResData = pgApiService.passengerLockSearch(req);*/
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"comm/selectInspectionPlanHistory.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("csEmpId", cs.getEmpId()));
				arguments.add(new BasicNameValuePair("workMm", workMm));
				JSONObject returnJson = getHttp.getPost(param_url, arguments, true);

				try {
					ejl = new EasyJsonList( returnJson.getJSONArray("dataList") );

					iphData.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						iphData.add(new InspectionPlanHistoryData(ejl.getValue(i,
								"EMP_ID"), ejl.getValue(i, "WORK_DT"), ejl
								.getValue(i, "WORK_DAY"), ejl.getValue(i,
								"WEEKDAY"), ejl.getValue(i, "DAY_NM1"), ejl
								.getValue(i, "DAY_NM2"), ejl.getValue(i,
								"DAY_NM3"), ejl.getValue(i, "D_CNT"), ejl
								.getValue(i, "N_CNT")));

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}


			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			CalendarGridActivity.this.ProgressDialog.dismiss();
//			mCalendarAdapter = new CalendarGridAdapter(context, asResData);
//			mGvCalendar.setAdapter(asListAdapter);
			initCalendarAdapter();
		}
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
		mTvCalendarTitle.setText(DateUtil.getFormat(2).format(calendar.getTime()));
		DayInfo day;

		Log.e("DayOfMOnth", dayOfMonth+"");

		for(int i=0; i<dayOfMonth-1; i++)
		{
			int date = lastMonthStartDay+i;
			day = new DayInfo();
			day.setDay(Integer.toString(date));
			day.setInMonth(false);

			mDayList.add(day);
		}
		for(int i=1; i <= thisMonthLastDay; i++)
		{
			day = new DayInfo();
			day.setDay(Integer.toString(i));
			day.setInMonth(true);

			mDayList.add(day);
		}
		for(int i=1; i<42-(thisMonthLastDay+dayOfMonth-1)+1; i++)
		{
			day = new DayInfo();
			day.setDay(Integer.toString(i));
			day.setInMonth(false);
			mDayList.add(day);
		}

		//initCalendarAdapter();
		new InspectionPlanHistoryAsync().execute();
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
//		mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
//				+ (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
		mTvCalendarTitle.setText(DateUtil.getFormat(2).format(calendar.getTime()));
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
//		mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
//				+ (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
		mTvCalendarTitle.setText(DateUtil.getFormat(2).format(calendar.getTime()));
		return calendar;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long arg3)
	{
		///클릭리스너 구현하세요
		if(mDayList.get(position).isInMonth()){
			//이번달일때만
			String day = mDayList.get(position).getDay();
			Calendar cal = mThisMonthCalendar;
			cal.set(Calendar.DAY_OF_MONTH,Integer.valueOf(day));
			String retStr = DateUtil.getFormat(0).format(cal.getTime());
			Log.e("retStr", "Ismonth");
			Toast.makeText(context, retStr, Toast.LENGTH_SHORT).show();

		}else{
			//이전 달이거나 다음달 이번달 선택하라는 토스트 뿌리기

			if(position<15){
				String day = mDayList.get(position).getDay();
				Calendar cal = mThisMonthCalendar;
				cal.add(Calendar.MONTH, -1);
				cal.set(Calendar.DAY_OF_MONTH,Integer.valueOf(day));
				String retStr = DateUtil.getFormat(0).format(cal.getTime());
				Log.v("retStr", "retStr" + "not Ismonth <15");
				Toast.makeText(context, retStr, Toast.LENGTH_SHORT).show();
				mThisMonthCalendar = getNextMonth(mThisMonthCalendar);//초기화 오늘 월
			}else{
				String day = mDayList.get(position).getDay();
				Calendar cal = mThisMonthCalendar;
				cal.add(Calendar.MONTH, +1);
				cal.set(Calendar.DAY_OF_MONTH,Integer.valueOf(day));
				String retStr = DateUtil.getFormat(0).format(cal.getTime());
				Log.w("retStr", "retStr" +  "not Ismonth >15");
				Toast.makeText(context, retStr, Toast.LENGTH_SHORT).show();
				mThisMonthCalendar = getLastMonth(mThisMonthCalendar);//초기화
			}

		}

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
		}
	}
	private void initCalendarAdapter()
	{
		mCalendarAdapter = new CalendarGridAdapter(this, R.layout.griditem_dayinfo, mDayList,iphData);
		mGvCalendar.setAdapter(mCalendarAdapter);
	}
//	private void initCalendarAdapter()
//	{
//		mCalendarAdapter = new CalendarGridAdapter(this, R.layout.griditem_dayinfo, mDayList);
//		mGvCalendar.setAdapter(mCalendarAdapter);
//	}
}