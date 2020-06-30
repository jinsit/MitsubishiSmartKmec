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
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
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
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.DM.DM.DM_DM01_R00_Adapter02.RefreshListener;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

public class DM_DM01_R00 extends Activity implements OnClickListener,
		OnItemClickListener {
	private Context context;
	private TextView et_dm_registerDate;
	private TextView tv_dm_basicOtSubTotalHours;
	private TextView tv_dm_nightDutyHours;
	private TextView tv_dm_specialOtSubTotalHours;
	private TextView tv_dm_totalHours;

	//private TextView btn_dm_udpate;
	private TextView btn_dm_makeOt;
	private TextView btn_dm_makeAttendance;
	private TextView btn_dm_inquery;
	private TextView btn_dm_calendar;

	private ProgressDialog progress;

	private ListView lv_dm_basicOt;
	private ListView lv_dm_specialOt;
	private ListView lv_dm_attend;
	private CommonSession commonSession;

	private EasyJsonList ejl01;
	private EasyJsonList ejl02,ejl03;
	private EasyJsonMap ej01;

	private DM_DM01_R00_ITEM01 item;
	private List<DM_DM01_R00_ITEM02> itemList01;
	private List<DM_DM01_R00_ITEM03> itemList02;
	private List<DM_DM01_R00_ITEM04> itemList03;
	private DM_DM01_R00_Adapter01 adapter01;
	private DM_DM01_R00_Adapter02 adapter02;
	private DM_DM01_R00_Adapter03 adapter03;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dm_dm01_r00);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		context = this;
		commonSession = new CommonSession(context);

		//타이틀 바
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("OT/근태 등록");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		item = new DM_DM01_R00_ITEM01();
		itemList01 = new ArrayList<DM_DM01_R00_ITEM02>();
		itemList02 = new ArrayList<DM_DM01_R00_ITEM03>();
		itemList03 = new ArrayList<DM_DM01_R00_ITEM04>();

		currentSelectedItem02 = null;
		currentSelectedItem03 = null;
		this.setRegisterDate(commonSession.getWorkDt());
		//this.btn_dm_makeOt.setVisibility(View.GONE);
		//this.btn_dm_udpate.setVisibility(View.GONE);


		//액티비티 시작하자마자 바로 조회
		this.searchOverTimeSchedule();


	}

	protected void getInstances() {
		et_dm_registerDate = (TextView) findViewById(R.id.et_dm_registerDate);
		tv_dm_basicOtSubTotalHours = (TextView) findViewById(R.id.tv_dm_basicOtSubTotalHours);
		tv_dm_nightDutyHours = (TextView) findViewById(R.id.tv_dm_nightDutyHours);
		tv_dm_specialOtSubTotalHours = (TextView) findViewById(R.id.tv_dm_specialOtSubTotalHours);
		tv_dm_totalHours = (TextView) findViewById(R.id.tv_dm_totalHours);

		//btn_dm_udpate = (TextView) findViewById(R.id.btn_dm_udpate);
		btn_dm_makeOt = (TextView) findViewById(R.id.btn_dm_makeOt);
		btn_dm_makeAttendance = (TextView)findViewById(R.id.btn_dm_makeAttendance);
		btn_dm_inquery = (TextView) findViewById(R.id.btn_dm_inquery);
		btn_dm_calendar = (TextView) findViewById(R.id.btn_dm_calendar);

		lv_dm_basicOt = (ListView) findViewById(R.id.lv_dm_basicOt);
		lv_dm_specialOt = (ListView) findViewById(R.id.lv_dm_specialOt);
		lv_dm_attend= (ListView) findViewById(R.id.lv_dm_attend);
		setEvents();
	}

	protected void setEvents() {
		btn_dm_calendar.setOnClickListener(this);
		et_dm_registerDate.setOnClickListener(this);
		//btn_dm_udpate.setOnClickListener(this);
		btn_dm_makeOt.setOnClickListener(this);
		btn_dm_makeAttendance.setOnClickListener(this);
		btn_dm_inquery.setOnClickListener(this);
		lv_dm_specialOt.setOnItemClickListener(this);
		lv_dm_attend.setOnItemClickListener(this);
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
			case R.id.et_dm_registerDate:
				getCalendar(0);
				break;

	/*	case R.id.btn_dm_udpate:
			if(itemList02.size() == 0){
			//if(itemList01.size() == 0 && itemList02.size() == 0){
				AlertView.showAlert("상신 할 데이타가 없습니다.", context);
				return;
			}
			this.progress(true);
			new upate().execute("bagicWorkTime");
			break;*/
			case R.id.btn_dm_makeOt:
				DM_DM01_R01P dm01 = new DM_DM01_R01P(this,item.getCsEmpId());
				dm01.show();
				dm01.searchWorkType();
				dm01.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						// TODO Auto-generated method stub
						searchOverTimeSchedule();
					}

				});
				break;


			case R.id.btn_dm_makeAttendance:
				Intent intent = new Intent(this, DM_DM02_R00P.class);
				startActivity(intent);

				break;

			case R.id.btn_dm_inquery:
				searchOverTimeSchedule();
				break;

			default:
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub

		if(parent.getAdapter()==adapter02){
			//특별오티
			if(position > -1)
			{
				currentSelectedItem02 = itemList02.get(position);
				if(!currentSelectedItem02.getOtRemark().equals("") && currentSelectedItem02.getOtRemark() != null){
					DM_DM01_R02P dm01 = new DM_DM01_R02P(this, currentSelectedItem02);
					dm01.show();
					dm01.setOnDismissListener(new OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
							// TODO Auto-generated method stub
							searchOverTimeSchedule();
						}

					});
				}
			}
			else
			{
				currentSelectedItem02 = null;
			}
		}else if(parent.getAdapter()==adapter03){
			//근태

			if(position > -1)
			{

				currentSelectedItem03 = itemList03.get(position);

				Bundle extras = new Bundle();
				Intent intent = new Intent(this, DM_DM02_R00P.class);
				extras.putSerializable("item", this.currentSelectedItem03);
				intent.putExtras(extras);
				startActivity(intent);

//					Intent intent = new Intent(this, DM_DM02_R00P.class);
//					startActivity(intent);


//				currentSelectedItem03 = itemList03.get(position);
//				if(!currentSelectedItem03.getREP_ST().equals("0") && currentSelectedItem03.getOtRemark() != null){
//					DM_DM01_R02P dm01 = new DM_DM01_R02P(this, currentSelectedItem02);
//					dm01.show();
//				}
			}
			else
			{
				currentSelectedItem03 = null;
			}
		}

	}

	public void searchOverTimeSchedule(){
		this.progress(true);
		new selectOverTimeSchedule().execute("bagicWorkTime");
	}
	public class selectOverTimeSchedule extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;
		JSONObject returnJson02;
		JSONObject returnJson03;
		JSONObject returnJson04;


		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "dm/selectOverTimeSchedule.do";
					String param_url_02 = WebServerInfo.getUrl()
							+ "dm/selectOverTimeSchedule.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("selTp","1"));
					arguments.add(new BasicNameValuePair("csEmpId",// "301241"
							DM_DM01_R00.this.commonSession.getEmpId()));
					arguments.add(new BasicNameValuePair("attendDt",
							DM_DM01_R00.this.getRegisterDate()));
					returnJson01 = http.getPost(param_url_01, arguments, true);
					arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("selTp","2"));
					arguments.add(new BasicNameValuePair("csEmpId",//"301241"
							DM_DM01_R00.this.commonSession.getEmpId()));
					arguments.add(new BasicNameValuePair("attendDt",
							DM_DM01_R00.this.getRegisterDate()));
					returnJson02 = http.getPost(param_url_02, arguments, true);
					arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("selTp","3"));
					arguments.add(new BasicNameValuePair("csEmpId",//"301241"
							DM_DM01_R00.this.commonSession.getEmpId()));
					arguments.add(new BasicNameValuePair("attendDt",
							DM_DM01_R00.this.getRegisterDate()));
					returnJson03 = http.getPost(param_url_02, arguments, true);
					arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("selTp","4"));
					arguments.add(new BasicNameValuePair("csEmpId",//"301241"
							DM_DM01_R00.this.commonSession.getEmpId()));
					arguments.add(new BasicNameValuePair("attendDt",
							DM_DM01_R00.this.getRegisterDate()));
					returnJson04 = http.getPost(param_url_02, arguments, true);


					try {
						ej01 = new EasyJsonMap(
								returnJson02.getJSONObject("dataMap"));
						ejl01 = new EasyJsonList(returnJson01.getJSONArray("dataList"));
						ejl02 = new EasyJsonList(returnJson03.getJSONArray("dataList"));
						ejl03 = new EasyJsonList(returnJson04.getJSONArray("dataList"));
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
					itemList03.clear();
					item = null;
					item = new DM_DM01_R00_ITEM01(ej01.getValue("CS_EMP_ID")
							, ej01.getValue("WORK_DT")
							,ej01.getValue("APPROVAL_YN")
							,ej01.getValue("BASIC_OT")
							,ej01.getValue("ON_DUTY_OT")
							,ej01.getValue("SPECIAL_OT"));

					int jsonSize01 = returnJson01.getJSONArray("dataList")
							.length();
					for (int i = 0; i < jsonSize01; i++) {
						itemList01.add(new DM_DM01_R00_ITEM02(ejl01.getValue(i,"CS_EMP_ID")
								, ejl01.getValue(i, "WORK_DT")
								,ejl01.getValue(i, "JOB_NO")
								,ejl01.getValue(i, "WORK_CD")
								,ejl01.getValue(i, "BLDG_NO")
								,ejl01.getValue(i, "BLDG_NM")
								,ejl01.getValue(i, "CAR_NO")
								,ejl01.getValue(i, "DONG_CAR_NO")
								,ejl01.getValue(i, "CS_DT_FR")
								,ejl01.getValue(i, "CS_DT_TO")
								,ejl01.getValue(i, "CS_TM_FR")
								,ejl01.getValue(i, "CS_TM_TO")
								,ejl01.getValue(i, "CS_TM")));
					}
					int jsonSize02 = returnJson03.getJSONArray("dataList")
							.length();
					for (int i = 0; i < jsonSize02; i++) {
						itemList02.add(new DM_DM01_R00_ITEM03(ejl02.getValue(i,"CS_EMP_ID")
								, ejl02.getValue(i, "OT_WORK_DT")
								,ejl02.getValue(i, "OT_NO")
								,ejl02.getValue(i, "OT_WORK_CD")
								,ejl02.getValue(i, "WORK_NM")
								,ejl02.getValue(i, "BLDG_NO")
								,ejl02.getValue(i, "BLDG_NM")
								,ejl02.getValue(i, "CAR_NO")
								,ejl02.getValue(i, "OT_TM_FR")
								,ejl02.getValue(i, "OT_TM_TO")
								,ejl02.getValue(i, "OT_TM")
								,ejl02.getValue(i, "REP_ST")
								,ejl02.getValue(i, "REP_ST_NM")
								,ejl02.getValue(i, "OT_RMK")));
					}

					int jsonSize03 = returnJson04.getJSONArray("dataList").length();
					for(int i=0;i<jsonSize03;i++){
						itemList03.add(new DM_DM01_R00_ITEM04(ejl03.getValue(i, "EMP_ID"),
								ejl03.getValue(i, "EMP_NM"),
								ejl03.getValue(i, "ATTEN_CD_NM"),
								ejl03.getValue(i, "REP_ST"),
								ejl03.getValue(i, "REP_ST_NM"),
								ejl03.getValue(i, "OT_RMK"),
								ejl03.getValue(i, "OT_WORK_DT")
						));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				progress(false);
				DM_DM01_R00.this.setBasicOtSubTotalHours(item.getBasicOt());
				DM_DM01_R00.this.setNightDutyHours(item.getOnDutyOt());
				DM_DM01_R00.this.setSpecialOtSubTotalHours(item.getSpecialOt());
				DM_DM01_R00.this.setTotalHours();

				adapter01 = new DM_DM01_R00_Adapter01(context, R.layout.dm_dm01_r00_adapter01, itemList01);
				adapter02 = new DM_DM01_R00_Adapter02(context, R.layout.dm_dm01_r00_adapter02, itemList02, new RefreshListener()
				{
					@Override
					public void onRefresh() {
						// TODO Auto-generated method stub
						DM_DM01_R00.this.searchOverTimeSchedule();
					}
				});
				adapter03 = new DM_DM01_R00_Adapter03(context, R.layout.dm_dm01_r00_adapter03, itemList03,getRegisterDate(),
						new  com.jinsit.kmec.DM.DM.DM_DM01_R00_Adapter03.RefreshListener()
						{
							@Override
							public void onRefresh() {
								// TODO Auto-generated method stub
								DM_DM01_R00.this.searchOverTimeSchedule();
							}
						});
				DM_DM01_R00.this.lv_dm_basicOt.setAdapter(adapter01);
				DM_DM01_R00.this.lv_dm_specialOt.setAdapter(adapter02);
				DM_DM01_R00.this.lv_dm_attend.setAdapter(adapter03);
				if(itemList01.size() == 0 && itemList02.size() == 0 &&itemList03.size() == 0){
					AlertView.showAlert("조회된 데이타가 없습니다.", context);
				}

		/*
				if (item.getApprovalYn().equals("0")) {
					//APPROVALYN이 0은 미상신, 1:상신, Y:승인
					if (DM_DM01_R00.this.getRegisterDate().equals(
							DateUtil.nowDate())) {
						DM_DM01_R00.this.btn_dm_makeOt
								.setVisibility(View.VISIBLE);
						DM_DM01_R00.this.btn_dm_udpate
								.setVisibility(View.VISIBLE);
					} else {
						DM_DM01_R00.this.btn_dm_makeOt.setVisibility(View.GONE);
						DM_DM01_R00.this.btn_dm_udpate.setVisibility(View.GONE);
					}

				} else {
					DM_DM01_R00.this.btn_dm_makeOt.setVisibility(View.GONE);
					DM_DM01_R00.this.btn_dm_udpate.setVisibility(View.GONE);
				}*/
			}

		}
	}// end of SelectData inner-class

	/*private class upate extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "dm/updateReportToManager.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("csEmpId",item.getCsEmpId()));
					arguments.add(new BasicNameValuePair("otWorkDt",
							DM_DM01_R00.this.registerDate));
					arguments.add(new BasicNameValuePair("usrId",
							DM_DM01_R00.this.commonSession.getEmpId()));

					returnJson01 = http.getPost(param_url_01, arguments, true);
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
			String resultFg= "0";
			if (result.equals("bagicWorkTime")) {
				try {
					resultFg = returnJson01.getString("dataString");
					}
				catch (JSONException e) {
					e.printStackTrace();
				}
				progress(false);
				if(resultFg.equals("1"))
				{
					AlertView.showAlert("상신 성공 했습니다.", context);
					//DM_DM01_R00.this.btn_dm_makeOt.setVisibility(View.GONE);
					//DM_DM01_R00.this.btn_dm_udpate.setVisibility(View.GONE);
				}
				else
				{
					AlertView.showAlert("상신 실패 했습니다.", context);
				}

			}
		}
	}// end of SelectData inner-class
	*/
	private void progress(Boolean isActivated) {
		if (isActivated) {
			DM_DM01_R00.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			DM_DM01_R00.this.progress.dismiss();
		}
	}

	private void getCalendar(int fromTo) {

		Intent intent = new Intent(DM_DM01_R00.this,
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
					this.setRegisterDate(bundle
							.getString("dateSelected"));
					//달력 날짜 가져오자마자 조회
					this.searchOverTimeSchedule();
					break;
				} else {
					break;
				}
			default:
				break;
		}
	}

	private DM_DM01_R00_ITEM03 currentSelectedItem02;
	private DM_DM01_R00_ITEM04 currentSelectedItem03;
	private String registerDate;
	public String getRegisterDate() {
		return  this.et_dm_registerDate.getText().toString();
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
		this.et_dm_registerDate.setText(registerDate);
	}


	private String basicOtSubTotalHours;
	public void setBasicOtSubTotalHours(String basicOtSubTotalHours) {
		this.basicOtSubTotalHours = basicOtSubTotalHours;
		this.tv_dm_basicOtSubTotalHours.setText(basicOtSubTotalHours);
	}
	private String nightDutyHours;
	private void setNightDutyHours(String nightDutyHours) {
		this.nightDutyHours = nightDutyHours;
		this.tv_dm_nightDutyHours.setText(nightDutyHours);
	}

	private String specialOtSubTotalHours;
	private void setSpecialOtSubTotalHours(String specialOtSubTotalHours) {
		this.specialOtSubTotalHours = specialOtSubTotalHours;
		this.tv_dm_specialOtSubTotalHours.setText(specialOtSubTotalHours);
	}


	private String totalHours;
	private void setTotalHours() {
		try
		{
			double basicOtsubTotalHours = Double.valueOf(this.basicOtSubTotalHours);
			double nightDutyHours = Double.valueOf(this.nightDutyHours);
			double specialOtSubTotalHours = Double.valueOf(this.specialOtSubTotalHours);
			double totalHours = basicOtsubTotalHours + nightDutyHours + specialOtSubTotalHours;
			this.tv_dm_totalHours.setText(String.valueOf(totalHours));
			this.totalHours = String.valueOf(totalHours);
		}
		catch(Exception ex)
		{
			this.totalHours ="0";
			this.tv_dm_totalHours.setText("0");
		}
	}
	/////////////////////////////////////여긴볼필요없음
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
	private boolean isFirstHide = false;
	private void navigationInit(){
		testnavi = (FrameLayout) findViewById(R.id.fl_naviBar);
		homeNavi = new HomeNavigation(context, null);
		homeNavi = (HomeNavigation) findViewById(R.id.hn_homeNavi);
		if(!isFirstHide){
			naviPref.setHide(true);
			isFirstHide = true;
		}
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


	
	
