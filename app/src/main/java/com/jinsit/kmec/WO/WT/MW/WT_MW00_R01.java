package com.jinsit.kmec.WO.WT.MW;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog.btnClickListener;
import com.jinsit.kmec.WO.WT.RI.WO_WT00_R01_ITEM00;
import com.jinsit.kmec.WO.WT.TS.WO_TS00_R00_ITEM01;
import com.jinsit.kmec.WO.WorkCD;
import com.jinsit.kmec.comm.CallService;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.GPSService;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.SimpleDialog;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;


/**
 * @discription 작업이 시작과 끝만 있는것 
 * 점심식사/저녁식사/철야식사/ 회의교육 / 작업준비/ 고장대기/ 사무정리/ 자택복귀/ 사무실복귀/ 상주근무
 * @author 원성민
 *
 */
public class WT_MW00_R01 extends Activity implements OnClickListener{
	
	
	private static final int REGISTER_PEND = 0;
	private static final int REGISTER_TRANSFER = 1;
	private static final int REGISTER_HELP = 2;

	private static final String JOB_ACT_MOVE = "11";
	private static final String JOB_ACT_START = "15";
	private static final String JOB_ACT_ARRIVE = "31";
	private static final String JOB_ACT_COMPLETE = "39";
	Context context;
	Activity activity;
	TextView tv_mw_r01date, tv_mw_r01workNm,  tv_mw_r01st, tv_mw_r01cs_fr, 	tv_mw_r01startTm ,tv_mw_r01completeTm;
	Button  btn_mw_r01jobComplete;
	private ProgressDialog ProgressDialog;
	private ProgressDialog progress;

	
	private String jobNo, empId, workDt, carNo;//
	String nfcPlc;
	String refControlNo;
	private String jobAct;
	private EasyJsonMap ejm;
	private CommonSession commonSession;
	private EasyJsonMap ej01;
	private WO_WT00_R01_ITEM00 workTargetData;
	private WO_TS00_R00_ITEM01 item01;

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wt_mw00_r01);
		activityInit();
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("작업대상정보");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
	}

	private void activityInit() {
		// TODO Auto-generated method stub
		activity = this;
		context = this;
		commonSession = new CommonSession(context);
		jobNo = getIntent().getExtras().getString("jobNo");
		workDt = getIntent().getExtras().getString("workDt");
		// Log.e("jobNo", jobNo);
		tv_mw_r01date = (TextView) findViewById(R.id.tv_mw_r01date);
		tv_mw_r01workNm = (TextView) findViewById(R.id.tv_mw_r01workNm);
		tv_mw_r01st = (TextView) findViewById(R.id.tv_mw_r01st);
		tv_mw_r01cs_fr = (TextView) findViewById(R.id.tv_mw_r01cs_fr);
		tv_mw_r01startTm = (TextView)findViewById(R.id.tv_mw_r01startTm);
		tv_mw_r01startTm.setOnClickListener(this);
		tv_mw_r01completeTm = (TextView) findViewById(R.id.tv_mw_r01completeTm);
	
		btn_mw_r01jobComplete = (Button) findViewById(R.id.btn_mw_r01jobComplete);
		btn_mw_r01jobComplete.setOnClickListener(this);
		new WorkTargetDetailAsync().execute();
		setConfig();
	}

	private void setConfig(){
		ActivityAdmin.getInstance().addActivity(this);
	}
	
	private void dataSetting() {
		tv_mw_r01date.setText(workTargetData.getWORK_DT());
		tv_mw_r01workNm.setText(workTargetData.getWORK_NM());
		tv_mw_r01st.setText(workTargetData.getST());
		tv_mw_r01cs_fr.setText(workTargetData.getCS_FR());
		tv_mw_r01startTm.setText(workTargetData.getSTART_TM());
		tv_mw_r01completeTm.setText(workTargetData.getCOMPLETE_TM());

		empId = workTargetData.getCS_EMP_ID();
		
		workDt = workTargetData.getWORK_DT();
		refControlNo = workTargetData.getREF_CONTR_NO();
		carNo = workTargetData.getCAR_NO();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
	
		case R.id.tv_mw_r01startTm:
			if(workTargetData.getST().equals("계획")){
				timeRegistration(JOB_ACT_START);
			}
			else{
				////계획모드에서만 예약을 할 수 있습니다.
				SimpleDialog sm01 = new SimpleDialog(context, "알림","계획상태에서 이동을 할 수 있습니다.");
				sm01.show();
			}
			break;
	
		case R.id.btn_mw_r01jobComplete:
			if(!workTargetData.getST().equals("계획")){
				this.jobAct = JOB_ACT_COMPLETE;
				repaireTimeRegistration();
			}
			else{
				////계획모드에서만 예약을 할 수 있습니다.
				SimpleDialog sm01 = new SimpleDialog(context, "알림","시작을 먼저 하십시오.");
				sm01.show();
			}
			break;
		}
	}
	private void timeRegistration(String jobAct) {
		String dMsg = "";
		GPSService gs = new GPSService(context);
		if (!gs.isGetLocation()) {
			gpsActivation();
			return;
		}
		
		if (jobAct.equals(JOB_ACT_START)) {
			dMsg = "작업을 시작하시겠습니까?";
		}else if (jobAct.equals(JOB_ACT_COMPLETE)) {
			dMsg = this.getString(R.string.msg_completionTimeRegistration);
		}
		this.jobAct = jobAct;
		SimpleDialog ynDialog = new SimpleDialog(context, "알림",dMsg,
				new com.jinsit.kmec.comm.jinLib.SimpleDialog.btnClickListener() {

					@Override
					public void onButtonClick() {
						// TODO Auto-generated method stub
						progress(true);
						new TimeStatusChangeAsync().execute("bagicWorkTime");
					}
				});
		ynDialog.show();
	}
	
	

	private void progress(Boolean isActivated) {
		if (isActivated) {
			WT_MW00_R01.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			WT_MW00_R01.this.progress.dismiss();
		}
	}

	private void repaireTimeRegistration() {
		String dMsg = "";
		GPSService gs = new GPSService(context);
		if (!gs.isGetLocation()) {
			gpsActivation();
			return;
		}
		
		dMsg = "작업을 완료하시겠습니까?";
		SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(context, dMsg,
				new btnClickListener(){

					@Override
					public void onButtonClick() {
						// TODO Auto-generated method stub
						progress(true);
						new TimeStatusChangeAsync().execute("bagicWorkTime");
						//
						
						
					}
				});
		ynDialog.show();

	}



	private void eatJobQuickComplete(){
		if(!workTargetData.getST().equals("계획")){
			this.jobAct = JOB_ACT_COMPLETE;
			//repaireTimeRegistration();
			progress(true);
			new TimeStatusChangeAsync().execute("bagicWorkTime");
		}
	}
	
	/**
	 * 작업대상 상세정보 불러오기
	 * 
	 * @author 원성민
	 *
	 */
	private class WorkTargetDetailAsync extends AsyncTask<Void, String, Void> {

		@Override
		protected void onPostExecute(Void result) {
			WT_MW00_R01.this.ProgressDialog.dismiss();
			try {
				workTargetData = new WO_WT00_R01_ITEM00(ejm.getValue(
						"CS_EMP_ID"), ejm
						.getValue( "WORK_DT"), ejm.getValue(
						 "JOB_NO"),ejm
							.getValue( "WORK_CD"), ejm
						.getValue( "WORK_NM"), ejm.getValue(
						 "ST"), ejm.getValue( "CS_DT"), ejm
						.getValue( "CS_FR"), ejm.getValue(
						"BLDG_NO"), ejm.getValue( "BLDG_NM"),
						ejm.getValue( "CAR_NO"), ejm
								.getValue( "RESERV_ST"), ejm
								.getValue( "ADDR"), ejm
								.getValue( "MAIN_EMP_NM"),
						ejm.getValue( "MAIN_EMP_PHONE"), ejm
								.getValue( "SUB_EMP_NM"), ejm
								.getValue( "SUB_EMP_PHONE"),
						ejm.getValue( "CS_DEPT_NM"), ejm
								.getValue( "NOTIFY_NM"), ejm
								.getValue( "NOTIFY_PHONE"),
						ejm.getValue( "RECEV_DESC"), ejm
								.getValue( "RECEV_TM"), ejm
								.getValue( "RESERV_TM"), ejm
								.getValue( "REPAIR_TM"), ejm
								.getValue( "MOVE_TM"), ejm
								.getValue( "ARRIVE_TM"), ejm
								.getValue( "COMPLETE_TM"),
						ejm.getValue( "RESCUE_TM"), ejm
								.getValue( "START_TM"), ejm
								.getValue( "CONTACT_CD"), ejm
								.getValue( "STATUS_CD"), ejm
								.getValue( "REF_CONTR_NO"),
						ejm.getValue("PARTS_NO"), ejm
								.getValue( "MODEL_NM"), ejm
								.getValue( "RECEV_NO"), ejm
								.getValue( "CHECK_YN"), ejm
								.getValue( "CBS_YN"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			dataSetting();

			//식사관련 작업일 때
			if((workTargetData.getWORK_CD().equals(WorkCD.점심식사) || workTargetData.getWORK_CD().equals(WorkCD.저녁식사)
					|| workTargetData.getWORK_CD().equals(WorkCD.철야식사) || workTargetData.getWORK_CD().equals(WorkCD.저녁간식))
					&& JOB_ACT_START.equals(jobAct)){
				//onClick(btn_mw_r01jobComplete);
				eatJobQuickComplete();

			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			WT_MW00_R01.this.ProgressDialog = android.app.ProgressDialog.show(
					WT_MW00_R01.this, "작업대상", "작업대상목록 불러오는중");
		}

		@Override
		protected Void doInBackground(Void... params) {

			workTargetData = new WO_WT00_R01_ITEM00();

			GetHttp getHttp = new GetHttp();
			String param_url = WebServerInfo.getUrl()
					+ "ip/selectWorkTargetDetail.do";
			List<NameValuePair> arguments = new ArrayList<NameValuePair>();

			arguments.add(new BasicNameValuePair("csEmpId", commonSession.getEmpId()));
			arguments.add(new BasicNameValuePair("workDt", 	workDt));
			arguments.add(new BasicNameValuePair("jobNo", jobNo));
			JSONObject returnJson = getHttp.getPost(param_url, arguments,
					true);

			try {
					ejm = new EasyJsonMap(
							returnJson.getJSONObject("dataMap"));
				} catch (JSONException e) {
					e.printStackTrace();
				}

			return null;
	}
		
	}
	
	
	
	private class TimeStatusChangeAsync extends AsyncTask<String, Integer, String> {
		GetHttp http = new GetHttp();
		JSONObject returnJson01;

	
		@Override
		protected String  doInBackground(String... params) {
			// 1. bagicWorkTime
			
			if (params[0].equals("bagicWorkTime")) {
			try {
				String param_url = WebServerInfo.getUrl()
						+ "ip/updateWorkStatusChanged.do";
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", workTargetData.getWORK_DT()));
				arguments.add(new BasicNameValuePair("jobNo",  workTargetData.getJOB_NO()));
				arguments.add(new BasicNameValuePair("jobAct", jobAct));
				arguments.add(new BasicNameValuePair("usrId", commonSession.getEmpId()));
				returnJson01 = http.getPost(param_url, arguments,
						true);

				try {
					ej01 = new EasyJsonMap(returnJson01.getJSONObject("dataMap"));

				} catch (JSONException e) {
					e.printStackTrace();
				}

			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
				return "None";
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
			progress(false);
			if (result.equals("bagicWorkTime")) {
				item01 = null;
				try {
					item01 = new WO_TS00_R00_ITEM01(ej01.getValue("JOB_NO"),
							ej01.getValue("JOB_ST_NM"),
							ej01.getValue("AS_TP"),
							ej01.getValue("CS_TM_FR"));
				} catch (JSONException e) {
					e.printStackTrace();
					item01 = null;
				}

				//progress(false);
				if(item01 != null){

					if(item01 != null){
//						if(jobAct.equals(JOB_ACT_MOVE)){
//							workTargetData.setMOVE_TM(item01.getCsTmFr());
//						}else if(jobAct.equals(JOB_ACT_ARRIVE)){
//							workTargetData.setARRIVE_TM(item01.getCsTmFr());
//						}else if(jobAct.equals(JOB_ACT_COMPLETE)){
//							workTargetData.setCOMPLETE_TM(item01.getCsTmFr());
//						}
//						tv_mw_st.setText(item01.getJobStNm());
//						tv_mw_cs_fr.setText(item01.getCsTmFr());
//						workTargetData.setST(item01.getJobStNm());
//						workTargetData.setCS_FR(item01.getCsTmFr());
						
						
						if(jobAct.equals(JOB_ACT_COMPLETE)){
							CallService.startGPSService(context);
							finish();
						}else{
							CallService.startGPSService(context);
							new WorkTargetDetailAsync().execute();
						}
					}
				}
			}
			
		}
	

}
	
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
	void gpsActivation() {
		new AlertDialog.Builder(context)
		.setTitle("위치서비스 동의")
		.setMessage("GPS를 켜지 않으면 작업을 시작할 수 없습니다.")
		.setNeutralButton("이동",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						context.startActivity(new Intent(
								android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					}
				})
			
				.show();
	}

}
