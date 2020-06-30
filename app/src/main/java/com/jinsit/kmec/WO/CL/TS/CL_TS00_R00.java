package com.jinsit.kmec.WO.CL.TS;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.CM_ReadPicture;
import com.jinsit.kmec.CM.CM_SearchBldgInfo_ITEM01;
import com.jinsit.kmec.CM.SearchAdminInfo;
import com.jinsit.kmec.WO.WT.TS.WO_TS00_R00_ITEM01;
import com.jinsit.kmec.WO.WT.TS.WO_TS00_R00_ITEM02;
import com.jinsit.kmec.WO.WT.TS.WO_TS00_R00_ITEM03;
import com.jinsit.kmec.WO.WT.TS.WO_TS00_R00_ITEM04;
import com.jinsit.kmec.WO.WT.TS.WO_TS00_R03P_ITEM01;
import com.jinsit.kmec.WO.WT.TS.WO_TS00_R04P_ITEM01;
import com.jinsit.kmec.WO.WT.TS.WO_TS00_R07P_ITEM01;
import com.jinsit.kmec.WO.WT.TS.WO_TS00_R08P_ITEM01;
import com.jinsit.kmec.WO.WT.TS.WO_TS00_R09P_ITEM01;
import com.jinsit.kmec.WO.WT.TS.WO_TS00_R11P;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.StringUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

/**
 * 고장수리 완료 화면
 */
public class CL_TS00_R00 extends Activity implements OnClickListener {
	private Context context;
	private EasyJsonMap ej01;
	private EasyJsonMap ej02;
	private WO_TS00_R00_ITEM03 workTargetData;
	private WO_TS00_R00_ITEM01 item01;
	private WO_TS00_R00_ITEM02 item02;
	private WO_TS00_R00_ITEM04 item04;
	
	private ProgressDialog progress;

	private TextView tv_fr_date;
	private TextView tv_fr_workNm;
	private TextView tv_fr_bldgInfo;
	private TextView tv_fr_addr;
	private TextView tv_fr_csDeptNm;
	private TextView tv_fr_empNm1;
	private TextView tv_fr_empHp1;
	private TextView tv_fr_empNm2;
	private TextView tv_fr_empHp2;
	private TextView tv_fr_notifyNm;
	private TextView tv_fr_recevDesc;
	
	private TextView tv_fr_specialDesc;

	private TextView btn_fr_adminInfo;
	private TextView btn_fr_reserverTime;

	private TextView tv_fr_travelTime;
	private TextView tv_fr_arrivalTime;
	private TextView tv_fr_repaireCompletionTime;
	private TextView tv_fr_completionTime;

	private TextView btn_fr_troublePicture;
	private CheckBox cb_fr_respond;
	private CheckBox cb_fr_confined;
	private TextView tv_fr_rescuetime;
	private TextView btn_fr_aSProcCode;
	
	private LinearLayout lin_fr_cpErrorCode;
	private TextView tv_fr_cpErrorCode;
	
	private TextView btn_fr_aSProcReport;
	private TextView btn_completework;
	
	private TextView tv_fr_st;
	private TextView tv_fr_csfr;
	
	private LinearLayout lin_aSProcCode;
	
	private TextView tv_fr_aSProcCode;
	private TextView tv_fr_CbsNm1;
	private TextView tv_fr_CbsNm2;
	private TextView tv_fr_CbsNm3;
	private TextView tv_fr_falutNm;
	private TextView tv_fr_procNm;
	private TextView tv_fr_dutyNm;

	
	private EditText et_fr_orderDesc;
	
	
	private CommonSession commonSession;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wo_ts00_r00);
		activityInit();
		
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		searchDetail();
	};

	protected void activityInit() {
		context = this;
		// 타이틀 바
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("작업대상정보");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		getInstances();
		commonSession = new CommonSession(context);
		
		jobNo = getIntent().getExtras().getString("jobNo");
		workDt = getIntent().getExtras().getString("workDt");
		searchDetail();
	
		lin_aSProcCode.setVisibility(View.GONE);
		
		this.selectedItem03 = null;
		this.selectedItemList04 = new ArrayList<WO_TS00_R04P_ITEM01>();
		this.selectedItem07 = null;
		this.selectedItem08 = null;
		this.selectedItem09 = null;
		this.cpErrorCodeList = new ArrayList<String>();
	}

	protected void getInstances() {
		tv_fr_date = (TextView) findViewById(R.id.tv_fr_date);
		tv_fr_workNm = (TextView) findViewById(R.id.tv_fr_workNm);
		tv_fr_bldgInfo = (TextView) findViewById(R.id.tv_fr_bldgInfo);
		tv_fr_addr = (TextView) findViewById(R.id.tv_fr_addr);
		tv_fr_csDeptNm = (TextView) findViewById(R.id.tv_fr_csDeptNm);
		tv_fr_empNm1 = (TextView) findViewById(R.id.tv_fr_empNm1);
		tv_fr_empHp1 = (TextView) findViewById(R.id.tv_fr_empHp1);
		tv_fr_empNm2 = (TextView) findViewById(R.id.tv_fr_empNm2);
		tv_fr_empHp2 = (TextView) findViewById(R.id.tv_fr_empHp2);
		tv_fr_notifyNm = (TextView) findViewById(R.id.tv_fr_notifyNm);
		tv_fr_recevDesc = (TextView) findViewById(R.id.tv_fr_recevDesc);
		tv_fr_specialDesc = (TextView)findViewById(R.id.tv_fr_specialDesc);
		btn_fr_adminInfo = (TextView) findViewById(R.id.btn_fr_adminInfo);
		btn_fr_reserverTime = (TextView) findViewById(R.id.btn_fr_reserverTime);

		tv_fr_travelTime = (TextView) findViewById(R.id.tv_fr_travelTime);
		tv_fr_arrivalTime = (TextView) findViewById(R.id.tv_fr_arrivalTime);
		tv_fr_repaireCompletionTime = (TextView) findViewById(R.id.tv_fr_repaireCompletionTime);
		tv_fr_completionTime = (TextView) findViewById(R.id.tv_fr_completionTime);

		btn_fr_troublePicture = (TextView) findViewById(R.id.btn_fr_troublePicture);

		cb_fr_respond = (CheckBox) findViewById(R.id.cb_fr_respond);
		cb_fr_respond.setClickable(false);
		cb_fr_confined = (CheckBox) findViewById(R.id.cb_fr_confined);
		cb_fr_confined.setClickable(false);
		tv_fr_rescuetime = (TextView) findViewById(R.id.tv_fr_rescuetime);
		btn_fr_aSProcCode = (TextView) findViewById(R.id.btn_fr_aSProcCode);
		
		lin_fr_cpErrorCode = (LinearLayout) findViewById(R.id.lin_fr_cpErrorCode);
		tv_fr_cpErrorCode = (TextView) findViewById(R.id.tv_fr_cpErrorCode);

		tv_fr_cpErrorCode.setVisibility(View.GONE);
		
		btn_fr_aSProcReport = (TextView) findViewById(R.id.btn_fr_aSProcReport);
		btn_completework = (TextView) findViewById(R.id.btn_completework);
		btn_completework.setVisibility(View.GONE);
		
		tv_fr_st = (TextView) findViewById(R.id.tv_fr_st);
		tv_fr_csfr = (TextView) findViewById(R.id.tv_fr_csfr);
		
		lin_aSProcCode= (LinearLayout)findViewById(R.id.lin_aSProcCode);
		
		tv_fr_aSProcCode = (TextView) findViewById(R.id.tv_fr_aSProcCode);
		tv_fr_CbsNm1 = (TextView) findViewById(R.id.tv_fr_CbsNm1);
		tv_fr_CbsNm2 = (TextView) findViewById(R.id.tv_fr_CbsNm2);
		tv_fr_CbsNm3= (TextView) findViewById(R.id.tv_fr_CbsNm3);
		tv_fr_falutNm = (TextView) findViewById(R.id.tv_fr_falutNm);
		tv_fr_procNm = (TextView) findViewById(R.id.tv_fr_procNm);
		tv_fr_dutyNm = (TextView) findViewById(R.id.tv_fr_dutyNm);
		
		et_fr_orderDesc = (EditText)findViewById(R.id.et_fr_orderDesc);
		et_fr_orderDesc.setVisibility(View.GONE);

		setEvents();
	}

	protected void setEvents() {
		btn_fr_adminInfo.setOnClickListener(this);
		btn_fr_reserverTime.setOnClickListener(this);

		tv_fr_rescuetime.setOnClickListener(this);
		btn_fr_troublePicture.setOnClickListener(this);
		btn_fr_aSProcReport.setOnClickListener(this);
		setConfig();
	}

	private void setConfig(){
		ActivityAdmin.getInstance().addActivity(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_fr_adminInfo:
			SearchAdminInfo searchAdminInfo = new SearchAdminInfo(context,
					workTargetData.getBldgNo());
			searchAdminInfo.show();
			break;
		
		case R.id.btn_fr_troublePicture:
			CM_ReadPicture wo14 = new CM_ReadPicture(context, workTargetData.getJobNO() ,workTargetData.getWorkDt(),"1");
			wo14.show();
			wo14.inqueryImages();
			break;
		case R.id.btn_fr_aSProcReport:
			if( item04.getCustSign() != null && !item04.getCustSign().equals("")){
				final WO_TS00_R11P wo11 = new WO_TS00_R11P(context, workTargetData, selectedItem03, selectedItemList04, selectedItem07,selectedItem08,selectedItem09, item04);
				wo11.show();
				wo11.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						// TODO Auto-generated method stub
						if(wo11.getResult()){
							searchDetail();
						}
					}

				});
				return;
			}
			break;
		default:
			break;
		}
	}
	


	public class workTargetDetailAsync extends
			AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;
		JSONObject returnJson02;

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected String doInBackground(String... params) {
			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url = WebServerInfo.getUrl()
							+ "ip/selectWorkTargetDetail.do";
					String param_url02 = WebServerInfo.getUrl()
							+ "ip/selectASProcReportMail.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();

					arguments.add(new BasicNameValuePair("csEmpId", commonSession.getEmpId()));
					arguments.add(new BasicNameValuePair("workDt", workDt));
					arguments.add(new BasicNameValuePair("jobNo", jobNo));
					returnJson01 = http.getPost(param_url, arguments, true);
					ej01 = new EasyJsonMap(returnJson01.getJSONObject("dataMap"));
					
					arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));
					arguments.add(new BasicNameValuePair("workDt", workDt));
					arguments.add(new BasicNameValuePair("jobNo", jobNo));
					returnJson02 = http.getPost(param_url02, arguments, true);
					ej02 = new EasyJsonMap(returnJson02.getJSONObject("dataMap"));
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return params[0];

			}
			return "None";

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			// 1. bagicWorkTime
			if (result.equals("bagicWorkTime")) {
				workTargetData = null;
				try {
					workTargetData = new WO_TS00_R00_ITEM03(ej01.getValue("CS_EMP_ID"),
							ej01.getValue("WORK_DT"), ej01.getValue("JOB_NO"),
							ej01.getValue("WORK_NM"), ej01.getValue("ST"),ej01.getValue("JOB_ST"),
							ej01.getValue("CS_DT"), ej01.getValue("CS_FR"),
							ej01.getValue("BLDG_NO"), ej01.getValue("BLDG_NM"),
							ej01.getValue("CAR_NO"),
							ej01.getValue("RESERV_ST"), ej01.getValue("ADDR"),
							ej01.getValue("MAIN_EMP_NM"),
							ej01.getValue("MAIN_EMP_PHONE"),
							ej01.getValue("SUB_EMP_NM"),
							ej01.getValue("SUB_EMP_PHONE"),
							ej01.getValue("CS_DEPT_NM"),
							ej01.getValue("NOTIFY_NM"),
							ej01.getValue("NOTIFY_PHONE"),
							ej01.getValue("RECEV_DESC"),
							ej01.getValue("RECEV_TM"),
							ej01.getValue("RESERV_TM"),
							ej01.getValue("REPAIR_TM"),
							ej01.getValue("MOVE_TM"),
							ej01.getValue("ARRIVE_TM"),
							ej01.getValue("COMPLETE_TM"),
							ej01.getValue("RESCUE_TM"),
							ej01.getValue("START_TM"),
							ej01.getValue("CONTACT_CD"),
							ej01.getValue("STATUS_CD"),
							ej01.getValue("REF_CONTR_NO"),
							ej01.getValue("PARTS_NO"),
							ej01.getValue("MODEL_NM"),
							ej01.getValue("RECEV_NO"),
							ej01.getValue("CHECK_YN"), ej01.getValue("CBS_YN"),ej01.getValue("SPECIAL_DESC"));

				} catch (JSONException e) {
					e.printStackTrace();
					workTargetData = null;
				}
				item04 = null;
				try {
					item04 = new WO_TS00_R00_ITEM04(ej02.getValue("CS_EMP_ID"),
							ej02.getValue("WORK_DT"), ej02.getValue("JOB_NO"),
							ej02.getValue("MAILID1"), ej02.getValue("MAILID2"),ej02.getValue("SEND_ST"),
							ej02.getValue("BLDG_NO"), ej02.getValue("BLDG_NM"),
							ej02.getValue("CAR_NO"),  ej02.getValue("CAR_CD"),
							ej02.getValue("RECEV_NO"), ej02.getValue("RECEV_TM"),
							ej02.getValue("RESERV_TM"),
							ej02.getValue("MOVE_TM"),
							ej02.getValue("ARRIVE_TM"),
							ej02.getValue("COMPLETE_TM"),
							ej02.getValue("RESCUE_TM"),
							ej02.getValue("001"),
							ej02.getValue("EMP_NM"),
							ej02.getValue("CS_EMP_NM"),
							ej02.getValue("PHONE_1"),
							ej02.getValue("1"),
							ej02.getValue("EX_ORDER_CD"),
							ej02.getValue("STATUS_CD"),
							ej02.getValue("CBS_CD_1"),
							ej02.getValue("CBS_CD_2"),
							ej02.getValue("CBS_CD_3"),
							ej02.getValue("FAULT_CD"),
							ej02.getValue("PROC_CD"),
							ej02.getValue("DUTY_CD"),
							ej02.getValue("AS_CD_1"),
							ej02.getValue("AS_CD_2"),
							ej02.getValue("AS_RMK"),
							ej02.getValue("CUSTOMER"), ej02.getValue("CUST_SIGN"));

				} catch (JSONException e) {
					e.printStackTrace();
					item04 = null;
				}
				progress(false);
				if (workTargetData != null) {
					setDetailView();
				}
			}
		}
	}

	
	private void searchDetail() {
		progress(true);
		new workTargetDetailAsync().execute("bagicWorkTime");
	}

	private void initDetailView() {
		tv_fr_date.setText("");
		tv_fr_workNm.setText("");
		tv_fr_bldgInfo.setText("");
		tv_fr_addr.setText("");
		tv_fr_csDeptNm.setText("");
		tv_fr_empNm1.setText("");
		tv_fr_empHp1.setText("");
		tv_fr_empNm2.setText("");
		tv_fr_empHp2.setText("");
		tv_fr_notifyNm.setText("");
		tv_fr_recevDesc.setText("");
		tv_fr_specialDesc.setText("");
		setReserverDateTime("");

		
		
		this.tv_fr_rescuetime.setVisibility(View.GONE);
		this.tv_fr_rescuetime.setText("");
		
		tv_fr_travelTime.setText("");
		tv_fr_arrivalTime.setText("");
		tv_fr_repaireCompletionTime.setText("");
		
		this.cb_fr_confined.setEnabled(false);
		this.cb_fr_respond.setEnabled(false);
		
		tv_fr_completionTime.setText("");
		tv_fr_st.setText("");
		tv_fr_csfr.setText("");
	}

	private void setDetailView() {
		initDetailView();
		tv_fr_date.setText(workTargetData.getWorkDt());
		tv_fr_workNm.setText(workTargetData.getWorkNm());
		tv_fr_bldgInfo.setText(workTargetData.getBldgNm() + " " + workTargetData.getCarNo());
		tv_fr_addr.setText(workTargetData.getAddr());
		tv_fr_csDeptNm.setText(workTargetData.getCsDetpNm());
		tv_fr_empNm1.setText(workTargetData.getMainEmpNm());
		tv_fr_empHp1.setText(workTargetData.getMainEmpPhone());
		tv_fr_empNm2.setText(workTargetData.getSubEmpNm());
		tv_fr_empHp2.setText(workTargetData.getSubEmpPhone());
		tv_fr_notifyNm.setText(workTargetData.getNotifyNm());
		tv_fr_recevDesc.setText(workTargetData.getRecevDesc());
		tv_fr_specialDesc.setText(workTargetData.getSpecialDesc());
		tv_fr_st.setText(workTargetData.getSt());
		tv_fr_csfr.setText(workTargetData.getCsFr());
		
		setReserverDateTime(workTargetData.getReservTm());

		tv_fr_travelTime.setText(workTargetData.getMoveTm());
		tv_fr_arrivalTime.setText(workTargetData.getArriveTm());
		
		tv_fr_repaireCompletionTime.setText(workTargetData.getRepairTm());
		
		if (workTargetData.getRepairTm() != null && !workTargetData.getRepairTm().equals("")) {
			this.cb_fr_confined.setEnabled(true);
			this.cb_fr_respond.setEnabled(true);
		}
		else{
			this.cb_fr_confined.setEnabled(false);
			this.cb_fr_respond.setEnabled(false);
		}
		
		tv_fr_completionTime.setText(workTargetData.getCompleteTm());
		this.cb_fr_confined.setChecked(workTargetData.isStatus());

		setRescueDateTime(workTargetData.getRescueTm());
		
		if(workTargetData.isStatus()){
			this.tv_fr_rescuetime.setVisibility(View.VISIBLE);
		}
		else
		{
			this.tv_fr_rescuetime.setVisibility(View.GONE);
		}
		
		this.cb_fr_respond.setChecked(workTargetData.isContact());
		if( item04.getCustSign() != null && !item04.getCustSign().equals("")){
			this.btn_fr_aSProcReport.setText(StringUtil.padLeft("A/S 처리 보고서", "등록"));
		}
		else{
			this.btn_fr_aSProcReport.setText("A/S 처리 보고서");
		}
	}

	private void progress(Boolean isActivated) {
		if (isActivated) {
			CL_TS00_R00.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
			
		} else {
			CL_TS00_R00.this.progress.dismiss();
		}
	}

	private String jobNo;
	private String workDt;
	private String jobAct;
	
	private int reserverHour;
	private int reserverMinute;
	private int reserveYear;
	private int reserveMonth;
	private int reserveDay;
	private String reserveDateTime;
	private void setReserverDateTime(String value){
		reserveDateTime = value;
		btn_fr_reserverTime.setText(StringUtil.padLeft(CL_TS00_R00.this
				.getString(R.string.btnstr_reserverTime), value));
		
		if (value.equals(null) || value.equals("")) {
			value = DateUtil.nowDateTime();
			Log.v("currnetDateTime", value);
		}
		
		reserveYear = DateUtil.getYear(value);
		reserveMonth = DateUtil.getMonth(value);
		reserveDay = DateUtil.getDay(value);
		reserverHour = DateUtil.getHour(value);
		reserverMinute = DateUtil.getMinute(value);
		Log.v("reserveMonth",String.valueOf(reserveMonth));
	}

	private int rescueHour;
	private int rescueMinute;
	private int rescueYear;
	private int rescueMonth;
	private int rescueDay;
	private String rescueDateTime;
	private void setRescueDateTime(String value){
		rescueDateTime = value;
		tv_fr_rescuetime.setText(StringUtil.padLeft(CL_TS00_R00.this
				.getString(R.string.btnstr_rescueTime), value));
		
		if (value.equals(null) || value.equals("")) {
			value = DateUtil.nowDateTime();
			Log.v("currnetDateTime", value);
		}
		
		rescueYear = DateUtil.getYear(value);
		rescueMonth = DateUtil.getMonth(value);
		rescueDay = DateUtil.getDay(value);
		rescueHour = DateUtil.getHour(value);
		rescueMinute = DateUtil.getMinute(value);
		Log.v("reserveMonth",String.valueOf(reserveMonth));
	}

	public CM_SearchBldgInfo_ITEM01 currentSelectedItem01;

	
	private WO_TS00_R03P_ITEM01 selectedItem03;
	private void setSelectedItem03(WO_TS00_R03P_ITEM01 item){
		this.selectedItem03 = item;
		tv_fr_aSProcCode.setText(this.selectedItem03.getCodeNm());
	}
	private List<WO_TS00_R04P_ITEM01> selectedItemList04;
	private void setSelectedItemList4(List<WO_TS00_R04P_ITEM01> itemList){
		selectedItemList04 = itemList;
		this.tv_fr_CbsNm1.setText(itemList.get(0).getAssyNm());
		this.tv_fr_CbsNm2.setText(itemList.get(1).getAssyNm());
		this.tv_fr_CbsNm3.setText(itemList.get(1).getAssyNm());
	}
	private WO_TS00_R07P_ITEM01 selectedItem07;
	private void setSelectedItem07(WO_TS00_R07P_ITEM01 item){
		this.selectedItem07 = item;
		tv_fr_falutNm.setText(this.selectedItem07.getFaultNm());
	}
	
	private WO_TS00_R08P_ITEM01 selectedItem08;
	private void setSelectedItem08(WO_TS00_R08P_ITEM01 item){
		this.selectedItem08 = item;
		tv_fr_procNm.setText(this.selectedItem08.getProcNm());
	}
	private WO_TS00_R09P_ITEM01 selectedItem09;
	private void setSelectedItem09(WO_TS00_R09P_ITEM01 item){
		this.selectedItem09 = item;
		tv_fr_dutyNm.setText(this.selectedItem09.getDutyNm());
	}
	
	private List<String> cpErrorCodeList;
	private void setCpErrorCodeList(List<String> itemList){
		this.cpErrorCodeList  = itemList;
		if(this.cpErrorCodeList.size() > 0){
			this.tv_fr_cpErrorCode.setText("등록");
		}
		else
		{
			this.tv_fr_cpErrorCode.setText("");
		}
	}
	
	private String selCode;
	
	
	//----------------------내비게이션 영역--------------------------------------//
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
						Log.e("isHide", "naviHide = " +isHide );
					}else{
						testnavi.setPadding(0, 0, 0, 0); testnavi.setAlpha((float)1);
						Log.e("isHide", "naviHide = " +naviPref.isHide() );
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
			//----------------------내비게이션 영역--------------------------------------//
			

}

	
