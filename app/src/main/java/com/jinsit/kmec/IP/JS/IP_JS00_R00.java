package com.jinsit.kmec.IP.JS;

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
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.CalendarGridNoDataActivity;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.CV.CalendarGridAdapter;
import com.jinsit.kmec.comm.CV.DayInfo;
import com.jinsit.kmec.comm.CV.InspectionPlanHistoryData;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;

public class IP_JS00_R00 extends Activity {

	//uiInstances
	Context context;
	TextView et01_js00_YM_value;
	TextView btn_js_calendar;
	TextView btn02_js00_search;
	GridView gl01_js00_calender;

	//http
	private EasyJsonList ejl;
	private JSONObject returnJson01;
	private JSONObject returnJson02;

	//pop1
	private EasyJsonMap ejm01;
	private EasyJsonList ejl01;
	private EasyJsonList ejl02;
	private List<IP_JS00_R01P_Item> itemList01;
	private List<IP_JS00_R01P_Item> itemList02;

	//utils
	private ProgressDialog progress;
	private ListAdapter adpater01;
	private ListAdapter adpater02;
	private CommonSession cs;

	//calender
	public static int SUNDAY 		= 1;
	public static int MONDAY 		= 2;
	public static int TUESDAY 		= 3;
	public static int WEDNSESDAY 	= 4;
	public static int THURSDAY 		= 5;
	public static int FRIDAY 		= 6;
	public static int SATURDAY 		= 7;

	private String attendDt;
	private List<String> myList;
	private ArrayList<DayInfo> mDayList;
	private Calendar mThisMonthCalendar;
	private CalendarGridAdapter mCalendarAdapter;
	private ArrayList<InspectionPlanHistoryData> iphData;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ip_js00_r00);
		getInstances();
		manageCalConfig();
	}

	private void getInstances(){
		context 		   = this;
		cs				   = new CommonSession(context);
		et01_js00_YM_value = (TextView) findViewById(R.id.et01_js00_YM_value);
		btn_js_calendar		= (TextView)findViewById(R.id.btn_js_calendar);
		btn02_js00_search  = (TextView) findViewById(R.id.btn02_js00_search);
		gl01_js00_calender = (GridView) findViewById(R.id.gl01_js00_calender);
		itemList01		   = new ArrayList<IP_JS00_R01P_Item>();
		itemList02		   = new ArrayList<IP_JS00_R01P_Item>();
		setEvents();
	}
	private void setEvents(){
		et01_js00_YM_value.setOnClickListener(listner);
		btn_js_calendar.setOnClickListener(listner);
		btn02_js00_search.setOnClickListener(listner);
		setConfig();
	}
	private void setConfig(){
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("작업일지");
		aBar.setDisplayShowHomeEnabled(false);

		et01_js00_YM_value.setText( (DateUtil.nowDate()).substring(0, 7) );
		ActivityAdmin.getInstance().finishLastMenuActivites();
		ActivityAdmin.getInstance().addMenuActivity(this);
		ActivityAdmin.getInstance().addActivity(this);
	}


	private void manageCalConfig(){
		mDayList = new ArrayList<DayInfo>();
		myList = new ArrayList<String>();
		gl01_js00_calender.setOnItemClickListener(itemListener);
		mThisMonthCalendar = Calendar.getInstance();
		mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
		getCalendar(mThisMonthCalendar);
	}


	OnClickListener listner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.et01_js00_YM_value:
					startActivityForResult(new Intent(context,CalendarGridNoDataActivity.class), 99);
					break;
				case R.id.btn_js_calendar:
					startActivityForResult(new Intent(context,CalendarGridNoDataActivity.class), 99);
					break;
				case R.id.btn02_js00_search:
					String strDate = et01_js00_YM_value.getText().toString();
					int year  = Integer.parseInt(strDate.substring(0, 4));
					int month = Integer.parseInt(strDate.substring(5, 7));

					mThisMonthCalendar.set(year,month-1,1);
					getCalendar(mThisMonthCalendar);
					break;
			}
		}
	};


	OnItemClickListener itemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long arg3) {
			progress(true);
			attendDt = myList.get(position).toString();
			new Database().execute("selectBasicWorkTimeList");
		}
	};



	private void getCalendar(Calendar calendar){
		progress(true);
		int lastMonthStartDay;
		int dayOfMonth;
		int thisMonthLastDay;

		mDayList.clear();

		dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
		thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		calendar.add(Calendar.MONTH, -1);

		// 지난달의 마지막 일자를 구한다.
		lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		calendar.add(Calendar.MONTH, 1);

		if(dayOfMonth == SUNDAY)
		{
			dayOfMonth += 7;
		}

		lastMonthStartDay -= (dayOfMonth-1)-1;

		DayInfo day;

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
		new InspectionPlanHistoryAsync().execute();
	}


	public class InspectionPlanHistoryAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			try {
				iphData = new ArrayList<InspectionPlanHistoryData>();

				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"comm/selectInspectionPlanHistory.do";

				String workMm = et01_js00_YM_value.getText().toString();
				String csEmpId = cs.getEmpId();

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("csEmpId", csEmpId));
				arguments.add(new BasicNameValuePair("workMm", workMm));
				JSONObject returnJson = getHttp.getPost(param_url, arguments, true);
				myList.clear();

				try {
					ejl = new EasyJsonList( returnJson.getJSONArray("dataList") );

					iphData.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						iphData.add(new InspectionPlanHistoryData(ejl.getValue(i,
								"N_CNT"), ejl.getValue(i, "WORK_DT"), ejl
								.getValue(i, "WORK_DAY"), ejl.getValue(i,
								"WEEKDAY"), ejl.getValue(i, "DAY_NM1"), ejl
								.getValue(i, "DAY_NM2"), ejl.getValue(i,
								"DAY_NM3"), ejl.getValue(i, "D_CNT"), ejl
								.getValue(i, "N_CNT")));

						myList.add(ejl.getValue(i, "WORK_DT"));

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}


			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			initCalendarAdapter();
		}
	}


	private void initCalendarAdapter()
	{
		mCalendarAdapter = new CalendarGridAdapter(context, R.layout.griditem_dayinfo, mDayList, iphData);
		gl01_js00_calender.setAdapter(mCalendarAdapter);
		progress(false);
	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		switch(requestCode) {
			case 99:
				if (resultCode == RESULT_OK) {
					Bundle bundle = data.getExtras();
					String strDate = bundle.getString("dateSelected");
					et01_js00_YM_value.setText( strDate.substring(0, 7) );

					int year  = Integer.parseInt(strDate.substring(0, 4));
					int month = Integer.parseInt(strDate.substring(5, 7));

					mThisMonthCalendar.set(year,month-1,1);
					getCalendar(mThisMonthCalendar);
					break;
				}
		}
	}



	public class Database extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... params) {
			crud(params[0]);
			return params[0];
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			setData(result);
		}
	}
	private void crud(String div){

		GetHttp http = new GetHttp();

		if(div.equals("selectBasicWorkTimeList")){

			String param_url_01 = WebServerInfo.getUrl()+"ip/"+div+".do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.add(new BasicNameValuePair("csEmpId" , cs.getEmpId()));
			arguments.add(new BasicNameValuePair("attendDt", attendDt ));
			returnJson01 = http.getPost(param_url_01, arguments, true);

		}else if(div.equals("selectHomeCallSerList")){

			String param_url_02 = WebServerInfo.getUrl()+"ip/"+div+".do";
			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.add(new BasicNameValuePair("csEmpId" , cs.getEmpId()));
			arguments.add(new BasicNameValuePair("attendDt", attendDt ));
			returnJson02 = http.getPost(param_url_02, arguments, true);
		}

	}
	private void setData(String div){

		if(div.equals("selectBasicWorkTimeList")){

			try {

				ejm01 = new EasyJsonMap( returnJson01.getJSONObject("msgMap") );

				boolean isError = ejm01.getValue("errCd").equals("1") ? true : false;
				if(isError){
					progress(false);
					alert(ejm01.getValue("errMsg"), context);
				}else if(!isError){
					ejl01 = new EasyJsonList( returnJson01.getJSONArray("dataList") );
					new Database().execute("selectHomeCallSerList");

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}else if(div.equals("selectHomeCallSerList")){

			try {

				ejm01 = new EasyJsonMap( returnJson01.getJSONObject("msgMap") );
				boolean isError = ejm01.getValue("errCd").equals("1") ? true : false;
				if(isError){
					progress(false);
					alert(ejm01.getValue("errMsg"), context);
				}else if(!isError){
					ejl02 = new EasyJsonList( returnJson02.getJSONArray("dataList") );
					try{
						itemList01.clear();
						int jsonSize = returnJson01.getJSONArray("dataList").length();
						for(int i=1; i<jsonSize; i++){
							itemList01.add(new IP_JS00_R01P_Item(   ejl01.getValue(i, "WORK_NM")
											, ejl01.getValue(i, "CS_TM_FR")
											+"~"+ejl01.getValue(i, "CS_TM_TO")
											+"("+ejl01.getValue(i, "CS_TM")+"분)"
											, ejl01.getValue(i, "DEPT_NM")
											, ejl01.getValue(i, "BLDG_NM")
											, ejl01.getValue(i, "CAR_NO")
									)
							);
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

					try{
						itemList02.clear();
						int jsonSize = returnJson02.getJSONArray("dataList").length();
						for(int i=1; i<jsonSize; i++){
							itemList02.add(new IP_JS00_R01P_Item(   ejl02.getValue(i, "WORK_NM")
											, ejl02.getValue(i, "CS_TM_FR")
											+"~"+ejl02.getValue(i, "CS_TM_TO")
											+"("+ejl02.getValue(i, "CS_TM")+"분)"
											, ejl02.getValue(i, "DEPT_NM")
											, ejl02.getValue(i, "BLDG_NM")
											, ejl02.getValue(i, "CAR_NO")
									)
							);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

					//adapter
					adpater01 = new IP_JS00_R01P_Adapter(context, R.layout.ip_js00_r01p_adapter, itemList01);
					adpater02 = new IP_JS00_R01P_Adapter(context, R.layout.ip_js00_r01p_adapter, itemList02);

					IP_JS00_R01P d01 = new IP_JS00_R01P(context,ejl01,adpater01,adpater02);
					progress(false);
					d01.show();
					//progress(false);

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}


	private void alert(String msg, Context context){
		AlertView.showAlert(msg, context);
	}
	private void progress(Boolean isActivated){
		if(isActivated){
			IP_JS00_R00.this.progress =
					android.app.ProgressDialog.show(context, "알림","조회 중입니다.");
		}else{
			IP_JS00_R00.this.progress.dismiss();
		}
	}



	private HomeNavigation homeNavi;
	private FrameLayout testnavi;
	boolean isHide;
	private HomeNaviPreference naviPref;

	private void setToggleNavi(){
		boolean isHide = naviPref.isHide();
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int totalHeight = ScreenUtil.getRealScreenHeight(this);;
		int naviHeight = getResources().getDrawable(R.drawable.home_menu_on).getIntrinsicHeight();
		int viewArea = naviHeight/6;
		int setPadding = totalHeight-viewArea-naviHeight;
		if(isHide){
			testnavi.setPadding(0, setPadding, 0, 0);testnavi.setAlpha((float)0.5);
		}else{
			testnavi.setPadding(0, 0, 0, 0); testnavi.setAlpha((float)1);
		}
	}

	private void navigationInit(){
		testnavi = (FrameLayout) findViewById(R.id.fl_naviBar);
		homeNavi = new HomeNavigation(context, null);
		homeNavi = (HomeNavigation) findViewById(R.id.hn_homeNavi);
		homeNavi.setToggleNavi();
		Button navi = (Button) homeNavi.getBtn_naviHOME();
		navi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				naviPref.setHide(!naviPref.isHide());
				homeNavi.setToggleNavi();

			}
		});
	}
	@Override
	public void onResume(){
		super.onResume();
		naviPref = new HomeNaviPreference(context);
		navigationInit();
	}
};
