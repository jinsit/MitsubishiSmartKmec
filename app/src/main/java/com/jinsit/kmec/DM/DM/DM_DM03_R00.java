package com.jinsit.kmec.DM.DM;

import java.util.ArrayList;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

public class DM_DM03_R00 extends Activity implements OnClickListener,
		OnItemClickListener, OnItemSelectedListener {

	private Context context;


	private Spinner sp_dm_attendCd;
	private TextView btn_dm_inquery;
	private TextView et_dm_date;

	private EditText et_dm_rmk;
	private TextView btn_dm_calendar;

	private ListView lv_dm_approvalList,lv_dm_otList;

	private ProgressDialog progress;
	private CommonSession commonSession;



	private EasyJsonList ejl00, ejl01,ejl02;

	private List<DM_DM03_R01_ITEM00> itemList00;
	private List<DM_DM03_R01_ITEM01> itemList01;
	private List<DM_DM03_R01_ITEM02> itemList02;


	private List<DM_DM03_R02_ITEM01> itemList001;
	private List<DM_DM03_R02_ITEM02> itemList002;
	private DM_DM03_R01_Adapter00 adapter00;
	private DM_DM03_R01_Adapter01 adapter01;
	private DM_DM03_R01_Adapter02 adapter02;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dm_dm03_r00);
		activityInit();
	}


	protected void activityInit() {
		getInstances();
		context = this;
		commonSession = new CommonSession(context);

		//타이틀 바
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("OT/근태 승인");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		itemList00 = new ArrayList<DM_DM03_R01_ITEM00>();
		itemList01 = new ArrayList<DM_DM03_R01_ITEM01>();
		itemList02 = new ArrayList<DM_DM03_R01_ITEM02>();

		itemList001 = new ArrayList<DM_DM03_R02_ITEM01>();
		itemList002 = new ArrayList<DM_DM03_R02_ITEM02>();

		currentSelectedItem00 = null;

//	    itemList00.add(new DM_DM03_R01_ITEM00("전체","-"));
//	    itemList00.add(new DM_DM03_R01_ITEM00("승인대기","N"));
//	    itemList00.add(new DM_DM03_R01_ITEM00("승인완료","Y"));
//	    adapter00 = new DM_DM03_R01_Adapter00(context, R.layout.dm_dm03_r00_adapter01, itemList00);
//		DM_DM03_R00.this.sp_dm_attendCd.setAdapter(adapter00);
		this.progress(true);
		new selectApprovalOverTimeList().execute("getStCode");
		this.setAttendDate(commonSession.getWorkDt());
	}

	protected void getInstances() {
		sp_dm_attendCd = (Spinner) findViewById(R.id.sp_dm_attendCd);
		btn_dm_inquery = (TextView) findViewById(R.id.btn_dm_inquery);
		btn_dm_calendar = (TextView) findViewById(R.id.btn_dm_calendar);
		et_dm_date = (TextView) findViewById(R.id.et_dm_date);
		et_dm_rmk = (EditText) findViewById(R.id.et_dm_rmk);
		lv_dm_approvalList = (ListView) findViewById(R.id.lv_dm_approvalList);
		lv_dm_otList = (ListView) findViewById(R.id.lv_dm_otList);
		setEvents();
	}

	protected void setEvents() {
		btn_dm_inquery.setOnClickListener(this);
		btn_dm_calendar.setOnClickListener(this);
		et_dm_date.setOnClickListener(this);
		sp_dm_attendCd.setOnItemSelectedListener(this);
		lv_dm_approvalList.setOnItemClickListener(this);
		lv_dm_otList.setOnItemClickListener(this);
		setConfig();
	}

	private void setConfig(){
		ActivityAdmin.getInstance().addActivity(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_dm_calendar:
				getCalendar(0);
				break;
			case R.id.et_dm_date:
				getCalendar(0);
				break;
			case R.id.btn_dm_inquery:
				this.progress(true);
				new selectApprovalOverTimeList().execute("bagicWorkTime");
				break;
			default:
				break;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		if (parent.getAdapter() == adapter01) {
			if (position > -1) {
				currentSelectedItem01 = itemList01.get(position);
				detailInquery("bagicWorkTime");
			} else {
				currentSelectedItem01 = null;
			}
		} else if (parent.getAdapter() == adapter02) {
			if (position > -1) {
				currentSelectedItem02 = itemList02.get(position);
				detailInquery("bagicWorkTime2");
			} else {
				currentSelectedItem02 = null;
			}
		}

//		if(position > -1)
//		{
//			currentSelectedItem01 = itemList01.get(position);
//
//
//			detailInquery();
//	/*
//			DM_DM03_R02P dm01 = new DM_DM03_R02P(this, currentSelectedItem01,new btnClickListener(){
//				@Override
//				public void onButtonClick() {
//					DM_DM03_R00.this.inquery();
//				}
//			});
//			dm01.show();*/
//		}
//		else
//		{
//			currentSelectedItem01 = null;
//		}
	}


	public class selectApprovalOnOverTime extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject retJson01,retJson02;
		EasyJsonList ejl001,ejl002;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "dm/selectApprovalOnOverTime.do";
					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("selSt","1"));
					arguments.add(new BasicNameValuePair("csEmpId",currentSelectedItem01.getEMP_ID()));
					arguments.add(new BasicNameValuePair("workDt",getAttendDate()));
					arguments.add(new BasicNameValuePair("otNo","0"));

					retJson01 = http.getPost(param_url_01, arguments, true);

					try {
						ejl001 = new EasyJsonList(
								retJson01.getJSONArray("dataList"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				return params[0];
			}else if (params[0].equals("bagicWorkTime2")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "dm/selectApprovalOnOverTime.do";
					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("selSt","2"));
					arguments.add(new BasicNameValuePair("csEmpId",currentSelectedItem02.getEMP_ID()));
					arguments.add(new BasicNameValuePair("workDt",getAttendDate()));
					arguments.add(new BasicNameValuePair("otNo",currentSelectedItem02.getOT_NO()));

					retJson02 = http.getPost(param_url_01, arguments, true);

					try {
						ejl002 = new EasyJsonList(
								retJson02.getJSONArray("dataList"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				return params[0];
			}
			return "None";
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			// 1. bagicWorkTime
			if (result.equals("bagicWorkTime")) {

				try {
					itemList001.clear();
					int jsonSize01 = retJson01.getJSONArray("dataList")
							.length();

					for (int i = 0; i < jsonSize01; i++) {
						itemList001.add(new DM_DM03_R02_ITEM01(ejl001.getValue(i,
								"EMP_ID")
								, ejl001.getValue(i, "EMP_NM")
								, ejl001.getValue(i, "ATTEND_CD")
								, ejl001.getValue(i, "ATTEN_CD_NM")
								, ejl001.getValue(i, "ATTEND_DT")
								, ejl001.getValue(i, "RMK")
								, ejl001.getValue(i, "REP_ST")
						));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				progress(false);
				if(itemList001.size()!=0){
					DM_DM03_R02P dm03 = new DM_DM03_R02P(context, itemList001.get(0),
							new com.jinsit.kmec.DM.DM.DM_DM03_R02P.btnClickListener() {

								@Override
								public void onButtonClick() {
									// TODO Auto-generated method stub
									progress(true);
									new selectApprovalOverTimeList().execute("bagicWorkTime");
								}
							}, getAttendDate());
					dm03.show();
				}
				progress(false);
				if(itemList001.size()!=0){

					//DM_DM03_R02P dm01 = new DM_DM03_R02P(context, itemList001.get(0), null,getAttendDate());
					//dm01.show();
				}

//				adapter02 = new DM_DM03_R00_Adapter02(context,
//						R.layout.dm_dm03_r00_adapter02, itemList02);
//
//				DM_DM03_R00.this.lv_dm_approvalList.setAdapter(adapter02);
			}else if(result.equals("bagicWorkTime2")){
				try {
					itemList002.clear();
					int jsonSize01 = retJson02.getJSONArray("dataList")
							.length();
					for (int i = 0; i < jsonSize01; i++) {
						itemList002.add(new DM_DM03_R02_ITEM02(ejl002.getValue(i,
								"OT_NO")
								, ejl002.getValue(i, "CS_EMP_ID")
								, ejl002.getValue(i, "EMP_NM")
								, ejl002.getValue(i, "BLDG_NM")
								, ejl002.getValue(i, "CAR_NO")
								, ejl002.getValue(i, "OT_WORK_CD")
								, ejl002.getValue(i, "WORK_NM")
								, ejl002.getValue(i, "OT_TM")
								, ejl002.getValue(i, "TOTAL_OT_TM")
								, ejl002.getValue(i, "OT_FR")
								, ejl002.getValue(i, "OT_TO")
								, ejl002.getValue(i, "RMK")
								, ejl002.getValue(i, "REP_ST")
						));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				progress(false);
				if(itemList002.size()!=0){
					DM_DM03_R03P dm03 = new DM_DM03_R03P(context, itemList002.get(0),
							new com.jinsit.kmec.DM.DM.DM_DM03_R03P.btnClickListener() {

								@Override
								public void onButtonClick() {
									// TODO Auto-generated method stub
									progress(true);
									new selectApprovalOverTimeList().execute("bagicWorkTime");
								}
							}, getAttendDate());
					dm03.show();
				}

			}

		}
	}// end of SelectData inner-class


	public class selectApprovalOverTimeList extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson00,returnJson01,returnJson02;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "dm/selectApprovalOverTimeList.do";
					///근태
					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("selTp","2"));
					arguments.add(new BasicNameValuePair("apprEmpId",commonSession.getEmpId()));
					arguments.add(new BasicNameValuePair("workDt",getAttendDate()));
					arguments.add(new BasicNameValuePair("repSt",currentSelectedItem00.getCd()));

					returnJson01 = http.getPost(param_url_01, arguments, true);
					///특별오티
					arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("selTp","3"));
					arguments.add(new BasicNameValuePair("apprEmpId",commonSession.getEmpId()));
					arguments.add(new BasicNameValuePair("workDt",getAttendDate()));
					arguments.add(new BasicNameValuePair("repSt",currentSelectedItem00.getCd()));

					returnJson02 = http.getPost(param_url_01, arguments, true);

					try {
						ejl01 = new EasyJsonList(
								returnJson01.getJSONArray("dataList"));
						ejl02 = new EasyJsonList(
								returnJson02.getJSONArray("dataList"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				return params[0];
			}else if(params[0].equals("getStCode")){
				//상신상태코드 조회
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "dm/selectApprovalOverTimeList.do";
					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("selTp","1"));
					arguments.add(new BasicNameValuePair("apprEmpId",commonSession.getEmpId()));
					arguments.add(new BasicNameValuePair("workDt",getAttendDate()));
					arguments.add(new BasicNameValuePair("repSt",""));

					returnJson00 = http.getPost(param_url_01, arguments, true);

					try {
						ejl00 = new EasyJsonList(
								returnJson00.getJSONArray("dataList"));

					} catch (JSONException e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				return params[0];
			}
			return "None";
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			// 1. bagicWorkTime
			if (result.equals("bagicWorkTime")) {

				try {
					itemList01.clear();
					itemList02.clear();
					int jsonSize01 = returnJson01.getJSONArray("dataList")
							.length();

					for (int i = 0; i < jsonSize01; i++) {
						itemList01.add(new DM_DM03_R01_ITEM01(ejl01.getValue(i,
								"EMP_ID")
								, ejl01.getValue(i, "EMP_NM")
								, ejl01.getValue(i, "ATTEN_CD_NM")
								, ejl01.getValue(i, "REP_ST_NM")));
					}


					int jsonSize02 = returnJson02.getJSONArray("dataList")
							.length();
					for (int i = 0; i < jsonSize02; i++) {
						itemList02.add(new DM_DM03_R01_ITEM02(ejl02.getValue(i,"CS_EMP_ID")
								, ejl02.getValue(i, "EMP_NM")
								, ejl02.getValue(i, "OT_NO")
								, ejl02.getValue(i, "OT_TM")
								, ejl02.getValue(i, "TOTAL_OT_TM")
								, ejl02.getValue(i, "BLDG_NM")
								, ejl02.getValue(i, "REP_ST_NM")));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				progress(false);
				adapter01 = new DM_DM03_R01_Adapter01(context,
						R.layout.dm_dm03_r01_adapter01, itemList01);
				DM_DM03_R00.this.lv_dm_approvalList.setAdapter(adapter01);
				//2번
				adapter02 = new DM_DM03_R01_Adapter02(context,
						R.layout.dm_dm03_r01_adapter02, itemList02);
				DM_DM03_R00.this.lv_dm_otList.setAdapter(adapter02);

			}else if(result.equals("getStCode")){
				try {
					itemList00.clear();
					itemList00.add(new DM_DM03_R01_ITEM00("전체",""));
					int jsonSize00 = returnJson00.getJSONArray("dataList")
							.length();

					for (int i = 0; i < jsonSize00; i++) {
						itemList00.add(new DM_DM03_R01_ITEM00(ejl00.getValue(i,
								"title")
								, ejl00.getValue(i, "sub_cd")));
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

				progress(false);
				adapter00 = new DM_DM03_R01_Adapter00(context,
						R.layout.dm_dm03_r00_adapter01, itemList00);
				DM_DM03_R00.this.sp_dm_attendCd.setAdapter(adapter00);
			}


		}
	}// end of SelectData inner-class


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
							   long id) {
		// TODO Auto-generated method stub
		if (position > -1) {
			this.currentSelectedItem00 = itemList00.get(position);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		this.currentSelectedItem00 = null;

	}
	public void detailInquery(String execute)
	{
		this.progress(true);
		new selectApprovalOnOverTime().execute(execute);
	}
	public void inquery()
	{
		this.progress(true);
		new selectApprovalOverTimeList().execute("bagicWorkTime");
	}
	private void progress(Boolean isActivated) {
		if (isActivated) {
			DM_DM03_R00.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			DM_DM03_R00.this.progress.dismiss();
		}
	}

	private void getCalendar(int fromTo) {

		Intent intent = new Intent(DM_DM03_R00.this,
				com.jinsit.kmec.comm.CalendarGridNoDataActivity.class);
		startActivityForResult(intent, fromTo);
	}

	/**
	 * @author 원성민 캘린더를 호출하여 선택된 날짜를 반환받을 때 사용합니다.
	 *
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case 0:
				if (resultCode == RESULT_OK) {
					Bundle bundle = data.getExtras();
					this.setAttendDate(bundle
							.getString("dateSelected"));
					break;
				} else {
					break;
				}
			default:
				break;
		}
	}

	private DM_DM03_R01_ITEM00 currentSelectedItem00;
	private DM_DM03_R01_ITEM01 currentSelectedItem01;
	private DM_DM03_R01_ITEM02 currentSelectedItem02;
	private String attendDate;
	public String getAttendDate() {
		return  this.et_dm_date.getText().toString();
	}

	public void setAttendDate(String registerDate) {
		this.attendDate = registerDate;
		this.et_dm_date.setText(registerDate);
	}

	private String remark;
	private String getRemark()
	{
		return this.et_dm_rmk.getText().toString();
	}
	private void setRemark(String remark)
	{
		this.remark = remark;
		this.et_dm_rmk.setText(remark);
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

}


	
	
